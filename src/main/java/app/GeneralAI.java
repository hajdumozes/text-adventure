package app;

import characters.Character;
import combat.DistanceBased;
import combat.Position;
import combat.Targetable;
import combat.UnreachablePositionException;
import combat.skills.Skill;

import java.util.*;

public class GeneralAI {

    public void letGeneralAIDecide(Character character) {
        Character chosenTarget = letAiChooseOpponent(character);
        moveTowardsTarget(character, chosenTarget);
        int distance = new Battlefield().countPositionDifference(character.getPosition(), chosenTarget.getPosition());
        Skill selectedSkillInReach = letAiChooseSkill(character, distance);
        if (selectedSkillInReach != null) {
            evaluateSkill(selectedSkillInReach, chosenTarget);
        } else if (distance <= character.getWeaponReach()) {
            new CharacterActions().attack(character, chosenTarget);
        } else {
            moveTowardsTarget(character, chosenTarget);
        }
    }

    private Skill letAiChooseSkill(Character character, int distance) {
        List<Skill> allAvailableSkills = new SkillManagement().getUsableSkills(character);
        List<Skill> filteredSkills = filterSkillsInReach(allAvailableSkills, distance);
        if (filteredSkills.size() > 0) {
            return filteredSkills.get(new Random().nextInt(filteredSkills.size()));
        } else {
            return null;
        }
    }

    private List<Skill> filterSkillsInReach(List<Skill> skills, int distance) {
        List<Skill> filteredList = new ArrayList<>();
        for (Skill skill : skills) {
            if (!(skill instanceof DistanceBased) || ((DistanceBased) skill).getReach() >= distance) {
                filteredList.add(skill);
            }
        }
        return filteredList;
    }

    private void evaluateSkill(Skill skill, Character target) {
        if (skill instanceof Targetable) {
            skill.applyTo(Collections.singletonList(target));
        } else {
            skill.applyTo(Collections.singletonList(null));
        }
    }

    private void moveTowardsTarget(Character character, Character target) {
        List<Position> optimalRoutes = selectOptimalRoutesToPosition(character, target.getPosition());
        moveToSelectedDestination(character, optimalRoutes);
    }

    private void moveToSelectedDestination(Character character, List<Position> optimalRoutes) {
        try {
            List<Position> filteredRoutes = filterNonOccupiedRoutes(optimalRoutes);
            if (filteredRoutes.size() > 0) {
                Position destination = selectFromOptimalRoutes(filteredRoutes);
                new Movement().move(character, destination);
            }
        } catch (UnreachablePositionException positionException) {
            moveToSelectedDestination(character, optimalRoutes);
        }
    }

    private Character letAiChooseOpponent(Character character) {
        int percentageChance = new Random().nextInt(100) + 1;
        List<Character> enemies = new AttackEvaluation().findPossibleTargets(!character.isFriendly());
        enemies.sort(Comparator.comparing(enemy -> enemy.getHealth().getCurrentValue()));
        if (percentageChance < 70) {
            return enemies.get(enemies.size() - 1);
        } else {
            return enemies.get(new Random().nextInt(enemies.size()));
        }
    }

    protected Position getNearestEnemyPosition(Character character) {
        Battlefield battlefield = new Battlefield();
        List<Character> possibleTargets = new AttackEvaluation().findPossibleTargets(!character.isFriendly());
        Position positionFound = possibleTargets.get(0).getPosition();
        int minimalDifference = battlefield.countPositionDifference(character.getPosition(), positionFound);
        for (Character enemy : possibleTargets) {
            int actualDifference = battlefield.countPositionDifference(character.getPosition(), enemy.getPosition());
            if (actualDifference < minimalDifference) {
                positionFound = enemy.getPosition();
                minimalDifference = actualDifference;
            }
        }
        return positionFound;
    }

    private List<Position> selectOptimalRoutesToPosition(Character character, Position destination) {
        Battlefield battlefield = new Battlefield();
        List<Position> allRoutes = new Movement().findAllPossibleRoutes(character);
        List<Position> optimalRoutes = new ArrayList<>();
        for (Position position : allRoutes) {
            int currentDifference = battlefield.countPositionDifference(position, destination);
            if (currentDifference != 0) {
                if (optimalRoutes.isEmpty()) {
                    optimalRoutes.add(position);
                } else if (battlefield.countPositionDifference(optimalRoutes.get(0), destination) >
                        battlefield.countPositionDifference(position, destination)) {
                    optimalRoutes.clear();
                    optimalRoutes.add(position);
                } else if (battlefield.countPositionDifference(optimalRoutes.get(0), destination) ==
                        battlefield.countPositionDifference(position, destination)) {
                    optimalRoutes.add(position);
                }
            }
        }
        return optimalRoutes;
    }

    private List<Position> filterNonOccupiedRoutes(List<Position> routes) {
        List<Position> filtered = new ArrayList<>();
        for (Position position : routes) {
            if (new Battlefield().checkIfPositionIsOccupied(position)) {
                filtered.add(position);
            }
        }
        return filtered;
    }

    private Position selectFromOptimalRoutes(List<Position> optimalRoutes) {
        return optimalRoutes.get(new Random().nextInt(optimalRoutes.size()));
    }
}

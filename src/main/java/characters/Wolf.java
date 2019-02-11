package characters;

import combat.DistanceBased;
import combat.Position;
import combat.Targetable;
import combat.UnreachablePositionException;
import combat.skills.Skill;
import combat.skills.ownable.Bite;
import combat.skills.ownable.Howl;
import items.Equipment.Equipment;
import items.Equipment.ownable.WolfClaw;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static app.AttackEvaluation.findPossibleTargets;
import static app.Battlefield.*;
import static app.SkillManagement.getUsableSkills;
import static app.SkillManagement.invokeMethod;

public class Wolf extends Character {

    {
        setPosition(new Position(5, 6));
    }

    public Wolf() {
        super("Wolf", "Wolf", 11, 15, 5, 5,
                new Equipment(new WolfClaw(), new WolfClaw()), false);
        getSkills().add(new Bite(this));
        getSkills().add(new Howl(this));
    }

    @Override
    public void letAiDecide() {
        Character chosenTarget = letAiChooseTarget();
        moveTowardsTarget(chosenTarget);
        int distance = countPositionDifference(getPosition(), chosenTarget.getPosition());
        Skill selectedSkillInReach = letAiChooseSkill(distance);
        if (selectedSkillInReach != null) {
            evaluateSkill(selectedSkillInReach, chosenTarget);
        } else if (distance <= getWeaponReach()) {
            attack(chosenTarget);
        } else {
            moveTowardsTarget(chosenTarget);
        }
    }

    private Skill letAiChooseSkill(int distance) {
        List<Skill> allAvailableSkills = getUsableSkills(this);
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
            invokeMethod(skill.getMethod(), skill, target);
        } else {
            invokeMethod(skill.getMethod(), skill, null);
        }
    }

    private void moveTowardsTarget(Character target) {
        List<Position> optimalRoutes = selectOptimalRoutesToPosition(target.getPosition());
        moveToSelectedDestination(optimalRoutes);
    }

    private void moveToSelectedDestination(List<Position> optimalRoutes) {
        try {
            List<Position> filteredRoutes = filterNonOccupiedRoutes(optimalRoutes);
            if (filteredRoutes.size() > 0) {
                Position destination = selectFromOptimalRoutes(filteredRoutes);
                move(destination);
            }
        } catch (UnreachablePositionException positionException) {
            moveToSelectedDestination(optimalRoutes);
        }
    }

    private Character letAiChooseTarget() {
        int percentageChance = new Random().nextInt(100) + 1;
        List<Character> enemies = findPossibleTargets(true);
        enemies.sort(Comparator.comparing(character -> character.getHealth().getCurrentValue()));
        if (percentageChance < 70) {
            return enemies.get(enemies.size() - 1);
        } else {
            return enemies.get(new Random().nextInt(enemies.size()));
        }
    }

    private Position getNearestEnemyPosition() {
        List<Character> possibleTargets = findPossibleTargets(true);
        Position positionFound = null;
        int minimalDifference = countPositionDifference(getPosition(), possibleTargets.get(0).getPosition());
        for (Character enemy : possibleTargets) {
            if (countPositionDifference(getPosition(), enemy.getPosition()) < minimalDifference) {
                positionFound = enemy.getPosition();
            }
        }
        return positionFound;
    }

    private List<Position> selectOptimalRoutesToPosition(Position destination) {
        List<Position> allRoutes = findAllPossibleRoutes(this);
        List<Position> optimalRoutes = new ArrayList<>();
        for (Position position : allRoutes) {
            int currentDifference = countPositionDifference(position, destination);
            if (currentDifference != 0) {
                if (optimalRoutes.isEmpty()) {
                    optimalRoutes.add(position);
                } else if (countPositionDifference(optimalRoutes.get(0), destination) > countPositionDifference(position, destination)) {
                    optimalRoutes.clear();
                    optimalRoutes.add(position);
                } else if (countPositionDifference(optimalRoutes.get(0), destination) == countPositionDifference(position, destination)) {
                    optimalRoutes.add(position);
                }
            }
        }
        return optimalRoutes;
    }

    private List<Position> filterNonOccupiedRoutes(List<Position> routes) {
        List<Position> filtered = new ArrayList<>();
        for (Position position : routes) {
            if (checkIfPositionIsOccupied(position)) {
                filtered.add(position);
            }
        }
        return filtered;
    }

    private Position selectFromOptimalRoutes(List<Position> optimalRoutes) {
        return optimalRoutes.get(new Random().nextInt(optimalRoutes.size()));
    }
}

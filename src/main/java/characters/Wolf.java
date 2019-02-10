package characters;

import app.Main;
import attributes.DamageBonus;
import combat.*;
import items.Equipment.Equipment;
import items.WolfClaw;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static app.AttackEvaluation.findPossibleTargets;
import static app.Battlefield.*;
import static app.Combat.rollInitiative;
import static app.SkillManagement.*;

public class Wolf extends Character {

    private SkillWithCountDown howl = new SkillWithCountDown("Howl",
            "Calls for another wolf, which arrives in 3 turns.",
            findMethod("howl", null), 1,
            new Target(false, false), 3,
            findMethod("wolfArrive", null));
    private Skill bite = new Skill("Bite", "2d4 +2 damage, +4 to hit",
            findMethod("bite", Character.class),
            3, new Target(true, true));

    {
        setPosition(new Position(5, 6));
    }

    public Wolf() {
        super("Wolf", "Wolf", 11, 15, 5, 5, new Equipment(new WolfClaw(), new WolfClaw()), false);
    }

    @Override
    public List<Skill> showSpecialAttacks() {
        List<Skill> specialAttacks = new ArrayList<>();
        specialAttacks.add(howl);
        specialAttacks.add(bite);
        return specialAttacks;
    }

    public void howl() {
        System.out.println("\tWolf howled!");
        getSkillWithCountDowns().add(howl);
        useSkill(howl);
    }

    public void bite(Character target) {
        this.getDamageBonus().setCurrentValue(getDamageBonus().getCurrentValue() + 2);
        addToStatuses(new Status(new DamageBonus(), "Bite", 2, 1));
        System.out.println("\tWolf bit!");
        int hitRoll = Main.roll(1, 20) + 4;
        evaluateAttackRoll(hitRoll, target);
        useSkill(bite);
    }

    public void wolfArrive() {
        System.out.println("\n\t# Wolf appeared! #");
        Wolf reinforcement = new Wolf();
        Main.CHARACTERS_ALIVE.add(reinforcement);
        rollInitiative(reinforcement);
    }

    @Override
    public void letAiDecide() {
        Character chosenTarget = letAiChooseTarget();
        moveTowardsTarget(chosenTarget);

        if (countPositionDifference(this.getPosition(), chosenTarget.getPosition()) == getReach()) {
            List<Skill> allAvailableSkills = getUsableSkills(this);
            if (allAvailableSkills.size() > 0 && new Random().nextInt(100) + 1 > 50) {
                Skill selectedSkill = allAvailableSkills.get(new Random().nextInt(allAvailableSkills.size()));
                if (selectedSkill.getTarget().isTargetable()) {
                    invokeMethod(selectedSkill.getMethod(), this, chosenTarget);
                } else {
                    invokeMethod(selectedSkill.getMethod(), this, null);
                }
            } else {
                attack(chosenTarget);
            }
        } else {
            moveTowardsTarget(chosenTarget);
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

    private List<Position> findAllPossibleRoutes() {
        List<Position> routes = new ArrayList<>();
        for (int i = 0 - getSpeedValue(); i <= getSpeedValue(); i++) {
            int leftoverSteps = getSpeedValue() - Math.abs(i);
            for (int j = 0 - leftoverSteps; j <= leftoverSteps; j++) {
                int newColumn = getPosition().getColumn() + i;
                int newRow = getPosition().getRow() + j;
                if (newColumn >= 0 && newColumn < BATTLEFIELD[0].length
                        && newRow >= 0 && newRow < BATTLEFIELD.length) {
                    routes.add(new Position(getPosition().getRow() + j, getPosition().getColumn() + i));
                }
            }
        }
        return routes;
    }

    private List<Position> selectOptimalRoutesToPosition(Position destination) {
        List<Position> allRoutes = findAllPossibleRoutes();
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

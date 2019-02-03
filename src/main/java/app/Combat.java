package app;

import attributes.Attribute;
import characters.Character;
import combat.Ability;
import combat.Status;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static app.Main.*;

public class Combat {

    public static void progressThroughBattle() {
        int turnCounter = 0;
        while (getAliveCharactersFromBothSides()) {
            System.out.println("\n--------------------------------------------------");

            turnCounter++;
            System.out.println("\tTurn " + turnCounter);
            refreshStatuses();
            refreshAbilities();

            printAliveCharacterInfo();

            CHARACTERS_ALIVE.sort(Comparator.comparing(Character::getDexterity));
            for (Character character : CHARACTERS_ALIVE) {
                if (character.isAlive() && character.isFriendly()) {
                    System.out.println("\n\tWhat would you like to do?");
                    System.out.println("\t1. Attack");
                    System.out.println("\t2. Wait for certain death");
                    System.out.println("\t3. Defend");
                    System.out.println("\t4. Special attack");

                    String input = CONSOLE.nextLine();
                    evaluateUserInput(input, character);
                } else if (character.isAlive() && !character.isFriendly()) {
                    evaluateEnemyTurn(character);
                }
            }
        }
    }

    private static void printAliveCharacterInfo() {
        for (Character character : CHARACTERS_ALIVE) {
            System.out.println("\t" + character.getName() + "'s HP: " + character.getHealth().getValue());
        }
    }

    public static boolean getAliveCharactersFromBothSides() {
        boolean friendlyIsAlive = false;
        boolean hostileIsAlive = false;
        for (Character character : CHARACTERS_ALIVE) {
            if (character.isFriendly()) {
                friendlyIsAlive = true;
            } else {
                hostileIsAlive = true;
            }
        }
        return friendlyIsAlive && hostileIsAlive;
    }

    private static void evaluateUserInput(String input, Character character) {
        List<Character> possibleTargets = findPossibleTargets(character);
        switch (input) {
            case "1":
                System.out.println("\n\tYou decided to attack.");
                character.attack(possibleTargets.get(0));
                break;
            case "3":
                System.out.println("\tYou decided to defend.");
                character.defend();
                break;
            case "4":
                System.out.println("\tYou decided to use special attack.");
                character.special();
                break;
            default:
                System.out.println("\tYou longed for death");
                character.wait(character);
                break;
        }
    }

    public static List<Character> findPossibleTargets(Character currentCharacter) {
        if (currentCharacter.isFriendly()) {
            return CHARACTERS_ALIVE.stream().filter(character -> !character.isFriendly()).collect(Collectors.toList());
        } else {
            return CHARACTERS_ALIVE.stream().filter(character -> character.isFriendly()).collect(Collectors.toList());
        }
    }

    private static void evaluateEnemyTurn(Character enemy) {
        enemy.letAiDecide();
    }

    private static void refreshStatuses() {
        for (Character character : CHARACTERS_ALIVE) {
            Iterator<Status> iterator = character.getStatuses().iterator();
            while (iterator.hasNext()) {
                Status current = iterator.next();
                current.setDuration(current.getDuration() - 1);
                if (current.getDuration() <= 0) {
                    System.out.println("\n\tEffect of " + current.getName() + " expired.\n");
                    nullifyStatusEffect(current, character);
                    iterator.remove();
                }
            }
        }
    }

    private static void refreshAbilities() {
        for (Character character : CHARACTERS_ALIVE) {
            Iterator<Ability> iterator = character.getAbilityCountdowns().iterator();
            while (iterator.hasNext()) {
                Ability current = iterator.next();
                current.setCountdown(current.getCountdown() - 1);
                if (current.getCountdown() <= 0) {
                    try {
                        current.getMethod().invoke(character);
                    } catch (InvocationTargetException | IllegalAccessException iae) {
                        throw new RuntimeException("Method invoking error" + iae);
                    }
                    iterator.remove();
                }
            }
        }
    }

    private static void nullifyStatusEffect(Status status, Character player) {
        for (Attribute attribute : player.getAttributes()) {
            if (attribute.getName().equals(status.getAttribute().getName())) {
                attribute.decrease(status.getValue());
            }
        }
    }


}

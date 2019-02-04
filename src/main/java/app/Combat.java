package app;

import attributes.Attribute;
import characters.Character;
import combat.Skill;
import combat.Status;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import static app.Main.*;

public class Combat {

    protected static void progressThroughBattle() {
        int turnCounter = 0;
        while (getAliveCharactersFromBothSides()) {
            System.out.println("\n--------------------------------------------------");

            turnCounter++;
            System.out.println(MessageFormat.format("\tTurn {0}", turnCounter));
            refreshStatuses();
            refreshSkillCountdowns();
            printInfoOfAliveCharacters();
            progressThroughTurnsOfAliveCharacters();
        }
    }

    private static void printInfoOfAliveCharacters() {
        for (Character character : CHARACTERS_ALIVE) {
            System.out.println(MessageFormat.format(
                    "\t{0}''s HP: {1}", character.getName(), character.getHealth().getValue()));
        }
    }

    private static void progressThroughTurnsOfAliveCharacters() {
        CHARACTERS_ALIVE.sort(Comparator.comparing(Character::getDexterity));
        for (Character character : CHARACTERS_ALIVE) {
            if (character.isAlive() && character.isFriendly()) {
                System.out.println(MessageFormat.format("\n\t{0}''s turn:", character.getName()));
                System.out.println("\n\tWhat would you like to do?");
                System.out.println("\t1. Attack");
                System.out.println("\t2. Wait for certain death");
                System.out.println("\t3. Defend");
                System.out.println("\t4. Special attack");

                String input = CONSOLE.nextLine();
                evaluateUserInput(input, character);
            } else if (character.isAlive() && !character.isFriendly()) {
                System.out.println(MessageFormat.format("\n\t{0}''s turn:", character.getName()));
                character.letAiDecide();
            }
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
        switch (input) {
            case "1":
                character.attack(chooseEnemy(character));
                break;
            case "3":
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

    private static Character chooseEnemy(Character character) {
        List<Character> possibleTargets = findPossibleTargets(character);
        System.out.println("\n\tWhich enemy you want to attack?");
        for (int i = 1; i <= possibleTargets.size(); i++) {
            System.out.println(MessageFormat.format("\t{0}. {1} - {2} HP",
                    i, possibleTargets.get(i - 1).getName(), possibleTargets.get(i - 1).getHealth().getValue()));
        }
        String input = CONSOLE.nextLine();
        return possibleTargets.get((Integer.parseInt(input) - 1));
    }

    public static List<Character> findPossibleTargets(Character currentCharacter) {
        if (currentCharacter.isFriendly()) {
            return CHARACTERS_ALIVE.stream().filter(character -> !character.isFriendly()).collect(Collectors.toList());
        } else {
            return CHARACTERS_ALIVE.stream().filter(character -> character.isFriendly()).collect(Collectors.toList());
        }
    }

    private static void refreshStatuses() {
        for (Character character : CHARACTERS_ALIVE) {
            Iterator<Status> iterator = character.getStatuses().iterator();
            while (iterator.hasNext()) {
                Status current = iterator.next();
                current.setDuration(current.getDuration() - 1);
                if (current.getDuration() <= 0) {
                    System.out.println(MessageFormat.format("\n\tEffect of {0} expired.\n", current.getName()));
                    nullifyStatusEffect(current, character);
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

    private static void refreshSkillCountdowns() {
        for (Character character : new ArrayList<>(CHARACTERS_ALIVE)) {
            Iterator<Skill> iterator = character.getSkills().iterator();
            while (iterator.hasNext()) {
                Skill current = iterator.next();
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
}

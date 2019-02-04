package app;

import attributes.Attribute;
import characters.Character;
import combat.Skill;
import combat.Status;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import static app.Main.*;

public class Combat {

    protected static void progressThroughBattle() {
        int turnCounter = 0;
        rollInitiativeForAllCharacters();
        while (getAliveCharactersFromBothSides()) {
            System.out.print("\033[H\033[2J"); // it should clear console
            System.out.println("\n--------------------------------------------------");

            turnCounter++;
            System.out.println(MessageFormat.format("\tTurn {0}", turnCounter));
            refreshStatuses();
            refreshSkillCountdowns();
            printInfoOfAliveCharacters();
            progressThroughTurnsOfAliveCharacters();
        }
    }

    private static void rollInitiativeForAllCharacters() {
        for (Character character : CHARACTERS_ALIVE) {
            rollInitiative(character);
        }
    }

    public static void rollInitiative(Character character) {
        int initiative = roll(1, 20) + (character.getDexterity().getValue() / 2);
        character.getInitiative().setValue(initiative);
    }

    private static void printInfoOfAliveCharacters() {
        for (Character character : CHARACTERS_ALIVE) {
            System.out.println(MessageFormat.format(
                    "\t{0}''s HP: {1}", character.getName(), character.getHealth().getValue()));
        }
    }

    private static void progressThroughTurnsOfAliveCharacters() {
        CHARACTERS_ALIVE.sort(Comparator.comparing(Character::getInitiative));
        for (Character character : new ArrayList<>(CHARACTERS_ALIVE)) {
            System.out.println("\n--------------------------------------------------");
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
                Method chosenMethod = chooseSpecial(character);
                invokeMethod(chosenMethod, character);
                break;
            default:
                System.out.println("\tYou longed for death");
                character.wait(character);
                break;
        }
    }

    private static Character chooseEnemy(Character character) {
        List<Character> possibleTargets = findPossibleTargets(character);
        System.out.println("\n\tWhich enemy do you want to use?");
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

    private static Method chooseSpecial(Character character) { //maybe it can be merged with chooseEnemy
        List<Method> specialAttacks = character.showSpecialAttacks();
        System.out.println("\n\tWhich skill do you want to attack?");
        for (int i = 1; i <= specialAttacks.size(); i++) {
            System.out.println(MessageFormat.format("\t{0}. {1}",
                    i, specialAttacks.get(i - 1).getName()));
        }
        String input = CONSOLE.nextLine();
        return specialAttacks.get((Integer.parseInt(input) - 1));
    }

    private static void refreshStatuses() {
        for (Character character : CHARACTERS_ALIVE) {
            Iterator<Status> iterator = character.getStatuses().iterator();
            while (iterator.hasNext()) {
                Status current = iterator.next();
                current.setDuration(current.getDuration() - 1);
                if (current.getDuration() <= 0) {
                    System.out.println(MessageFormat.format(
                            "\n\tEffect of {0} expired on {1}.\n", current.getName(), character.getName()));
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
                    invokeMethod(current.getMethod(), character);
                    iterator.remove();
                }
            }
        }
    }

    private static void invokeMethod(Method method, Character character) {
        try {
            method.invoke(character);
        } catch (InvocationTargetException | IllegalAccessException iae) {
            throw new RuntimeException("Method invoking error" + iae);
        }
    }
}

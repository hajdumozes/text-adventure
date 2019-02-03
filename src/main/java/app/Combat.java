package app;

import attributes.Attribute;
import characters.Character;
import combat.Ability;
import combat.Status;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import static app.Main.*;

public class Combat {

    public static void progressThroughBattle() {
        int turnCounter = 0;
        while (FRIENDLY_PARTY.size() > 0 && HOSTILE_PARTY.size() > 0) {
            System.out.println("\n--------------------------------------------------");
            Character player = FRIENDLY_PARTY.get(0);
            Character wolf = HOSTILE_PARTY.get(0);

            turnCounter++;
            System.out.println("\tTurn " + turnCounter);
            refreshStatuses(player);
            refreshAbilities(wolf);

            printAliveCharacterInfo(FRIENDLY_PARTY);
            printAliveCharacterInfo(HOSTILE_PARTY);

            System.out.println("\n\tWhat would you like to do?");
            System.out.println("\t1. Attack");
            System.out.println("\t2. Wait for certain death");
            System.out.println("\t3. Defend");
            System.out.println("\t4. Special attack");

            String input = CONSOLE.nextLine();
            evaluateUserInput(input, player, wolf);
            evaluateEnemyTurn();
        }
    }

    private static void printAliveCharacterInfo(List<Character> characters) {
        for (Character character : characters) {
            System.out.println("\t" + character.getName() + "'s HP: " + character.getHealth().getValue());
        }
    }

    private static void evaluateUserInput(String input, Character player, Character wolf) {
        switch (input) {
            case "1":
                System.out.println("\n\tYou decided to attack.");
                player.attack(wolf);
                break;
            case "3":
                System.out.println("\tYou decided to defend.");
                player.defend();
                break;
            case "4":
                System.out.println("\tYou decided to use special attack.");
                player.special();
                break;
            default:
                System.out.println("\tYou longed for death");
                player.wait(player);
                break;
        }
    }

    private static void evaluateEnemyTurn() {
        for (Character enemy : HOSTILE_PARTY) {
            enemy.letAiDecide();
        }
    }

    private static void refreshStatuses(Character player) {
        Iterator<Status> iterator = player.getStatuses().iterator();
        while (iterator.hasNext()) {
            Status current = iterator.next();
            current.setDuration(current.getDuration() - 1);
            if (current.getDuration() <= 0) {
                System.out.println("\n\tEffect of " + current.getName() + " expired.\n");
                nullifyStatusEffect(current, player);
                iterator.remove();
            }
        }
    }

    private static void refreshAbilities(Character character) {
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

    private static void nullifyStatusEffect(Status status, Character player) {
        for (Attribute attribute : player.getAttributes()) {
            if (attribute.getName().equals(status.getAttribute().getName())) {
                attribute.decrease(status.getValue());
            }
        }
    }
}

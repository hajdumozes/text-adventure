package app;

import attributes.Attribute;
import characters.*;
import characters.Character;
import combat.Ability;
import combat.Status;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static final List<Character> FRIENDLY_PARTY = new ArrayList<>();
    public static final List<Character> HOSTILE_PARTY = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Character player = new Barbarian();
        Character wolf = new Wolf();
        FRIENDLY_PARTY.add(player);
        HOSTILE_PARTY.add(wolf);
        System.out.println(
                "\t Every good story begins with a traveler, who is lost in the woods and have to \n" +
                        "\t climb the ladder of the hierarchy from the very base of it. Should we even start telling \n" +
                        "\t this tale, or the newcoming hero dies to the first lonely wolf in the deep of the woods? \n" +
                        "\t Let's find out!\n");
        int turnCounter = 0;

        progressThroughBattle(turnCounter, scanner);
        decideOutcome();
    }


    private static void progressThroughBattle(int turnCounter, Scanner scanner) {
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

            String input = scanner.nextLine();
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

    private static void decideOutcome() {
        if (FRIENDLY_PARTY.size() > 0) {
            System.out.println("\n\tCongratulations you've won! You may begin your journey!");
        } else {
            System.out.println("\n\tYou are dead.");
        }
    }
}

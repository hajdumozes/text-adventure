package app;

import attributes.Attribute;
import characters.*;
import characters.Character;
import statuses.Status;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Character> friendlyCharacters = new ArrayList<>();
        List<Character> enemyCharacters = new ArrayList<>();
        Character player = new Barbarian();
        Character wolf = new Wolf();
        friendlyCharacters.add(player);
        enemyCharacters.add(wolf);
        System.out.println("\t Every good story begins with a traveler, who is lost in the woods and have to \n" +
                "\t climb the ladder of the hierarchy from the very base of it. Should we even start telling \n" +
                "\t this tale, or the newcoming hero dies to the first lonely wolf in the deep of the woods? \n" +
                "\t Let's find out!\n");
        int turnCounter = 0;

        progressThroughBattle(friendlyCharacters, enemyCharacters, turnCounter, scanner);
        decideOutcome(friendlyCharacters);
    }


    private static void progressThroughBattle(List<Character> friendlyCharacters, List<Character> enemyCharacters, int turnCounter, Scanner scanner) {
        while (friendlyCharacters.size() > 0 && enemyCharacters.size() > 0) {
            try {
                System.out.println("\n--------------------------------------------------");
                Character player = friendlyCharacters.get(0);
                Character wolf = enemyCharacters.get(0);

                turnCounter++;
                System.out.println("\tTurn " + turnCounter);
                refreshStatuses(player);

                System.out.println("\tYour HP: " + player.getHealth().getValue());
                System.out.println("\tWolf's HP: " + wolf.getHealth().getValue());
                System.out.println("\n\tWhat would you like to do?");
                System.out.println("\t1. Attack");
                System.out.println("\t2. Wait for certain death");
                System.out.println("\t3. Defend");
                System.out.println("\t4. Special attack");

                String input = scanner.nextLine();
                evaluateUserInput(input, player, wolf);

                System.out.println("\n\tThe wolf attacked you!");
                wolf.attack(player);
            } catch (CharacterDiedException characterDeath) {
                if (!enemyCharacters.remove(characterDeath.getCharacter())) {
                    friendlyCharacters.remove(characterDeath.getCharacter());
                }
            }
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

    private static void nullifyStatusEffect(Status status, Character player) {
        for (Attribute attribute : player.getAttributes()) {
            if (attribute.getName().equals(status.getAttribute().getName())) {
                attribute.decrease(status.getValue());
            }
        }
    }

    private static void decideOutcome(List<Character> party) {
        if (party.size() > 0) {
            System.out.println("\n\tCongratulations you've won! You may begin your journey!");
        } else {
            System.out.println("\n\tYou are dead.");
        }
    }
}

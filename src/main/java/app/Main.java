package app;

import attributes.Attribute;
import characters.*;
import characters.Character;
import statuses.Status;

import java.util.Iterator;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Character player = new Player();
        Character wolf = new Wolf();
        System.out.println("\t Every good story begins with a traveler, who is lost in the woods and have to \n" +
                "\t climb the ladder of the hierarchy from the very base of it. Should we even start telling \n" +
                "\t this tale, or the newcoming hero dies to the first lonely wolf in the deep of the woods? \n" +
                "\t Let's find out!\n");
        int counter = 0;
        while (wolf.isAlive() && player.isAlive()) {
            System.out.println("\n--------------------------------------------------");

            refreshStatuses(player);
            counter++;

            System.out.println("\t Turn " + counter);
            System.out.println("\t Your HP: " + player.getHealth().getValue());
            System.out.println("\t Wolf's HP: " + wolf.getHealth().getValue());
            System.out.println("\n\t What would you like to do?");
            System.out.println("\t 1. Attack");
            System.out.println("\t 2. Wait for certain death");
            System.out.println("\t 3. Defend");

            String input = scanner.nextLine();
            evaluateUserInput(input, player, wolf);

            System.out.println("\n\tThe wolf attacked you!");
            wolf.attack(player);
        }
        decideOutcome(player);
    }

    private static void evaluateUserInput(String input, Character player, Character wolf) {
        if (input.equals("1")) {
            System.out.println("\n\tYou decided to attack.");
            player.attack(wolf);
        } else if (input.equals("3")) {
            System.out.println("\tYou decided to defend.");
            player.defend();
        } else {
            System.out.println("\tYou longed for death");
            player.wait(player);
        }
    }

    private static void refreshStatuses(Character player) {
        Iterator<Status> iterator = player.getStatuses().iterator();
        while (iterator.hasNext()) {
            Status current = iterator.next();
            current.setDuration(current.getDuration() - 1);
            if (current.getDuration() <= 0) {
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

    private static void decideOutcome(Character player) {
        if (player.isAlive()) {
            System.out.println("\tCongratulations you've won! You may begin your journey!");
        } else {
            System.out.println("\tYou are dead.");
        }
    }
}

package app;

import characters.*;
import characters.Character;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Character player = new Player();
        Character wolf = new Wolf();
        player.attack(wolf);
        System.out.println("\t Every good story begins with a traveler, who is lost in the woods and have to \n" +
                "\t climb the ladder of the hierarchy from the very base of it. Should we even start telling \n" +
                "\t this tale, or the newcoming hero dies to the first lonely wolf in the deep of the woods? \n" +
                "\t Let's find out!\n");
        while (wolf.isAlive() && player.isAlive()) {
            System.out.println("\t Your HP: " + player.getHealth());
            System.out.println("\n\t What would you like to do?");
            System.out.println("\t 1. Attack");
            System.out.println("\t 2. Wait for certain death");

            String input = scanner.nextLine();
            if (input.equals("1")) {
                player.attack(wolf);
                System.out.println("\tYou decided to attack.");
            } else {
                System.out.println("\tYou longed for death");
                player.wait(player);
            }

            System.out.println("\tThe wolf attacked you!\n");
            wolf.attack(player);
        }
        if (player.isAlive()) {
            System.out.println("\tCongratulations you've won! You may begin your journey!");
        } else {
            System.out.println("\tYour dead.");
        }
    }
}

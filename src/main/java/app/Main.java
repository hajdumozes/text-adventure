package app;

import characters.*;
import characters.Character;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static app.Combat.*;

public class Main {
    public static final List<Character> FRIENDLY_PARTY = new ArrayList<>();
    public static final List<Character> HOSTILE_PARTY = new ArrayList<>();
    public static final Scanner CONSOLE = new Scanner(System.in);

    public static void main(String[] args) {
        chooseClass();
        Character wolf = new Wolf();
        HOSTILE_PARTY.add(wolf);
        System.out.println("\t Every good story begins with a traveler, who is lost in the woods and have to \n" +
                "\t climb the ladder of the hierarchy from the very base of it. Should we even start telling \n" +
                "\t this tale, or the newcoming hero dies to the first lonely wolf in the deep of the woods? \n" +
                "\t Let's find out!\n");

        progressThroughBattle();
        decideOutcome();
    }

    private static void decideOutcome() {
        if (FRIENDLY_PARTY.size() > 0) {
            System.out.println("\n\tCongratulations you've won! You may begin your journey!");
        } else {
            System.out.println("\n\tYou are dead.");
        }
    }

    private static void chooseClass() {
        System.out.println("\n\tChoose your starting class:");
        System.out.println("\t1. Barbarian");
        System.out.println("\t2. Paladin");
        String input = CONSOLE.nextLine();
        switch (input) {
            case "1":
                FRIENDLY_PARTY.add(new Barbarian());
                break;
            case "2":
                FRIENDLY_PARTY.add(new Paladin());
                break;
        }
    }
}

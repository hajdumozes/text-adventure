package app;

import characters.*;
import characters.Character;
import items.Equipment.Equipment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static app.Combat.*;

public class Main {
    public static final List<Character> FRIENDLY_PARTY = new ArrayList<>();
    public static final List<Character> HOSTILE_PARTY = new ArrayList<>();
    public static final List<? extends Playable> PLAYABLE_CLASSES = new ArrayList<>(Arrays.asList(new Barbarian(), new Paladin()));
    public static final Scanner CONSOLE = new Scanner(System.in);

    public static void main(String[] args) {
        chooseClass();
        chooseEquipment();
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
        System.out.println("\n\tChoose your starting class:\n");
        for (int i = 1; i <= PLAYABLE_CLASSES.size(); i++) {
            System.out.println("\t" + i + ". " + ((Character) PLAYABLE_CLASSES.get(i - 1)).getName());
        }
        String input = CONSOLE.nextLine();
        Character character = (Character) PLAYABLE_CLASSES.get(Integer.parseInt(input) - 1);
        FRIENDLY_PARTY.add(character);
    }

    private static void chooseEquipment() {
        System.out.println("\n\tChoose your starting equipment:\n");
        Playable chosenCharacter = (Playable) FRIENDLY_PARTY.get(0);
        for (int i = 1; i <= chosenCharacter.showStartingEquipments().size(); i++) {
            System.out.println("\t" + i + ". " + chosenCharacter.showStartingEquipments().get(i - 1).toString());
        }
        String input = CONSOLE.nextLine();
        Equipment equipment = chosenCharacter.showStartingEquipments().get((Integer.parseInt(input) - 1));
        ((Character) chosenCharacter).setEquipment(equipment);
    }
}

package app;

import characters.*;
import characters.Character;
import items.Equipment.Equipment;

import java.text.MessageFormat;
import java.util.*;

import static app.Combat.*;

public class Main {
    public static final List<Character> CHARACTERS_ALIVE = new ArrayList<>();
    public static final List<? extends Playable> PLAYABLE_CLASSES = new ArrayList<>(Arrays.asList(new Barbarian(), new Paladin()));
    public static final Scanner CONSOLE = new Scanner(System.in);

    public static void main(String[] args) {
        chooseClass();
        chooseEquipment();
        rollAbility();
        Character wolf = new Wolf();
        CHARACTERS_ALIVE.add(wolf);
        System.out.println("\t Every good story begins with a traveler, who is lost in the woods and have to \n" +
                "\t climb the ladder of the hierarchy from the very base of it. Should we even start telling \n" +
                "\t this tale, or the newcoming hero dies to the first lonely wolf in the deep of the woods? \n" +
                "\t Let's find out!\n");

        progressThroughBattle();
    }

    public static void decideOutcome() {
        if (CHARACTERS_ALIVE.get(0).isFriendly()) {
            System.out.println("\n\tCongratulations you've won! You may begin your journey!");
        } else {
            System.out.println("\n\tYou are dead.");
        }
    }

    private static void chooseClass() {
        System.out.println("\n\tChoose your starting class:\n");
        for (int i = 1; i <= PLAYABLE_CLASSES.size(); i++) {
            System.out.println(MessageFormat.format("\t {0}. {1}", i,
                    ((Character) PLAYABLE_CLASSES.get(i - 1)).getName()));
        }
        String input = CONSOLE.nextLine();
        Character character = (Character) PLAYABLE_CLASSES.get(Integer.parseInt(input) - 1);
        CHARACTERS_ALIVE.add(character);
        System.out.println(MessageFormat.format("\n\tYou selected {0}.", character.getName()));
    }

    public static int rollAbility() {
        List<Integer> rolls = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            rolls.add(roll(1, 6));
        }
        rolls.sort(Comparator.comparing(Integer::intValue));
        rolls.remove(0);
        int abilityPoint = rolls.stream().mapToInt(Integer::intValue).sum();
        CHARACTERS_ALIVE.get(0).getDexterity().setValue(abilityPoint);
        return abilityPoint;
        // With more ability the return will have an impact. Player will roll 6times and choose which points go where.
    }

    private static void chooseEquipment() {
        System.out.println("\n\tChoose your starting equipment:\n");
        Playable chosenCharacter = (Playable) CHARACTERS_ALIVE.get(0);
        for (int i = 1; i <= chosenCharacter.showStartingEquipments().size(); i++) {
            System.out.println(MessageFormat.format(
                    "\t{0}. {1}", i, chosenCharacter.showStartingEquipments().get(i-1).toString()));
        }
        String input = CONSOLE.nextLine();
        Equipment equipment = chosenCharacter.showStartingEquipments().get((Integer.parseInt(input) - 1));
        ((Character) chosenCharacter).setEquipment(equipment);
    }

    public static int roll(int numberOfDices, int maxNumber) {
        return numberOfDices * new Random().nextInt(maxNumber) + 1;
    }
}

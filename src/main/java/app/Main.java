package app;

import app.actions.Combat;
import app.battlefield.Battlefield;
import characters.Character;
import characters.animals.Wolf;
import characters.playable.*;
import combat.WinCondition;
import items.Equipment.Equipment;

import java.text.MessageFormat;
import java.util.*;

public class Main {
    public static final List<Character> CHARACTERS_ALIVE = new ArrayList<>();
    public static final List<Character> CHARACTERS_DEAD = new ArrayList<>();
    public static final List<Character> SUMMONABLE_CHARACTERS = new ArrayList<>();
    public static final List<? extends Playable> PLAYABLE_CLASSES =
            new ArrayList<>(Arrays.asList(new Barbarian(), new Paladin(), new Hunter(), new Wizard()));
    public static final Scanner CONSOLE = new Scanner(System.in);
    public static final String CONSOLE_SEPARATOR = "______________________________________________________" +
            "______________________________________________________";

    public static void main(String[] args) {
        Main main = new Main();
        String name = main.chooseName();
        main.chooseClass(name);
        main.chooseEquipment();
        main.rollAbility();
        for (int i = 0; i < 4; i++) {
            CHARACTERS_ALIVE.add(new Wolf());
        }
        System.out.println(CONSOLE_SEPARATOR);
        System.out.println("\n\n\tForget stories. Face the facts. This is a test. A test of endurance.\n" +
                "\tYou are not alone, though. You will face the arena with 3 party members:\n\n" +
                "\tZephyros as a wizard doesn't belong to the front lines, and use high risk - high reward attacks.\n" +
                "\tAdalbert is your friendly paladin neighbour. You know, healing, disabling and stuff.\n" +
                "\tFríða uses sneaky hunter tactics and turn nature against the foes.\n\n " +
                "\tAre you ready? The first challenge awaits you. The wolves. Kill them all, or kill 10 to make them run.\n");
        CHARACTERS_ALIVE.add(new Wizard("Zephyros"));
        CHARACTERS_ALIVE.add(new Paladin("Adalbert"));
        CHARACTERS_ALIVE.add(new Hunter("Fríða"));
        new Battlefield().emptyBattlefield();
        new Battlefield().putRandomTreesToBattlefield();
        new Combat().progressThroughBattle(new WinCondition(10, Wolf.class, false, true));
    }

    private String chooseName() {
        System.out.println("\n\tChoose your character's name:");
        String input = CONSOLE.nextLine().trim();
        return input;
    }

    private void chooseClass(String name) {
        try {
            System.out.println("\n\tChoose your starting class:\n");
            for (int i = 1; i <= PLAYABLE_CLASSES.size(); i++) {
                System.out.println(MessageFormat.format("\t {0}. {1}", i,
                        ((Character) PLAYABLE_CLASSES.get(i - 1)).getClassName()));
            }
            String input = CONSOLE.nextLine().trim();
            Character character = (Character) PLAYABLE_CLASSES.get(Integer.parseInt(input) - 1);
            CHARACTERS_ALIVE.add(character);
            character.setName(name);
            System.out.println(MessageFormat.format("\n\tYou selected {0}.", character.getClassName()));
        } catch (NumberFormatException | IndexOutOfBoundsException invalidClass) {
            System.out.println("\tInvalid input. Try again.");
            chooseClass(name);
        }
    }

    public int rollAbility() {
        List<Integer> rolls = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            rolls.add(roll(1, 6));
        }
        rolls.sort(Comparator.comparing(Integer::intValue));
        rolls.remove(0);
        int abilityPoint = rolls.stream().mapToInt(Integer::intValue).sum();
        CHARACTERS_ALIVE.get(0).getDexterity().setCurrentValue(abilityPoint);
        return abilityPoint;
        // With more ability the return will have an impact. Player will roll 6 times and choose which points go where.
    }

    private void chooseEquipment() {
        System.out.println(CONSOLE_SEPARATOR);
        try {
            System.out.println("\n\tChoose your starting equipment:\n");
            Playable chosenCharacter = (Playable) CHARACTERS_ALIVE.get(0);
            for (int i = 1; i <= chosenCharacter.showStartingEquipments().size(); i++) {
                System.out.println(MessageFormat.format(
                        "\t{0}. {1}", i, chosenCharacter.showStartingEquipments().get(i - 1).toString()));
            }
            String input = CONSOLE.nextLine().trim();
            Equipment equipment = chosenCharacter.showStartingEquipments().get((Integer.parseInt(input) - 1));
            ((Character) chosenCharacter).setEquipment(equipment);
        } catch (NumberFormatException | IndexOutOfBoundsException invalidClass) {
            System.out.println("\tInvalid input. Try again.");
            chooseEquipment();
        }
    }

    public int roll(int numberOfDices, int maxNumber) {
        return numberOfDices * new Random().nextInt(maxNumber) + 1;
    }
}

package app;

import characters.Character;
import combat.UnreachablePositionException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static app.AttackEvaluation.*;
import static app.Battlefield.getMovementDestinationFromUser;
import static app.Main.*;
import static app.SkillManagement.*;

public class Combat {

    protected static void progressThroughBattle() {
        int turnCounter = 0;
        rollInitiativeForAllCharacters();
        while (getAliveCharactersFromBothSides()) {
            turnCounter++;
            System.out.println(MessageFormat.format("\n\n\t\t\t\t\t\t\t\t\t\t\t\tTURN {0}\n\n", turnCounter));
            refreshStatuses();
            refreshSkillCountdowns();
            refreshMovementAvailability();
            progressThroughTurnsOfAliveCharacters();
        }
    }

    private static void rollInitiativeForAllCharacters() {
        for (Character character : CHARACTERS_ALIVE) {
            rollInitiative(character);
        }
    }

    public static void rollInitiative(Character character) {
        int initiative = roll(1, 20) + (character.getDexterityValue() / 2);
        character.getInitiative().setCurrentValue(initiative);
    }

    private static void printInfoOfAliveCharacters() {
        for (Character character : CHARACTERS_ALIVE) {
            System.out.println(MessageFormat.format(
                    "\t{0}''s HP: {1}", character.getName(), character.getHealthCurrentValue()));
        }
    }

    private static void progressThroughTurnsOfAliveCharacters() {
        CHARACTERS_ALIVE.sort(Comparator.comparing(Character::getInitiative));
        for (Character character : new ArrayList<>(CHARACTERS_ALIVE)) {
            if (character.isAlive() && character.isFriendly()) {
                progressThroughTurnOfFriendlyCharacter(character);
            } else if (character.isAlive() && !character.isFriendly()) {
                System.out.println(CONSOLE_SEPARATOR);
                System.out.println(MessageFormat.format("\n\t{0}''s turn:", character.getName()));
                character.letAiDecide();
            }
        }
    }

    protected static void progressThroughTurnOfFriendlyCharacter(Character character) {
        Battlefield.showBattlefield();
        printInfoOfAliveCharacters();
        printOptionsForCurrentFriendlyCharacter(character);
    }

    protected static void printOptionsForCurrentFriendlyCharacter(Character character) {
        System.out.println(CONSOLE_SEPARATOR);
        try {
            System.out.println(MessageFormat.format("\n\t{0}''s turn:", character.getName()));
            System.out.println("\n\tWhat would you like to do?");
            System.out.println("\t1. Attack");
            System.out.println("\t2. Move - free action once per turn");
            System.out.println("\t3. Defend - increase AC by 5 for 1 turn");
            System.out.println("\t4. Skills");
            System.out.println("\t5. Inspect character");

            String input = CONSOLE.nextLine().trim();
            evaluateUserInput(input, character);
        } catch (NumberFormatException | IndexOutOfBoundsException mistype) {
            System.out.println("\tUnsupported actions count as ESC.");
            printOptionsForCurrentFriendlyCharacter(character);
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
                evaluateCharacterAttack(character);
                break;
            case "2":
                evaluateCharacterMovement(character);
                break;
            case "3":
                character.defend();
                break;
            case "4":
                evaluateCharacterSkill(character);
                break;
            case "5":
                inspectCharacter(character);
                break;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    private static void inspectCharacter(Character turnOfCharacter) {
        List<Character> allCharacters = getCharactersFromSelectedSide(true);
        allCharacters.addAll(getCharactersFromSelectedSide(false));
        Character chosenCharacter = chooseTargetFromCharacters(allCharacters);
        System.out.println(MessageFormat.format("\tName: {0}\n\tHealth: {1}/{2}\n\tInitiative: {3}\n\tDexterity: {4}" +
                        "\n\tArmor Class: {5}\n\tDamage Bonus: {6}",
                chosenCharacter.getName(), chosenCharacter.getHealthCurrentValue(), chosenCharacter.getHealthMaxValue(),
                chosenCharacter.getInitiativeValue(), chosenCharacter.getDexterityValue(),
                chosenCharacter.getArmorClassValue(), chosenCharacter.getDamageBonusValue()));
        printOptionsForCurrentFriendlyCharacter(turnOfCharacter);
    }

    private static void evaluateCharacterMovement(Character character) {
        try {
            character.move(getMovementDestinationFromUser(character));
            if (!character.isMovedThisTurn()) {
                character.setMovedThisTurn(true);
                System.out.println(MessageFormat.format("\t{0} used up free movement of the turn.", character.getName()));
                progressThroughTurnOfFriendlyCharacter(character);
            }
        } catch (UnreachablePositionException impossibleMovement) {
            System.out.println("\t" + impossibleMovement.getMessage());
            evaluateCharacterMovement(character);
        }
    }

    private static void refreshMovementAvailability() {
        for (Character character : new ArrayList<>(CHARACTERS_ALIVE)) {
            character.setMovedThisTurn(false);
        }
    }
}

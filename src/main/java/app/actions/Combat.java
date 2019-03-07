package app.actions;

import app.Main;
import app.battlefield.Battlefield;
import app.battlefield.Movement;
import app.battlefield.Positioning;
import characters.Character;
import combat.WinCondition;
import combat.exceptions.BattleIsOver;
import combat.exceptions.CantFinishAction;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static app.Main.*;

public class Combat extends Movement {

    public void progressThroughBattle(WinCondition... winConditions) {
        new Positioning().positionCharacters(CHARACTERS_ALIVE);
        try {
            int turnCounter = 0;
            rollInitiativeForAllCharacters();
            while (areAliveCharactersOnBothSides() && areAllFalse(winConditions)) {
                turnCounter++;
                System.out.println(MessageFormat.format("\n\n\t\t\t\t\t\t\t\t\t\t\t\tTURN {0}\n\n", turnCounter));
                SkillManagement skillManagement = new SkillManagement();
                skillManagement.refreshEffects();
                skillManagement.refreshSkillCountdowns();
                new Movement().refreshMovementAvailability();
                progressThroughTurnsOfAliveCharacters();
            }
            decideOutcome(winConditions);
        } catch (BattleIsOver battleIsOver) {
            decideOutcome(winConditions);
        }
    }

    public void decideOutcome(WinCondition... winConditions) {
        boolean overByWinCondition = false;
        boolean win = false;
        for (WinCondition winCondition : winConditions) {
            if (winCondition.determine()) {
                overByWinCondition = true;
                win = winCondition.WinIfHappens();
            }
        }
        if (!overByWinCondition) {
            win = CHARACTERS_ALIVE.get(0).isFriendly();
        }
        String output = win ? "\n\tCongratulations, you cleared the fist stage!" :
                "\n\tYou lost.";
        System.out.println(output);
    }

    private boolean areAllFalse(WinCondition[] winConditions) {
        for (WinCondition condition : winConditions) {
            if (condition.determine()) {
                return false;
            }
        }
        return true;
    }

    private void rollInitiativeForAllCharacters() {
        for (Character character : CHARACTERS_ALIVE) {
            rollInitiative(character);
        }
    }

    public void rollInitiative(Character character) {
        int initiative = new Main().roll(1, 20) + (character.getDexterityValue() / 2);
        character.getInitiative().setCurrentValue(initiative);
    }

    private void printInfoOfAliveCharacters() {
        for (Character character : CHARACTERS_ALIVE) {
            System.out.println(MessageFormat.format(
                    "\t{0}''s HP: {1}", character.getName(), character.getHealthCurrentValue()));
        }
    }

    private void progressThroughTurnsOfAliveCharacters() {
        CHARACTERS_ALIVE.sort(Comparator.comparing(Character::getInitiative));
        for (Character character : new ArrayList<>(CHARACTERS_ALIVE)) {
            try {
                if (!character.isStunned()) {
                    if (character.isAlive() && character.isFriendly()) {
                        progressThroughTurnOfFriendlyCharacter(character);
                    } else if (character.isAlive() && !character.isFriendly()) {
                        System.out.println(CONSOLE_SEPARATOR);
                        System.out.println(MessageFormat.format("\n\t{0}''s turn:", character.getName()));
                        character.letAiDecide();
                    }
                } else {
                    System.out.println(MessageFormat.format("\t{0} is stunned.", character.getName()));
                }
            } catch (CantFinishAction unfinishedAction) {
                System.out.println(MessageFormat.format("\t{0} couldn''t finish it''s turn {1}",
                        character, unfinishedAction.getMessage()));
            }
        }
    }

    public void progressThroughTurnOfFriendlyCharacter(Character character) {
        new Battlefield().showBattlefield();
        printInfoOfAliveCharacters();
        printOptionsForCurrentFriendlyCharacter(character);
    }

    protected void printOptionsForCurrentFriendlyCharacter(Character character) {
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

    protected boolean areAliveCharactersOnBothSides() {
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

    public List<Character> getDeadCharactersBySide(boolean playersSide) {
        List<Character> deadCharacters = new ArrayList<>();
        for (Character character : CHARACTERS_DEAD) {
            if (character.isFriendly() == playersSide) {
                deadCharacters.add(character);
            }
        }
        return deadCharacters;
    }

    private void evaluateUserInput(String input, Character character) {
        switch (input) {
            case "1":
                new AttackEvaluation().evaluateCharacterAttack(character);
                break;
            case "2":
                evaluateCharacterMovement(character);
                break;
            case "3":
                new CharacterActions().defend(character);
                break;
            case "4":
                new SkillManagement().evaluateCharacterSkill(character);
                break;
            case "5":
                new CharacterActions().inspectCharacter(character);
                break;
            default:
                throw new IndexOutOfBoundsException();
        }
    }
}

package app;

import characters.Character;
import combat.NoTargetException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static app.Battlefield.countPositionDifference;
import static app.Combat.printOptionsForCurrentFriendlyCharacter;
import static app.Main.CHARACTERS_ALIVE;
import static app.Main.CONSOLE;

public class AttackEvaluation {

    protected static void evaluateCharacterAttack(Character character) {
        try {
            character.attack(chooseTargetFromCharacters(filterReachableCharacters
                    (character, getCharactersFromSelectedSide(false), character.getWeaponReach())));
        } catch (NoTargetException targetException) {
            System.out.println(MessageFormat.format("\t{0}. Press Enter to get back.", targetException.getMessage()));
            CONSOLE.nextLine();
            printOptionsForCurrentFriendlyCharacter(character);
        }
    }

    protected static List<Character> filterReachableCharacters(Character thisCharacter, List<Character> possibleTargets, int reach) {
        List<Character> reachableCharacters = new ArrayList<>();
        for (Character target : possibleTargets) {
            if (countPositionDifference(thisCharacter.getPosition(), target.getPosition()) <= reach) {
                reachableCharacters.add(target);
            }
        }
        if (reachableCharacters.size() > 0) {
            return reachableCharacters;
        } else {
            throw new NoTargetException("No target is within reach");
        }
    }

    protected static List<Character> getCharactersFromSelectedSide(boolean isTargetOnPlayersSide) {
        return findPossibleTargets(isTargetOnPlayersSide);
    }

    protected static Character chooseTargetFromCharacters(List<Character> possibleTargets) {
        System.out.println("\n\tSelect your target:");
        for (int i = 1; i <= possibleTargets.size(); i++) {
            System.out.println(MessageFormat.format("\t{0}. {1} - {2} HP",
                    i, possibleTargets.get(i - 1).getName(), possibleTargets.get(i - 1).getHealth().getCurrentValue()));
        }
        String input = CONSOLE.nextLine().trim();
        return possibleTargets.get((Integer.parseInt(input) - 1));
    }

    public static List<Character> findPossibleTargets(boolean isTargetOnPlayersSide) {
        if (isTargetOnPlayersSide) {
            return CHARACTERS_ALIVE.stream().filter(character -> character.isFriendly()).collect(Collectors.toList());
        } else {
            return CHARACTERS_ALIVE.stream().filter(character -> !character.isFriendly()).collect(Collectors.toList());
        }
    }
}

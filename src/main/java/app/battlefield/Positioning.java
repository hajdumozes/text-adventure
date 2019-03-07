package app.battlefield;

import app.actions.Combat;
import characters.Character;
import combat.Position;
import combat.exceptions.UnreachablePositionException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static app.battlefield.Battlefield.BATTLEFIELD;

public class Positioning {
    public void positionCharacters(List<Character> characters) {
        List<Character> friendlyCharacters = new ArrayList<>();
        System.out.println("\tBefore initiating the battle you must position you team. Give coordinates according" +
                " to the following sample: 'A3'");
        for (Character character : characters) {
            if (character.isFriendly()) {
                friendlyCharacters.add(character);
            } else {
                positionHostileCharacter(character);
            }
        }
        for (Character friendlyCharacter : friendlyCharacters) {
            new Battlefield().showBattlefield();
            positionFriendlyCharacter(friendlyCharacter);
        }
    }

    private void positionFriendlyCharacter(Character character) {
        try {
            System.out.println(MessageFormat.format("\tPlace {0} to the following coordinate:", character));
            Position position = new Movement().getPositionFromUser();
            if (position.getColumn() < 3 && new Battlefield().checkIfPositionIsOccupied(position)) {
                character.setPosition(position);
            } else {
                System.out.println("\tIncorrect coordinate. It is occupied already or is out of your arranging field." +
                        "\nBe aware. You may only place characters on column 1, 2 and 3.");
                positionFriendlyCharacter(character);
            }
        } catch (UnreachablePositionException positionError) {
            System.out.println("\t" + positionError.getMessage());
            positionFriendlyCharacter(character);
        }
    }

    public void positionHostileCharacter(Character character) {
        try {
            int row = new Random().nextInt(BATTLEFIELD.length);
            int column = new Random().nextInt(3) + 6;
            Position position = new Position(row, column);
            while (!new Combat().checkIfPositionIsOccupied(position)) {
                positionHostileCharacter(character);
            }
            character.setPosition(position);
        } catch (UnreachablePositionException positionError) {
            positionHostileCharacter(character);
        }
    }
}

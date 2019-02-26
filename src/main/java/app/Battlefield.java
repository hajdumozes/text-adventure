package app;

import characters.Character;
import combat.Position;
import objects.BattlefieldObject;
import objects.EmptySpace;

public class Battlefield {
    public static final BattlefieldObject[][] BATTLEFIELD = new BattlefieldObject[9][9];

    public void showBattlefield() {
        refreshBattlefield();
        System.out.println();
        System.out.print("\t\t  ");
        createRowNumbers();
        printBattlefield();
        System.out.println("\n\n");
    }

    public void refreshBattlefield() {
        emptyBattlefield();
        fillBattlefield();
    }

    public boolean checkIfDestinationIsReacheable(Character character, Position position) {
        int difference = countPositionDifference(character.getPosition(), position);
        return difference <= character.getSpeedValue();
    }

    public int countPositionDifference(Position one, Position other) {
        int oneColumn = one.getColumn();
        int oneRow = one.getRow();
        int otherColumn = other.getColumn();
        int otherRow = other.getRow();
        return Math.abs((oneColumn - otherColumn)) + Math.abs((oneRow - otherRow));
    }

    public void emptyBattlefield() {
        for (int row = 0; row < BATTLEFIELD.length; row++) {
            for (int column = 0; column < BATTLEFIELD[row].length; column++) {
                BATTLEFIELD[row][column] = new EmptySpace();
            }
        }
    }

    public boolean checkIfPositionIsOccupied(Position position) {
        return BATTLEFIELD[position.getRow()][position.getColumn()].getName().trim().equals("x");
    }

    private void fillBattlefield() {
        for (Character character : Main.CHARACTERS_ALIVE) {
            int row = character.getPosition().getRow();
            int column = character.getPosition().getColumn();

            BATTLEFIELD[row][column] = character;
        }
    }

    private void printBattlefield() {
        for (int row = 0; row < BATTLEFIELD.length; row++) {
            char letter = (char) (row + 65);
            System.out.print("\t" + letter + " |\t");
            for (int column = 0; column < BATTLEFIELD[row].length; column++) {
                BattlefieldObject battlefieldObject = BATTLEFIELD[row][column];
                String prefix = createPrefixForExistentObjects(battlefieldObject);
                String padding = createPaddingForName(battlefieldObject.getName());
                System.out.print(prefix + battlefieldObject + padding);
            }
            System.out.println();
        }
    }

    private String createPrefixForExistentObjects(BattlefieldObject battlefieldObject) {
        String prefix = "";
        if (!(battlefieldObject instanceof EmptySpace)) {
            prefix = battlefieldObject.getName().length() > 11 ? "\t" : "\t\t";
        }
        return prefix;
    }

    private String createPaddingForName(String name) {
        StringBuilder padding = new StringBuilder();
        for (int i = 0; i < 10 - name.length(); i++) {
            padding.append(" ");
        }
        return padding.toString();
    }

    private void createRowNumbers() {
        for (int i = 1; i <= BATTLEFIELD[0].length; i++) {
            System.out.print("\t\t " + i + "         ");
        }
        System.out.println("\n\t  __________________________________________________________________________________" +
                "______________________________________________________________");
    }
}

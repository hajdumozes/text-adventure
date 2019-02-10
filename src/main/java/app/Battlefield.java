package app;

import characters.Character;
import combat.Position;

import static app.Main.CONSOLE;

public class Battlefield {
    public static final String[][] BATTLEFIELD = new String[9][9];

    static {
        emptyBattlefield();
    }


    public static void showBattlefield() {
        refreshBattlefield();
        System.out.println();
        System.out.print("\t\t  ");
        createRowNumbers();
        printBattlefield();
        System.out.println("\n\n");
    }

    public static void refreshBattlefield() {
        emptyBattlefield();
        fillBattlefield();
    }

    protected static Position getMovementDestinationFromUser() {
        System.out.println("\t Give destination in the following pattern: A3");
        String input = CONSOLE.nextLine();
        int row = input.charAt(0) - 65;
        int column = Integer.parseInt(input.substring(1, 2)) - 1;
        return new Position(row, column);
    }

    public static boolean checkIfDestinationIsReacheable(Character character, Position position) {
        int difference = countPositionDifference(character.getPosition(), position);
        return difference <= character.getSpeedValue();
    }

    public static int countPositionDifference(Position one, Position other) {
        int oneColumn = one.getColumn();
        int oneRow = one.getRow();
        int otherColumn = other.getColumn();
        int otherRow = other.getRow();
        return Math.abs((oneColumn - otherColumn)) + Math.abs((oneRow - otherRow));
    }

    private static void emptyBattlefield() {
        for (int row = 0; row < BATTLEFIELD.length; row++) {
            for (int column = 0; column < BATTLEFIELD[row].length; column++) {
                BATTLEFIELD[row][column] = "  x ";
            }
        }
    }

    public static boolean checkIfPositionIsOccupied(Position position) {
        return BATTLEFIELD[position.getRow()][position.getColumn()].trim().equals("x");
    }

    private static void fillBattlefield() {
        for (Character character : Main.CHARACTERS_ALIVE) {
            int row = character.getPosition().getRow();
            int column = character.getPosition().getColumn();

            BATTLEFIELD[row][column] = character.getGivenName();
        }
    }

    private static void printBattlefield() {
        for (int row = 0; row < BATTLEFIELD.length; row++) {
            char letter = (char) (row + 65);
            System.out.print("\t" + letter + " |\t");
            for (int column = 0; column < BATTLEFIELD[row].length; column++) {
                String name = BATTLEFIELD[row][column];
                String padding = createPaddingForName(name);
                System.out.print(name + padding);
            }
            System.out.println();
        }
    }

    private static String createPaddingForName(String name) {
        StringBuilder padding = new StringBuilder();
        for (int i = 0; i < 10 - name.length(); i++) {
            padding.append(" ");
        }
        return padding.toString();
    }

    private static void createRowNumbers() {
        for (int i = 1; i <= BATTLEFIELD[0].length; i++) {
            System.out.print(i + "         ");
        }
        System.out.println("\n\t  _______________________________________________________________________________________");
    }
}

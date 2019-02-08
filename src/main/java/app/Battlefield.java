package app;

import characters.Character;
import combat.Position;

public class Battlefield {
    public static final String[][] BATTLEFIELD = new String[9][9];


    public static void showBattlefield() {
        emptyBattlefield();
        fillBattlefield();
        System.out.println();
        System.out.print("\t\t  ");
        createRowNumbers();
        printBattlefield();
        System.out.println("\n\n");
    }

    protected static void setCharacterPositions() {
        Main.CHARACTERS_ALIVE.get(0).setPosition(new Position(3, 3));
        Main.CHARACTERS_ALIVE.get(1).setPosition(new Position(5, 6));
    }

    private static void emptyBattlefield() {
        for (int row = 0; row < BATTLEFIELD.length; row++) {
            for (int column = 0; column < BATTLEFIELD[row].length; column++) {
                BATTLEFIELD[row][column] = "  x ";
            }
        }
    }

    private static void fillBattlefield() {
        for (Character character : Main.CHARACTERS_ALIVE) {
            int row = character.getPosition().getRow();
            int column = character.getPosition().getColumn();
            BATTLEFIELD[row][column] = character.getName();
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

package app;

public class Battlefield {
    public static final String[][] BATTLEFIELD = new String[10][15];


    public static void showBattlefield() {
        fillBattlefield();
        System.out.println();
        System.out.print("\t\t  ");
        createRowNumbers();

        printBattlefield();
    }

    private static void fillBattlefield() {
        for (int row = 0; row < BATTLEFIELD.length; row++) {
            for (int column = 0; column < BATTLEFIELD[row].length; column++) {
                BATTLEFIELD[row][column] = "Empty";
            }
        }
        BATTLEFIELD[4][0] = "Player";
        BATTLEFIELD[6][13] = "Wolf";
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
            if (i < 10) {
                System.out.print(i + "         ");
            } else {
                System.out.print(i + "        ");
            }
        }
        System.out.println("\n\t  ____________________________________________________________________________________________" +
                "_________________________________________________________");
    }
}

package app;

import characters.Character;
import combat.Position;
import combat.UnreachablePositionException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static app.Main.CHARACTERS_ALIVE;
import static app.Main.CONSOLE;

public class Movement extends Battlefield {

    protected void evaluateCharacterMovement(Character character) {
        try {
            move(character, getMovementDestinationFromUser(character));
            if (!character.hasMovedThisTurn()) {
                character.modifyStatus("MovedThisTurn", true);
                System.out.println(MessageFormat.format("\t{0} used up free movement of the turn.",
                        character.getName()));
                new Combat().progressThroughTurnOfFriendlyCharacter(character);
            }
        } catch (UnreachablePositionException impossibleMovement) {
            System.out.println("\t" + impossibleMovement.getMessage());
            evaluateCharacterMovement(character);
        }
    }

    protected void move(Character character, Position position) {
        if (checkIfDestinationIsReacheable(character, position)
                && checkIfPositionIsOccupied(position)) {
            System.out.println(MessageFormat.format("\t{0} moved from {1} to {2}",
                    character.getName(), character.getPosition(), position));
            character.setPosition(position);
            refreshBattlefield();
        } else {
            throw new UnreachablePositionException("Destination is too far away");
        }
    }

    protected Position getMovementDestinationFromUser(Character character) {
        System.out.println("\tGive destination in the following pattern: A3");
        printReachableRoutes(character);
        String input = CONSOLE.nextLine().trim().toUpperCase();
        int row = input.charAt(0) - 65;
        int column = Integer.parseInt(input.substring(1, 2)) - 1;
        return new Position(row, column);
    }

    private void printReachableRoutes(Character character) {
        List<Position> allRoutes = findAllPossibleRoutes(character);
        StringBuilder output = new StringBuilder("\n\tAvailable positions:");
        for (Position position : allRoutes) {
            output.append("\t");
            output.append(position);
        }
        System.out.println(output);
    }

    public List<Position> findAllPossibleRoutes(Character character) {
        return findAllPossibleRoutesByGivenDistance(character, character.getSpeedValue());
    }

    public List<Position> findAllPossibleRoutesByGivenDistance(Character character, int distance) {
        List<Position> routes = new ArrayList<>();
        for (int i = 0 - distance; i <= distance; i++) {
            int leftoverSteps = distance - Math.abs(i);
            for (int k = 0 - leftoverSteps; k <= leftoverSteps; k++) {
                int newRow = character.getPosition().getRow() + i;
                int newColumn = character.getPosition().getColumn() + k;
                if (newColumn >= 0 && newColumn < BATTLEFIELD[0].length
                        && newRow >= 0 && newRow < BATTLEFIELD.length
                        && BATTLEFIELD[newRow][newColumn].trim().equals("x")) {
                    routes.add(new Position(character.getPosition().getRow() + i,
                            character.getPosition().getColumn() + k));
                }
            }
        }
        return routes;
    }

    public void refreshMovementAvailability() {
        for (Character character : new ArrayList<>(CHARACTERS_ALIVE)) {
            character.modifyStatus("MovedThisTurn", false);
        }
    }
}

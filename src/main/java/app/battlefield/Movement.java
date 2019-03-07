package app.battlefield;

import app.actions.Combat;
import characters.Character;
import combat.Position;
import combat.exceptions.UnreachablePositionException;
import objects.BattlefieldObject;
import objects.EmptySpace;
import objects.Trap;

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

    public void move(Character character, Position position) {
        if (checkIfDestinationIsReacheable(character, position)
                && checkIfPositionIsOccupied(position)) {
            System.out.println(MessageFormat.format("\t{0} moved from {1} to {2}",
                    character.getName(), character.getPosition(), position));
            Position originalPosition = character.getPosition();
            BATTLEFIELD[originalPosition.getRow()][originalPosition.getColumn()] = new EmptySpace();
            character.setPosition(position);
            triggerTrapIfCurrent(position.getRow(), position.getColumn(), character);
            refreshBattlefield();
        } else {
            throw new UnreachablePositionException("Destination is too far away or occupied.");
        }
    }

    private void triggerTrapIfCurrent(int row, int column, Character movingCharacter) {
        BattlefieldObject object = BATTLEFIELD[row][column];
        if (object instanceof Trap) {
            ((Trap) object).trigger(movingCharacter);
        }
    }

    protected Position getMovementDestinationFromUser(Character character) {
        System.out.println("\tGive destination in the following pattern: A3");
        printReachableRoutes(character);
        return getPositionFromUser();

    }

    public Position getPositionFromUser() {
        try {
            String input = CONSOLE.nextLine().trim().toUpperCase();
            int row = input.charAt(0) - 65;
            int column = Integer.parseInt(input.substring(1, 2)) - 1;
            if (row < 0 || row >= BATTLEFIELD.length || column < 0 || column >= BATTLEFIELD.length) {
                throw new IndexOutOfBoundsException();
            }
            return new Position(row, column);
        } catch (NumberFormatException | IndexOutOfBoundsException incorrectPosition) {
            throw new UnreachablePositionException("Incorrect coordinate format. Please, try again.");
        }
    }

    private void printReachableRoutes(Character character) {
        List<Position> allRoutes = findAllPossibleRoutes(character);
        printPositions(allRoutes);
    }

    public void printPositions(List<Position> positions) {
        StringBuilder output = new StringBuilder("\n\tAvailable coordinates:");
        for (Position position : positions) {
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
        List<Position> allCoordinates = findAllPositionsWithinDistance(character.getPosition(), distance);
        for (Position position : allCoordinates) {
            if (checkIfPositionIsOccupied(position)) {
                routes.add(position);
            }
        }
        return routes;
    }

    public List<Position> findAllPositionsWithinDistance(Position position, int distance) {
        List<Position> routes = new ArrayList<>();
        for (int i = 0 - distance; i <= distance; i++) {
            int leftoverSteps = distance - Math.abs(i);
            for (int k = 0 - leftoverSteps; k <= leftoverSteps; k++) {
                int newRow = position.getRow() + i;
                int newColumn = position.getColumn() + k;
                if (newColumn >= 0 && newColumn < BATTLEFIELD[0].length
                        && newRow >= 0 && newRow < BATTLEFIELD.length) {
                    routes.add(new Position(position.getRow() + i,
                            position.getColumn() + k));
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

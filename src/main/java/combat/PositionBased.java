package combat;

import app.battlefield.Movement;
import characters.Character;

import java.util.List;

public interface PositionBased extends DistanceBased {

    default Position selectPosition(Character character) {
        Movement movement = new Movement();
        List<Position> positions = movement.findAllPositionsWithinDistance(character.getPosition(), getReach());
        System.out.println("\tGive position in the following pattern: A3");
        movement.printPositions(positions);
        return movement.getPositionFromUser();
    }
}

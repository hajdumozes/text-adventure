package characters;

import app.Movement;
import combat.Position;

import java.util.List;
import java.util.Random;

public interface Summonable {
    default void adjustPositionToSummoner(Character summoned, Character summoner) {
        int distance = 1;
        List<Position> positions = new Movement().findAllPossibleRoutesByGivenDistance(summoner, distance);
        while (positions.size() == 0) {
            distance++;
            positions = new Movement().findAllPossibleRoutesByGivenDistance(summoner, distance);
        }
        summoned.setPosition(positions.get(new Random().nextInt(positions.size())));
    }
}

package combat;

public class BattleIsOver extends RuntimeException {
    public BattleIsOver(String message) {
        super(message);
    }
}

package combat.exceptions;

public class CantFinishAction extends RuntimeException {
    public CantFinishAction(String message) {
        super(message);
    }
}

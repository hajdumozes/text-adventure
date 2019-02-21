package combat;

public class OutOfAmmunitionException extends RuntimeException {
    public OutOfAmmunitionException(String message) {
        super(message);
    }
}

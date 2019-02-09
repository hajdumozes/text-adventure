package combat;

public class UnreachablePositionException extends RuntimeException {
    public UnreachablePositionException(String message) {
        super(message);
    }
}

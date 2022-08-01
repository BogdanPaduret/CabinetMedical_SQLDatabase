package exceptions;

public class TooManyResultsException extends IllegalArgumentException {
    public TooManyResultsException(String message) {
        super(message);
    }

    public TooManyResultsException() {
        super("Prea multe rezultate generate.");
    }
}

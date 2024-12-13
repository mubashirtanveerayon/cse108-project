package utils.exceptions;

public class MultipleInitializationException extends RuntimeException {
    public MultipleInitializationException() {
        super("Attempt to re-initialize player database");
    }
}

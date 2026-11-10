package client;

public class InvalidParameters extends RuntimeException {
    public InvalidParameters(String message) {
        super(message);
    }
}

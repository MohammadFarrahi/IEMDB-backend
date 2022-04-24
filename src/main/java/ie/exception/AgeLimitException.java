package ie.exception;

public class AgeLimitException extends CustomException {
    public static final String message = "AgeLimitError";
    public AgeLimitException() {
        super(message);
    }
}
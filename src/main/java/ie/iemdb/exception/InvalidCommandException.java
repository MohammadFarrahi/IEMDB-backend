package ie.iemdb.exception;

public class InvalidCommandException extends CustomException {
    public static final String message = "InvalidCommand";
    public InvalidCommandException() {
        super(message);
    }
}
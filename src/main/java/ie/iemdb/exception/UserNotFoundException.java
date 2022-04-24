package ie.iemdb.exception;

public class UserNotFoundException extends ObjectNotFoundException {
    public static final String message = "UserNotFound";
    public UserNotFoundException() {
        super(message);
    }
}

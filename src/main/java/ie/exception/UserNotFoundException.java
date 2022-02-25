package ie.exception;

public class UserNotFoundException extends CustomException {
    public static final String message = "UserNotFound";
    public UserNotFoundException() {
        super(message);
    }
}

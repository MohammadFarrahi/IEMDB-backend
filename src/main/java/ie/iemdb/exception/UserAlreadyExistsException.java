package ie.iemdb.exception;

public class UserAlreadyExistsException extends CustomException {
    public static final String message = "User Already Exists";

    public UserAlreadyExistsException() {
        super(message);
    }
}



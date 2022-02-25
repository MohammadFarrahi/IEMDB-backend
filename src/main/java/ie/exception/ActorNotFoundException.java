package ie.exception;

public class ActorNotFoundException extends CustomException {
    public static final String message = "ActorNotFound";
    public ActorNotFoundException() {
        super(message);
    }
}
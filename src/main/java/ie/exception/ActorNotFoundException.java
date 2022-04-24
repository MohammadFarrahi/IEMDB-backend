package ie.exception;

public class ActorNotFoundException extends ObjectNotFoundException {
    public static final String message = "ActorNotFound";
    public ActorNotFoundException() {
        super(message);
    }
}
package ie.iemdb.exception;

public class ActorAlreadyExistsException extends CustomException {
    public static final String message = "ActorAlreadyExists";
    public ActorAlreadyExistsException() {
        super(message);
    }
}

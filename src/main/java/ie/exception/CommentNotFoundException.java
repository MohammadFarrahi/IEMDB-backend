package ie.exception;

public class CommentNotFoundException extends ObjectNotFoundException {
    public static final String message = "CommentNotFound";
    public CommentNotFoundException() {
        super(message);
    }
}
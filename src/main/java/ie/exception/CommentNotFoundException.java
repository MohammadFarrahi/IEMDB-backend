package ie.exception;

public class CommentNotFoundException extends CustomException {
    public static final String message = "CommentNotFound";
    public CommentNotFoundException() {
        super(message);
    }
}
package ie.iemdb.exception;

public class MovieNotFoundException extends ObjectNotFoundException {
    public static final String message = "MovieNotFound";
    public MovieNotFoundException() {
        super(message);
    }
}
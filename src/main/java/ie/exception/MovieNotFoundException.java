package ie.exception;

public class MovieNotFoundException extends CustomException {
    public static final String message = "MovieNotFound";
    public MovieNotFoundException() {
        super(message);
    }
}
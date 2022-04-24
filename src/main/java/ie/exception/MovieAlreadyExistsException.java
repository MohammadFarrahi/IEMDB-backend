package ie.exception;

public class MovieAlreadyExistsException extends CustomException {
    public static final String message = "MovieAlreadyExists";
    public MovieAlreadyExistsException() {
        super(message);
    }
}

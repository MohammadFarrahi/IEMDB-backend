package ie.exception;

public class InvalidRateScoreException extends CustomException {
    public static final String message = " InvalidRateScore";
    public InvalidRateScoreException() {
        super(message);
    }
}
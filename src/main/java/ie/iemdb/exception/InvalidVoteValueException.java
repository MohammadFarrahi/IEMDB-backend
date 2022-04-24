package ie.iemdb.exception;

public class InvalidVoteValueException extends CustomException {
    public static final String message = " InvalidVoteValue";
    public InvalidVoteValueException() {
        super(message);
    }
}
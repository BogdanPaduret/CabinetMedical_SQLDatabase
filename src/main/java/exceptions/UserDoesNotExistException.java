package exceptions;

public class UserDoesNotExistException extends AppointmentFailedException {

    public UserDoesNotExistException(String message) {
        super(message);
    }

    public UserDoesNotExistException() {
        super("User not found");
    }
}

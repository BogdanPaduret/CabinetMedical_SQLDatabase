package exceptions;

public class IncorrectDateOrderException extends AppointmentFailedException {

    public IncorrectDateOrderException(String message) {
        super(message);
    }
    public IncorrectDateOrderException() {
        super("Start date should be before end date");
    }

}

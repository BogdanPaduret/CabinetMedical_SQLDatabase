package exceptions;

public class AppointmentDoesNotExistException extends AppointmentFailedException {

    public AppointmentDoesNotExistException(String message) {
        super(message);
    }
    public AppointmentDoesNotExistException() {
        super("Appointment not found");
    }

}

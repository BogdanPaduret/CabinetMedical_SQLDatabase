package exceptions;

public class AppointmentFailedException extends IllegalArgumentException {

    public AppointmentFailedException(String message) {
        super(message);
    }
    public AppointmentFailedException() {
        super("Appointment failed");
    }

}

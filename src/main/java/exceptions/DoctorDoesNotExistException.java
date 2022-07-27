package exceptions;

public class DoctorDoesNotExistException extends AppointmentFailedException {

    public DoctorDoesNotExistException(String message) {
        super(message);
    }
    public DoctorDoesNotExistException() {
        super("Doctor ID not found");
    }

}

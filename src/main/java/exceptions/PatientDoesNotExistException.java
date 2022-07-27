package exceptions;

public class PatientDoesNotExistException extends AppointmentFailedException {

    public PatientDoesNotExistException(String message) {
        super(message);
    }
    public PatientDoesNotExistException() {
        super("Patient ID not found");
    }

}

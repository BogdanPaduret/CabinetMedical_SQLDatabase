package exceptions;

public class PatientDoesNotExistException extends UserDoesNotExistException {

    public PatientDoesNotExistException(String message) {
        super(message);
    }
    public PatientDoesNotExistException() {
        super("Patient not found");
    }

}

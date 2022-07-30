package exceptions;

public class DoctorDoesNotExistException extends UserDoesNotExistException {

    public DoctorDoesNotExistException(String message) {
        super(message);
    }
    public DoctorDoesNotExistException() {
        super("Doctor not found");
    }

}

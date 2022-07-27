package exceptions;

import java.util.NoSuchElementException;

public class NoUserTypeException extends NoSuchElementException {

    public NoUserTypeException(String message) {
        super(message);
    }
    public NoUserTypeException() {
        super("The given user type does not exist");
    }

}

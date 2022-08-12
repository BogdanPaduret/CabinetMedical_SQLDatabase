package exceptions;

public class AppointmentOnHolidayException extends AppointmentFailedException {

    public AppointmentOnHolidayException(String message) {
        super(message);
    }

    public AppointmentOnHolidayException() {
        this("Appointment falls on holiday");
    }
}

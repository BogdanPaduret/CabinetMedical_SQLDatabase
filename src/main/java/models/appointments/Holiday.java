package models.appointments;

import java.time.LocalDateTime;

public class Holiday extends Appointment {

    public Holiday(LocalDateTime startDate, LocalDateTime endDate) {
        this(-1, startDate, endDate);
    }

    public Holiday(int appointmentId, LocalDateTime startDate, LocalDateTime endDate) {
        this(appointmentId, -1, -1, startDate, endDate);
    }

    public Holiday(int appointmentId, int doctorId, int patientId, LocalDateTime startDate, LocalDateTime endDate) {
        super(appointmentId, doctorId, patientId, startDate, endDate);
    }

}

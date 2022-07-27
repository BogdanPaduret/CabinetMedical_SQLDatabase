package helpers;

import models.appointments.Appointment;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class Utils {

    //appointments
    public static String toStringAppointmentDates(Appointment appointment) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm a");

        Duration duration = appointment.getDuration();
        int days = (int) duration.toDays();

        duration = duration.minusDays(days);
        int hours = (int) duration.toHours();

        duration = duration.minusHours(hours);
        int minutes = (int) duration.toMinutes();

        String string = "";

        string += "Starts on " + appointment.getStartDate().format(formatter);
        string += "\nEnds on " + appointment.getEndDate().format(formatter);
        string += "\nDuration:";

        int[] timeUnits = {days, hours, minutes};
        String[] singulars = {"day", "hour", "minute"};
        String[] plurals = {"days", "hours", "minutes"};

        for (int i = 0; i < timeUnits.length; i++) {
            int timeUnit = timeUnits[i];
            String singular = singulars[i];
            String plural = plurals[i];

            if (timeUnit != 0) {
                string += " " + timeUnit;
                if (timeUnit == 1) {
                    string += " " + singular;
                } else {
                    string += " " + plural;
                }
            }
        }

        return string;
    }

}

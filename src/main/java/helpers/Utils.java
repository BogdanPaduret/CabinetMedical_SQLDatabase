package helpers;

import models.appointments.Appointment;
import models.users.Doctor;
import models.users.Patient;
import models.users.Secretary;
import models.users.User;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

import static helpers.Constants.*;

public class Utils {

    //users
    public static User getNewUser(String type, int id, String firstName, String lastName) {
        switch (type) {
            default -> {
                return null;
            }
            case USER_DOCTOR -> {
                return new Doctor(id, firstName, lastName);
            }
            case USER_PATIENT -> {
                return new Patient(id, firstName, lastName);
            }
            case USER_SECRETARY -> {
                return new Secretary(id, firstName, lastName);
            }
        }
    }
    public static User getNewUser(String type, String firstName, String lastName) {
        return getNewUser(type, -1, firstName, lastName);
    }

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

    //query helpers
    public static String querySelect(String tableName) {
        String string = "";

        string += "SELECT *";
        string += "\nFROM " + tableName;

        return string;
    }

}

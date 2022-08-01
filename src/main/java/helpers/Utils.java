package helpers;

import exceptions.NoUserTypeException;
import exceptions.UserDoesNotExistException;
import models.appointments.Appointment;
import models.users.Doctor;
import models.users.Patient;
import models.users.Secretary;
import models.users.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static helpers.Constants.*;

public class Utils {

    //misc
    public static int parseInteger(String string, int valueOnException) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return valueOnException;
        }
    }

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
    private static User getNewUser(String type) {
        return getNewUser(type, "NONAME", "NONAME");
    }
    public static <T extends User> T getCastingUser(String type) {
        boolean exists = false;
        for (String string : USERS_ARRAY) {
            if (string.equals(type)) {
                exists = true;
                break;
            }
        }
        if (exists) {
            return (T) getNewUser(type);
        } else {
            throw new NoUserTypeException();
        }
    }

    //appointments
    public static Appointment getNewAppointment(int doctorId, int patientId, LocalDateTime startDate, LocalDateTime endDate) {
        return null;
    }
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

    //view helpers
    public static Scanner getScanner(String input) {
        if (input.equals("")) {
            return new Scanner(System.in);
        } else {
            return new Scanner(input);
        }
    }

    public static boolean exitAskSave(Scanner scanner) {
        return exit(scanner);
    }
    public static boolean exit(Scanner scanner) {
        System.out.println("""
                Sigur iesiti din aplicatie?
                 - Y / Yes  / y / yes   : pentru a iesi din modul curent.
                 - Q / Quit / q / quit  : pentru a inchide complet programul. Nu se salveaza.
                 - N / No   / n / no    : pentru a ramane in modul curent.
                """);
        char ans = scanner.nextLine().toLowerCase().charAt(0);
        if (ans == 'y') {
            return true;
        }
        if (ans == 'q') {
            System.exit(0);
        }
        return false;
    }

}

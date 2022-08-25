package helpers;

import com.sun.source.tree.Tree;
import exceptions.NoUserTypeException;
import exceptions.UserDoesNotExistException;
import models.appointments.Appointment;
import models.users.Doctor;
import models.users.Patient;
import models.users.Secretary;
import models.users.User;
import repositories.Repository;
import repositories.RepositoryLoad;
import repositories.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    public static boolean typeExists(String type) {
        for (int i = 0; i < USERS_ARRAY.length; i++) {
            if (USERS_ARRAY[i].equals(type)) {
                return true;
            }
        }
        return false;
    }

    //appointments
    public static Appointment getNewAppointment(int appointmentId, int doctorId, int patientId, LocalDateTime startDate, LocalDateTime endDate) {
        return new Appointment(appointmentId, doctorId, patientId, startDate, endDate);
    }
    public static Appointment getNewAppointment(int doctorId, int patientId, LocalDateTime startDate, LocalDateTime endDate) {
        return getNewAppointment(-1, doctorId, patientId, startDate, endDate);
    }

    public static String toStringAppointmentTimeline(Appointment appointment) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm a");

        String string = "";

        string += "Starts on " + appointment.getStartDate().format(formatter);
        string += "\nEnds on " + appointment.getEndDate().format(formatter);

        string += "\n" + toStringAppointmentDuration(appointment);

        return string;
    }
    public static String toStringAppointmentTime(Appointment appointment) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm a");

        String string = "";

        string += appointment.getStartDate().format(formatter);
        string += " - ";
        string += appointment.getEndDate().format(formatter);

        return string;
    }

    public static String toStringAppointmentDate(Appointment appointment) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy");

        String string = "";

        string += appointment.getStartDate().format(formatter);

        return string;
    }

    public static String toStringAppointmentDuration(Appointment appointment) {
        Duration duration = appointment.getDuration();
        int days = (int) duration.toDays();

        duration = duration.minusDays(days);
        int hours = (int) duration.toHours();

        duration = duration.minusHours(hours);
        int minutes = (int) duration.toMinutes();

        String string = "";
        string += "Duration:";

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

    public static String toSQLDateTimeString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }

    public static String toSQLDateString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
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

    //test
    public static List<User> generateRandomUsers(int howMany, boolean withId) {
        String[] types = USERS_ARRAY;
        String[] firstNames = {
                "Andrei",
                "Bogdan",
                "Dumitru",
                "Vlad",
                "Costel",
                "Vasile",
                "Ana",
                "Dana",
                "Victor",
                "Maria",
                "Bianca",
                "Georgeta",
                "Georgiana",
                "Victoria",
                "Angela",
                "Cristian"
        };
        String[] lastNames = {
                "Dumitriu",
                "Vasilescu",
                "Neculce",
                "Aciobanesei",
                "Pietraru",
                "Carstescu",
                "Mircea",
                "Atudoresei"
        };

        List<User> users = new ArrayList<>();

        int coefficient = 0;

        int c = 1;

        for (int i = 0; i < howMany; i++) {
            String type = types[(int) Math.floor(Math.random() * types.length + coefficient)];
            String firstName = firstNames[(int) Math.floor(Math.random() * firstNames.length + coefficient)];
            String lastName = lastNames[(int) Math.floor(Math.random() * lastNames.length + coefficient)];
            User user;
            if (withId == true) {
                user = getNewUser(type, c, firstName, lastName);
            } else {
                user = getNewUser(type, firstName, lastName);
            }
            if (user != null) {
                users.add(user);
                c++;
            }
        }

        return users;
    }
    public static List<User> generateRandomUniqueUsers(int howMany, boolean withId) {
        String[] types = USERS_ARRAY;
        String[] firstNames = {
                "Andrei",
                "Bogdan",
                "Dumitru",
                "Vlad",
                "Costel",
                "Vasile",
                "Ana",
                "Dana",
                "Victor",
                "Maria",
                "Bianca",
                "Georgeta",
                "Georgiana",
                "Victoria",
                "Angela",
                "Cristian",
                "Arabela",
                "Anabela"
        };
        String[] lastNames = {
                "Dumitriu",
                "Vasilescu",
                "Neculce",
                "Aciobanesei",
                "Pietraru",
                "Carstescu",
                "Mircea",
                "Atudoresei",
                "Violoncescu",
                "Hazarescu",
                "Ceapa",
                "Venghea",
                "Mironescu",
                "Dumitrescu",
                "Mihailovik",
                "Bespresdeu",
                "Neghea",
                "Ciorvea",
                "Cheplea",
                "Vectocsi",
                "Fesildea"
        };

        List<User> users = new ArrayList<>();

        int coefficient = 0;

        int c = 0;

        int tries = 0;

        Set<User> uniques = new HashSet<>();

        while (uniques.size() < howMany) {
            String firstName = firstNames[(int) Math.floor(Math.random() * firstNames.length + coefficient)];
            String lastName = lastNames[(int) Math.floor(Math.random() * lastNames.length + coefficient)];
            User user;
            user = getNewUser(USER_PATIENT, -1, firstName, lastName);
            if (user != null) {
                if (uniques.add(user)) {
                    tries = 0;
                    c++;
                }
            }
            tries++;
            if (tries > howMany) {
                System.out.println("No more combinations after " + c + " inputs");
                break;
            }
        }

        c = 1;

        for (User unique : uniques) {
            String type = types[(int) Math.floor(Math.random() * types.length + coefficient)];
            String firstName = unique.getFirstName();
            String lastName = unique.getLastName();
            User user;
            if (withId) {
                user = getNewUser(type, c, firstName, lastName);
            } else {
                user = getNewUser(type, firstName, lastName);
            }
            if (user != null) {
                users.add(user);
                c++;
            }
        }

        return users;
    }
    public static void fillRepository(Collection<User> list, UserRepository userRepository) {
        for (User user : list) {
            userRepository.insert(user);
        }
    }


    //enquiry methods
    public static String[] enquireDoctorName(Scanner scanner) {
        String[] doctorName = new String[0];
        while (doctorName.length < 2) {
            System.out.println("Introduceti prenumele si numele DOCTORULUI separate prin '" + NAME_SEPARATOR + "'");
            doctorName = scanner.nextLine().split(NAME_SEPARATOR);
        }
        return doctorName;
    }
    public static Doctor enquireDoctor(Scanner scanner) throws UserDoesNotExistException {
        String[] doctorName = enquireDoctorName(scanner);

        int doctorId = RepositoryLoad.userRepository.get(getNewUser(USER_DOCTOR, doctorName[0], doctorName[1]));
        return (Doctor) RepositoryLoad.userRepository.get(doctorId);
    }

    public static String[] enquirePatientName(Scanner scanner) {
        String[] patientName = new String[0];
        while (patientName.length < 2) {
            System.out.println("Introduceti prenumele si numele PACIENTULUI separate prin '" + NAME_SEPARATOR + "'");
            patientName = scanner.nextLine().split(NAME_SEPARATOR);
        }
        return patientName;
    }
    public static Patient enquirePatient(Scanner scanner) {
        String[] patientName = enquirePatientName(scanner);

        int patientId = RepositoryLoad.userRepository.get(getNewUser(USER_PATIENT, patientName[0], patientName[1]));
        return (Patient) RepositoryLoad.userRepository.get(patientId);
    }

    public static int[] enquireDate(Scanner scanner) {
        int[] date = new int[0];
        while (date.length < 3 || multiplyIntArray(date) == 0) {
            System.out.println("Introduceti anul, luna si ziua separate prin '" + STRING_SEPARATOR + "'");
            date = intFromScanner(scanner, STRING_SEPARATOR);
        }
        return date;
    }
    public static int[] enquireTime(Scanner scanner) {
        int[] time = new int[0];
        while (time.length < 2) {
            System.out.println("Introduceti ora si minutul programarii separate prin '" + TIME_SEPARATOR + "'");
            time = intFromScanner(scanner, TIME_SEPARATOR);
        }
        return time;
    }
    public static int enquireDuration(Scanner scanner) {
        int duration = 0;
        while (duration <= 0) {
            System.out.println("Introduceti durata programarii in minute intregi");
            duration = Utils.parseInteger(scanner.nextLine(), 0);
        }
        return duration;
    }

    //helpers
    private static int[] intFromScanner(Scanner scanner, String separator) {
        return intFromScanner(scanner, separator, 0);
    }
    private static int[] intFromScanner(Scanner scanner, String separator, int valOnException) {
        String[] input = scanner.nextLine().split(separator);
        int[] inputArray = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            inputArray[i] = Utils.parseInteger(input[i], valOnException);
        }
        return inputArray;
    }
    private static int multiplyIntArray(int[] array) {
        int r;
        if (array.length > 0) {
            r = 1;
            for (int i = 0; i < array.length; i++) {
                r = r * array[i];
            }
        } else {
            r = 0;
        }
        return r;
    }

}

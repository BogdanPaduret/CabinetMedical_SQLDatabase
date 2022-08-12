package views;

import exceptions.AppointmentDoesNotExistException;
import exceptions.AppointmentFailedException;
import exceptions.TooManyResultsException;
import helpers.Utils;
import models.appointments.Appointment;
import models.users.Secretary;
import models.users.User;
import repositories.RepositoryLoad;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import static helpers.Constants.*;
import static helpers.Utils.*;

public class ViewSecretary implements View {


    //instance variables
    private Secretary secretary;


    //constructor
    public ViewSecretary(Secretary secretary) {
        this.secretary = secretary;
    }
    public ViewSecretary(Secretary secretary, String databaseName) {
        init(databaseName);
        this.secretary = secretary;
    }


    //implement methods
    @Override
    public void play() {
        this.play("");
    }
    public void play(String input) {
        Scanner scanner = getScanner(input);

        boolean running = true;
        int choice = -1;

        while (running) {

            menu();

            choice = Utils.parseInteger(scanner.nextLine(), -1);

            switch (choice) {
                default -> System.out.println("Optiunea nu exista.\nIncercati din nou.");
                case 0 -> running = !Utils.exitAskSave(scanner);
                case 1 -> createAppointment(scanner);
                case 2 -> cancelAppointment(scanner);
                case 3 -> updateAppointment(scanner);
                case 6 -> showAllUsers();
                case 7 -> showAllDoctors();
                case 8 -> showAllPatients();
                case 9 -> showAllAppointments();
                case 21 -> soonestAppointment(scanner);
                case 22 -> longestAppointment(scanner);
                case 23 -> freeDoctorsOnTimeInterval(scanner);
            }
        }


    }


    //actions
    private void menu() {
        String string = "";

        string += "\n=== MOD SECRETAR ===";
        string += "\nSunteti logat ca " + secretary.getUserName().toUpperCase();

        string += "\nApasati 1 pentru a crea o programare";
        string += "\nApasati 2 pentru a anula o programare";
        string += "\nApasati 3 pentru a modifica o programare";

        string += "\nApasati 6 pentru a vedea toti utilizatorii";
        string += "\nApasati 7 pentru a vedea toti doctorii";
        string += "\nApasati 8 pentru a vedea toti pacientii";
        string += "\nApasati 9 pentru a vedea toate programarile";

        string += "\nApasati 21 pentru a vedea care este cea mai rapida programare intr-o anumita zi";
        string += "\nApasati 22 pentru a vedea care poate fi programarea cea mai lunga cu un anumit doctor";
        string += "\nApasati 23 pentru a vedea ce doctori sunt disponibili intr-o anumita perioada";

        string += "\nApasati 0 pentru a iesi";

        System.out.println(string);
    }

    private void createAppointment(Scanner scanner) {
        try {
            Appointment appointment = enquireAppointmentDetails(scanner);
            RepositoryLoad.appointmentRepository.insert(appointment);
            System.out.println("Programare efectuata cu succes cu ID-ul " + RepositoryLoad.appointmentRepository.get(appointment));

        } catch (AppointmentFailedException e) {
            System.out.println("Programarea nu poate fi facuta cu acest medic in acest interval orar." +
                    "\nAlegeti un alt doctor sau un alt interval orar."
            );
        } catch (TooManyResultsException e) {
            System.out.println("Prea multi utilizatori cu acelasi nume si aceeasi functie.");
        }

    }

    private void cancelAppointment(Scanner scanner) {
        String success = "Programare anulata cu succes!";
        String idNotFound = "O programare cu ID-ul introdus nu exista.";

        System.out.println("Introduceti ID-ul programarii");
        int id = Utils.parseInteger(scanner.nextLine(), -1);
        Appointment appointment = RepositoryLoad.appointmentRepository.get(id);
        if (appointment == null) {
            System.out.println(idNotFound);
            try {
                RepositoryLoad.appointmentRepository.delete(enquireAppointmentDetails(scanner));
                System.out.println(success);
            } catch (AppointmentFailedException e) {
                e.printStackTrace();
                System.out.println("Eroare la introducerea detaliilor. Nu exista o programare cu detaliile introduse.");
            }
        } else {
            try {
                RepositoryLoad.appointmentRepository.delete(id);
                System.out.println(success);
            } catch (AppointmentFailedException e) {
                e.printStackTrace();
                System.out.println(idNotFound);
            }
        }
    }

    private void updateAppointment(Scanner scanner) {
        System.out.println("Introduceti ID-ul programarii");
        int appointmentId = Utils.parseInteger(scanner.nextLine(), -1);

        if (appointmentId != -1) {
            updateAppointment(appointmentId,scanner);
        } else {
            throw new AppointmentDoesNotExistException();
        }
    }

    private void showAllUsers() {
        showAllUsers(USERS_ARRAY);
    }
    private void showAllDoctors() {
        showAllUsers(new String[]{USER_DOCTOR});
    }
    private void showAllPatients() {
        showAllUsers(new String[]{USER_PATIENT});
    }

    private void showAllAppointments() {
        List<Appointment> appointments = RepositoryLoad.appointmentRepository.getAll();
        for (Appointment a : appointments) {
            String doctorName = RepositoryLoad.userRepository.get(a.getDoctorId()).getUserName();
            String patientName = RepositoryLoad.userRepository.get(a.getPatientId()).getUserName();
            String string = "";
            string += "\n\nAppointment ID: " + a.getAppointmentId();
            string += "\nDoctor: " + doctorName;
            string += "\nPatient: " + patientName;
            string += "\n" + Utils.toStringAppointmentDates(a);
            System.out.println(string);
        }
    }

    private void soonestAppointment(Scanner scanner) {

    }

    private void longestAppointment(Scanner scanner) {

    }

    private void freeDoctorsOnTimeInterval(Scanner scanner) {

    }


    //helpers
    private Appointment enquireAppointmentDetails(Scanner scanner) {
        return enquireAppointmentDetails(scanner, -1);
    }
    private Appointment enquireAppointmentDetails(Scanner scanner, int appointmentId) {
        String[] patientName = new String[0];
        while (patientName.length < 2) {
            System.out.println("Introduceti prenumele si numele PACIENTULUI separate prin '" + NAME_SEPARATOR + "'");
            patientName = scanner.nextLine().split(NAME_SEPARATOR);
        }

        String[] doctorName = new String[0];
        while (doctorName.length < 2) {
            System.out.println("Introduceti prenumele si numele DOCTORULUI separate prin '" + NAME_SEPARATOR + "'");
            doctorName = scanner.nextLine().split(NAME_SEPARATOR);
        }

        int[] date = new int[0];
        while (date.length < 3 && multiplyIntArray(date) == 0) {
            System.out.println("Introduceti anul, luna si ziua separate prin '" + STRING_SEPARATOR + "'");
            date = intFromScanner(scanner, STRING_SEPARATOR);
        }

        int[] time = new int[0];
        while (time.length < 2) {
            System.out.println("Introduceti ora si minutul programarii separate prin '" + TIME_SEPARATOR + "'");
            time = intFromScanner(scanner, TIME_SEPARATOR);
        }

        int duration = 0;
        while (duration <= 0) {
            System.out.println("Introduceti durata programarii in minute intregi");
            duration = Utils.parseInteger(scanner.nextLine(), 0);
        }

        int doctorId = RepositoryLoad.userRepository.get(getNewUser(USER_DOCTOR, doctorName[0], doctorName[1]));
        int patientId = RepositoryLoad.userRepository.get(getNewUser(USER_PATIENT, patientName[0], patientName[1]));
        LocalDateTime startDate = LocalDateTime.of(date[0], date[1], date[2], time[0], time[1]);
        LocalDateTime endDate = startDate.plusMinutes(duration);

        if (appointmentId > 0) {
            return getNewAppointment(appointmentId, doctorId, patientId, startDate, endDate);
        }

        return getNewAppointment(doctorId, patientId, startDate, endDate);

    }

    private int[] intFromScanner(Scanner scanner, String separator) {
        return intFromScanner(scanner, separator, 0);
    }
    private int[] intFromScanner(Scanner scanner, String separator, int valOnException) {
        String[] input = scanner.nextLine().split(separator);
        int[] inputArray = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            inputArray[i] = Utils.parseInteger(input[i], valOnException);
        }
        return inputArray;
    }

    private int multiplyIntArray(int[] array) {
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

    private void updateAppointment(int appointmentId, Scanner scanner) {
        System.out.println("Introduceti noile valori ale programarii");
        try {
            Appointment appointment = enquireAppointmentDetails(scanner, appointmentId);
            RepositoryLoad.appointmentRepository.update(appointment);
            System.out.println("Modificare realizata cu succes!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Eroare la modificare.");
        }
    }

    private void showAllUserByType(String type) {
        List<User> users = RepositoryLoad.userRepository.getAll(type);
        for (User user : users) {
            System.out.println(user);
        }
    }
    private void showAllUsers(String[] types) {
        for (String string : types) {
            System.out.println("\nToti utilizatorii de tip " + string.toUpperCase());
            showAllUserByType(string);
        }
    }

}


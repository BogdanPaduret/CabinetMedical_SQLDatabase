package views;

import exceptions.AppointmentFailedException;
import helpers.Utils;
import models.appointments.Appointment;
import models.users.Secretary;
import repositories.RepositoryLoad;

import java.time.LocalDateTime;
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
                /*
                todo
                 case 6 -> {}
                 case 7 -> {}
                 case 8 -> {}
                 case 9 -> {}
                 */
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
        string += "\nApasati 0 pentru a iesi";

        System.out.println(string);
    }

    private void createAppointment(Scanner scanner) {
        try {
            RepositoryLoad.appointmentRepository.insert(enquireAppointmentDetails(scanner));
        } catch (AppointmentFailedException e) {
            System.out.println("Programarea nu poate fi facuta cu acest medic in acest interval orar." +
                    "\nAlegeti un alt doctor sau un alt interval orar."
            );
        }

    }

    private void cancelAppointment(Scanner scanner) {
        System.out.println("Introduceti ID-ul programarii");
        int id = Utils.parseInteger(scanner.nextLine(), -1);
        if (id != -1) {
            try {
                RepositoryLoad.appointmentRepository.delete(enquireAppointmentDetails(scanner));
            } catch (AppointmentFailedException e) {
                e.printStackTrace();
                System.out.println("Eroare la introducerea detaliilor");
            }
        } else {
            try {
                RepositoryLoad.appointmentRepository.delete(id);
            } catch (AppointmentFailedException e) {
                e.printStackTrace();
                System.out.println("Programare cu ID-ul introdus nu exista");
            }
        }
    }

    private void updateAppointment(Scanner scanner) {

    }



    //helpers
    private Appointment enquireAppointmentDetails(Scanner scanner) {
        System.out.println("Introduceti prenumele si numele PACIENTULUI separate prin '" + NAME_SEPARATOR + "'");
        String[] patientName = scanner.nextLine().split(NAME_SEPARATOR);
        System.out.println("Introduceti prenumele si numele DOCTORULUI separate prin '" + NAME_SEPARATOR + "'");
        String[] doctorName = scanner.nextLine().split(NAME_SEPARATOR);

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

}


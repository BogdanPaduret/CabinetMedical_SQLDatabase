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
                /*
                todo
                 case 2 -> {}
                 case 3 -> {}
                 case 6 -> {}
                 case 7 -> {}
                 case 8 -> {}
                 case 9 -> {}
                 */
            }
        }


    }


    //helpers
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

    private Appointment enquireAppointmentDetails(Scanner scanner) {
        System.out.println("Introduceti prenumele si numele PACIENTULUI separate prin '" + NAME_SEPARATOR + "'");
        String[] patientName = scanner.nextLine().split(NAME_SEPARATOR);
        System.out.println("Introduceti prenumele si numele DOCTORULUI separate prin '" + NAME_SEPARATOR + "'");
        String[] doctorName = scanner.nextLine().split(NAME_SEPARATOR);

        int year = 0;
        int month = 0;
        int day = 0;
        String[] input;
        while (year * month * day == 0) {
            System.out.println("Introduceti anul, luna si ziua programarii, separate prin '" + STRING_SEPARATOR + "'");
            input = scanner.nextLine().split(STRING_SEPARATOR);

            try {
                year = Integer.parseInt(input[0]);
                month = Integer.parseInt(input[1]);
                day = Integer.parseInt(input[2]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                System.out.println("Eroare la introducerea datei. Mai incercati o data");
            }
        }

        int startHour = 0;
        int startMinute=-1;
        while (startHour == 0 || startMinute<0) {
            System.out.println("Introduceti ora si minutul programarii separate prin '" + TIME_SEPARATOR + "'");
            input = scanner.nextLine().split(TIME_SEPARATOR);
            try {
                startHour = Integer.parseInt(input[0]);
                startMinute = Integer.parseInt(input[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                System.out.println("Eroare la introducerea orei. Mai incercati o data");
            }
        }

        int duration = 0;
        while (duration == 0) {
            System.out.println("Introduceti durata programarii in minute intregi");
            try {
                duration = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                System.out.println("Durata introdusa este incorecta. Mai incercati o data");
            }
        }

        int doctorId = RepositoryLoad.userRepository.get(getNewUser(USER_DOCTOR, doctorName[0], doctorName[1]));
        int patientId = RepositoryLoad.userRepository.get(getNewUser(USER_PATIENT, patientName[0], patientName[1]));
        LocalDateTime startDate = LocalDateTime.of(year, month, day, startHour, startMinute);
        LocalDateTime endDate = startDate.plusMinutes(duration);

        return getNewAppointment(doctorId, patientId, startDate, endDate);
    }

}


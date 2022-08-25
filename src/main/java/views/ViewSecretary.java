package views;

import exceptions.AppointmentDoesNotExistException;
import exceptions.AppointmentFailedException;
import exceptions.TooManyResultsException;
import exceptions.UserDoesNotExistException;
import helpers.Constants;
import helpers.Utils;
import models.appointments.Appointment;
import models.users.Doctor;
import models.users.Patient;
import models.users.Secretary;
import models.users.User;
import repositories.RepositoryLoad;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                case 20 -> showFreeSlotsByDoctorId(scanner);
                case 21 -> soonestAppointment(scanner);//showAllFreeSlotsByDay(scanner);
                case 22 -> longestAppointmentAtDoctor(scanner);//showAllFreeSlots(scanner);
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

        string += "\nApasati 20 pentru a vedea toate spatiile libere ale unui anumit doctor intr-o anumita zi";
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
        } catch (UserDoesNotExistException e) {
            System.out.println(e.getMessage());
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
            string += "\n" + Utils.toStringAppointmentTimeline(a);
            System.out.println(string);
        }
    }

    private void showFreeSlotsByDoctorId(Scanner scanner) {
        String[] doctorName = enquireDoctorName(scanner);
        try {
            int doctorId = RepositoryLoad.userRepository.get(getNewUser(USER_DOCTOR, doctorName[0], doctorName[1]));
            int[] date = enquireDate(scanner);
            LocalDate day = LocalDate.of(date[0], date[1], date[2]);
            List<Appointment> freeSlots = RepositoryLoad.appointmentRepository.getFreeSlots(doctorId, day);
            if (!freeSlots.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy");
                System.out.println("\nFree slots for doctor " + doctorName[0].toUpperCase() + " " + doctorName[1].toUpperCase() + " on " +
                        day.format(formatter) + ":");
                for (Appointment a : freeSlots) {
                    System.out.println("\n" + Utils.toStringAppointmentTime(a));
                }
            } else {
                System.out.println("Nu exista slot-uri libere cu doctorul ales in ziua aleasa.");
            }
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
        }
    }

    private void soonestAppointment(Scanner scanner) {
        int[] date = enquireDate(scanner);
        if (date.length >= 3) {
            LocalDate day = LocalDate.of(date[0], date[1], date[2]);

            Appointment appointment = RepositoryLoad.appointmentRepository.getFirstFreeSlot(day);
            if (appointment != null) {
                String string = "";
                string += "Cea mai rapida programare poate fi facuta cu doctorul ";
                string += RepositoryLoad.userRepository.get(appointment.getDoctorId()).getUserName().toUpperCase();
                string += " in intervalul ";
                string += Utils.toStringAppointmentTime(appointment);
                string += "\n" + Utils.toStringAppointmentDuration(appointment);
                System.out.println(string);
            } else {
                System.out.println("Nu exista nici un spatiu liber in ziua selectata");
            }
        } else {
            System.out.println("Date introduse incorect");
        }
    }

    private void longestAppointmentAtDoctor(Scanner scanner) {
        String[] doctorName = enquireDoctorName(scanner);
        try {
            int doctorId = RepositoryLoad.userRepository.get(Utils.getNewUser(USER_DOCTOR, doctorName[0], doctorName[1]));
            int[] date = enquireDate(scanner);
            LocalDate day = LocalDate.of(date[0], date[1], date[2]);

            Appointment appointment = RepositoryLoad.appointmentRepository.getLongestFreeSlot(doctorId, day);

            Doctor doctor = (Doctor) RepositoryLoad.userRepository.get(doctorId);

            if (appointment != null) {
                String string = "";

                string += "Cea mai lunga programare ce poate fi facuta cu doctorul " + doctor.getUserName().toUpperCase();
                string += " este in intervalul orar " + Utils.toStringAppointmentTime(appointment);
                string += " (" + Utils.toStringAppointmentDuration(appointment) + ")";

                System.out.println(string);
            } else {
                System.out.println("Nu exista nici un spatiu liber in ziua selectata cu doctorul selectat");
            }

        } catch (UserDoesNotExistException e ) {
            e.printStackTrace();
        }
    }

    private void freeDoctorsOnTimeInterval(Scanner scanner) {
        int[] date = enquireDate(scanner);
        int[] time = enquireTime(scanner);
        LocalDateTime startDate = LocalDateTime.of(date[0], date[1], date[2], time[0], time[1]);
        int duration = enquireDuration(scanner);
        LocalDateTime endDate = startDate.plusMinutes(duration);

        Appointment appointment = getNewAppointment(-1, -1, startDate, endDate);

        List<Doctor> doctors = RepositoryLoad.appointmentRepository.getFreeDoctors(appointment);

        if (doctors != null && !doctors.isEmpty()) {
            String string = "";
            string += "Doctori liber pentru: " + Utils.toStringAppointmentDate(appointment) + " " + Utils.toStringAppointmentTime(appointment);
            for (Doctor doctor : doctors) {
                string += "\n" + doctor.getUserName().toUpperCase();
            }
            System.out.println(string);
        }
    }


    //helpers
    private Appointment enquireAppointmentDetails(Scanner scanner) {
        return enquireAppointmentDetails(scanner, -1);
    }
    private Appointment enquireAppointmentDetails(Scanner scanner, int appointmentId) {
        String[] patientName;
        String[] doctorName;

        int patientId;
        int doctorId;

        int[] date;
        int[] time;
        int duration;

//        patientName = enquirePatientName(scanner);
//        patientId = RepositoryLoad.userRepository.get(getNewUser(USER_PATIENT, patientName[0], patientName[1]));

//        doctorName = enquireDoctorName(scanner);
//        doctorId = RepositoryLoad.userRepository.get(getNewUser(USER_DOCTOR, doctorName[0], doctorName[1]));

        Patient patient;
        Doctor doctor;

        try {
            patient = Utils.enquirePatient(scanner);
        } catch (UserDoesNotExistException e) {
            throw new UserDoesNotExistException("Patient does not exist.");
        }

        try {
            doctor = Utils.enquireDoctor(scanner);
        } catch (UserDoesNotExistException e) {
            throw new UserDoesNotExistException("Doctor does not exist.");
        }

        patientId = patient.getId();
        doctorId = doctor.getId();

        date = enquireDate(scanner);
        time = enquireTime(scanner);
        duration = enquireDuration(scanner);

        LocalDateTime startDate = LocalDateTime.of(date[0], date[1], date[2], time[0], time[1]);
        LocalDateTime endDate = startDate.plusMinutes(duration);

        if (appointmentId > 0) {
            return getNewAppointment(appointmentId, doctorId, patientId, startDate, endDate);
        }

        return getNewAppointment(doctorId, patientId, startDate, endDate);

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


package views;


import exceptions.AppointmentFailedException;
import helpers.Utils;
import models.appointments.Appointment;
import models.users.Doctor;
import repositories.Repository;
import repositories.RepositoryLoad;

import java.time.LocalDate;
import java.util.Scanner;

public class ViewDoctor implements View {

    //instance variables
    private Doctor doctor;

    //constructor
    public ViewDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @Override
    public void play() {
        this.play("");
    }
    public void play(String input) {
        Scanner scanner = Utils.getScanner(input);

        boolean running = true;
        int choice = -1;
        while (running) {
            menu();

            choice = Utils.parseInteger(scanner.nextLine(), -1);

            switch (choice) {
                default -> System.out.println("Optiunea nu exista.\nIncercati din nou.");
                case 0 -> running = !Utils.exitAskSave(scanner);
                case 1 -> createHoliday(scanner);
                case 2 -> cancelHoliday(scanner);
                case 3 -> createBreak(scanner);
                case 4 -> cancelBreak(scanner);
            }
        }
    }

    //actions
    private void menu() {
        String string = "";

        string += "\n=== MOD DOCTOR ===";
        string += "\nSunteti logat ca " + doctor.getUserName().toUpperCase();

        string += "\nApasati 1 pentru a crea o zi libera in calendar";
        string += "\nApasati 2 pentru a anula o zi libera din calendar";
        string += "\nApasati 3 pentru a crea o pauza in calendar";
        string += "\nApasati 4 pentru a anula o pauza din calendar";

        string += "\nApasati 0 pentru a iesi";

        System.out.println(string);
    }

    private void createHoliday(Scanner scanner) {
        Appointment holiday = enquireHolidayDetails(scanner);
        try {
            RepositoryLoad.appointmentRepository.insert(holiday);
        } catch (AppointmentFailedException e) {
            System.out.println("Aceasta zi contine alte programari deja.");
        }
    }
    private void cancelHoliday(Scanner scanner) {
        Appointment holiday = enquireHolidayDetails(scanner);
        try {
            RepositoryLoad.appointmentRepository.delete(holiday);
        } catch (AppointmentFailedException e) {
            e.printStackTrace();
        }
    }

    //todo createBreak() si cancelBreak()
    private void createBreak(Scanner scanner) {

    }
    private void cancelBreak(Scanner scanner) {

    }

    //helpers
    private Appointment enquireHolidayDetails(Scanner scanner) {
        int[] date = Utils.enquireDate(scanner);
        LocalDate day = LocalDate.of(date[0], date[1], date[2]);

        return Utils.getNewAppointment(doctor.getId(), doctor.getId(), day.atTime(9, 0), day.atTime(17, 0));
    }
}

package repositories;

import models.appointments.Appointment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static helpers.Constants.APPOINTMENTS_TABLE_NAME;
import static helpers.Constants.USERS_TABLE_NAME;
import static helpers.Utils.querySelect;

public class AppointmentRepository extends Repository<Appointment> {

    //constructor
    public AppointmentRepository() {
        super();
    }
    public AppointmentRepository(String Jdbc, String userName, String password) {
        super(Jdbc, userName, password);
    }


    //create
    @Override
    public void insert(Appointment obj) {
        insert(new Appointment[]{obj});
    }
    public void insert(Appointment[] appointments) {
        String string = "";

        string += "INSERT INTO " + APPOINTMENTS_TABLE_NAME + " (doctorId, patientId, startDateTime, endDateTime)";
        string += "\nVALUES";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d HH:mm");

        for (Appointment a : appointments) {
            int doctorId = a.getDoctorId();
            int patientId = a.getPatientId();
            String startDate = a.getStartDate().format(formatter);
            String endDate = a.getEndDate().format(formatter);
            string += String.format("\n(%d, %d, '%s', '%s'", doctorId, patientId, startDate, endDate);
        }

        executeStatement(string);
    }




    /*
    todo
     restul de implemented methods
     */


    //read
    @Override
    public Appointment get(int id) {
        return null;
    }

    @Override
    public int get(Appointment obj) {
        return -1;
    }

    @Override
    public List<Appointment> getAll() {
        return null;
    }


    //update
    @Override
    public void update(Appointment obj) {

    }


    //delete
    @Override
    public void delete(Appointment obj) {

    }
    @Override
    public void delete(int id) {

    }

    @Override
    public void clear() {
        String[] strings = {
                "DELETE FROM " + APPOINTMENTS_TABLE_NAME,
                "ALTER TABLE " + APPOINTMENTS_TABLE_NAME + " AUTO_INCREMENT = 0"
        };

        for (String string : strings) {
            executeStatement(string);
        }
    }


    //helpers

}

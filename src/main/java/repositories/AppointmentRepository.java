package repositories;

import exceptions.AppointmentDoesNotExistException;
import exceptions.TooManyResultsException;
import exceptions.UserDoesNotExistException;
import helpers.Utils;
import models.appointments.Appointment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static helpers.Constants.APPOINTMENTS_TABLE_NAME;
import static helpers.Constants.USERS_TABLE_NAME;
import static helpers.Utils.*;

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
        String string = Utils.querySelect(APPOINTMENTS_TABLE_NAME);

        string += String.format("\nWHERE id = %d", id);

        executeStatement(string);

        Appointment appointment = null;

        try {
            ResultSet set = getStatement().getResultSet();
            while (set.next()) {
                Appointment buffer = getFromSet(set);
                if (buffer.getAppointmentId() == id) {
                    appointment = buffer;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointment;
    }

    @Override
    public int get(Appointment obj) throws AppointmentDoesNotExistException, TooManyResultsException {
        int doctorId = obj.getDoctorId();
        int patientId = obj.getPatientId();
        LocalDateTime startDate = obj.getStartDate();
        LocalDateTime endDate = obj.getEndDate();

        String string = Utils.querySelect(APPOINTMENTS_TABLE_NAME);

        string += String.format("\nWHERE doctorId = %d, patientId = %d, startDateTime = '%s', endDateTime = '%s'",
                doctorId, patientId, Utils.toSQLDateTimeString(startDate), Utils.toSQLDateTimeString(endDate));

        executeStatement(string);

        List<Integer> ids = new ArrayList<>();

        try {
            ResultSet set = getStatement().getResultSet();
            while (set.next()) {
                Appointment buffer = getFromSet(set);
                if (buffer.getDoctorId() == doctorId &&
                        buffer.getPatientId() == patientId &&
                        buffer.getStartDate().equals(startDate) &&
                        buffer.getEndDate().equals(endDate))
                {
                    ids.add(buffer.getAppointmentId());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (ids.size() > 1) {
            throw new TooManyResultsException();
        } else if (ids.size() == 0) {
            throw new UserDoesNotExistException();
        } else {
            return ids.get(0);
        }
    }

    @Override
    public List<Appointment> getAll() {
        String string = Utils.querySelect(APPOINTMENTS_TABLE_NAME);

        executeStatement(string);

        List<Appointment> appointments = new ArrayList<>();

        try {
            ResultSet set = getStatement().getResultSet();
            while (set.next()) {
                appointments.add(getFromSet(set));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if (appointments.size() == 0) {
            appointments = null;
        }

        return appointments;
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
    public static Appointment getFromSet(ResultSet set) throws SQLException {
        int appointmentId = set.getInt(1);
        int doctorId = set.getInt(2);
        int patientId = set.getInt(3);
        LocalDateTime startDate = set.getTimestamp(4).toLocalDateTime();
        LocalDateTime endDate = set.getTimestamp(5).toLocalDateTime();

        Method[] allMethods = Appointment.class.getMethods();
        List<Method> setters = new ArrayList<Method>();
        for (Method method : allMethods) {
            if (method.getName().startsWith("get")) {
                setters.add(method);
            }
        }

        return getNewAppointment(appointmentId, doctorId, patientId, startDate, endDate);
    }



}

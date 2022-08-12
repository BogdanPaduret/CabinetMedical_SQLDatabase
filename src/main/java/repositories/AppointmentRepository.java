package repositories;

import exceptions.*;
import helpers.Utils;
import models.appointments.Appointment;
import models.appointments.Holiday;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static helpers.Constants.*;
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
//            try {
//                if (!isHoliday(a)) {
                    int doctorId = a.getDoctorId();
                    int patientId = a.getPatientId();
                    String startDate = a.getStartDate().format(formatter);
                    String endDate = a.getEndDate().format(formatter);
                    string += String.format("\n(%d, %d, '%s', '%s')", doctorId, patientId, startDate, endDate);
//                } else {
//                    throw new AppointmentOnHolidayException(a.getStartDate() + "falls on a holiday for the interested party. Appointment not inserted.");
//                }
//            } catch (AppointmentFailedException e) {
//                e.printStackTrace();
//            }
        }

        executeStatement(string);
    }


    //read
    public boolean isHoliday(Appointment appointment) {
//        return getHolidays(appointment).size() != 0;
        return false;
    }
    public List<Holiday> getHolidays(Appointment appointment) {
        String string = Utils.querySelect(HOLIDAYS_TABLE_NAME);

        string += String.format("\nWHERE ('%s' BETWEEN startDate AND endDate) AND (doctorId = %d OR patientId = %d OR (doctorId IS NULL AND patientId IS NULL))",
                Utils.toSQLDateTimeString(appointment.getStartDate()), appointment.getDoctorId(), appointment.getPatientId());

        executeStatement(string);

        List<Holiday> holidays = new ArrayList<>();

        try {
            ResultSet set = getStatement().getResultSet();
            while (set.next()) {
                holidays.add((Holiday) getFromSet(set));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return holidays;
    }

    @Override
    public Appointment get(int id) {
        String string = Utils.querySelect(APPOINTMENTS_TABLE_NAME);

        string += String.format("\nWHERE appointmentId = %d", id);

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
        return getAllHelper(new String[0], new String[0]);
    }
    public List<Appointment> getAll(String[] variableNames, String[] variableValues) throws InvalidParameterException {
        checkLengths(variableNames, variableValues);

        String[] stringValues = toSQLStringArray(variableValues, "'s'");

        return getAllHelper(variableNames, stringValues);
    }
    public List<Appointment> getAll(String[] variableNames, Integer[] variableValues) throws InvalidParameterException {
        checkLengths(variableNames, variableValues);

        String[] stringValues = toSQLStringArray(variableValues, "%d");

        return getAllHelper(variableNames, stringValues);
    }

    private <T, U> void checkLengths(T[] array1, U[] array2) {
        if (array1.length != array2.length) {
            throw new InvalidParameterException();
        }
    }
    private <T> String[] toSQLStringArray(T[] data, String format) {
        String[] stringValues = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            stringValues[i] = String.format(format, data[i].toString());
        }
        return stringValues;
    }
    private List<Appointment> getAllHelper(String[] variableName, String[] variableValue) {
        String string = Utils.querySelect(APPOINTMENTS_TABLE_NAME);

        if (variableName.length > 0) {
            string += "\nWHERE";
            for (int i = 0; i < variableName.length; i++) {
                if (i > 0) {
                    string += " AND";
                }
                string += String.format(" %s = %s", variableName[i], variableValue[i]);
            }
        }

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

        return appointments;    }


    //update
    @Override
    public void update(Appointment o) {
        String string = "";

        string += "UPDATE " + APPOINTMENTS_TABLE_NAME;
        string += String.format("\nSET doctorId = %d, patientId = %d, startDateTime = '%s', endDateTime = '%s'",
                o.getDoctorId(), o.getPatientId(), Utils.toSQLDateTimeString(o.getStartDate()), Utils.toSQLDateTimeString(o.getEndDate()));
        string += String.format("\nWHERE id = '%d'", o.getAppointmentId());

        executeStatement(string);
    }


    //delete
    @Override
    public void delete(Appointment obj) {
        String string = "DELETE FROM " + APPOINTMENTS_TABLE_NAME;
        string += String.format("\nWHERE doctorId = %d AND patientId = %d AND startDateTime = '%s' AND endDateTime = '%s'",
                obj.getDoctorId(), obj.getPatientId(), Utils.toSQLDateTimeString(obj.getStartDate()), Utils.toSQLDateTimeString(obj.getEndDate()));

        executeStatement(string);
    }
    @Override
    public void delete(int id) {
        String string = "DELETE FROM " + APPOINTMENTS_TABLE_NAME;
        string += String.format("\nWHERE appointmentId = %d", id);

        executeStatement(string);
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
    private Appointment getFromSet(ResultSet set) throws SQLException {
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

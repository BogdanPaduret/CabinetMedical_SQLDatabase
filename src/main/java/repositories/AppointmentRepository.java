package repositories;

import exceptions.*;
import helpers.Utils;
import models.appointments.Appointment;
import models.appointments.Holiday;
import models.users.Doctor;

import javax.print.Doc;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.DoubleConsumer;

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
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEEE dd-MM-yyyy 'at' HH:mm");

        for (Appointment a : appointments) {
            try {
                if (dateTimeFree(a, (Doctor) RepositoryLoad.userRepository.get(a.getDoctorId()))) {
                    int doctorId = a.getDoctorId();
                    int patientId = a.getPatientId();
                    String startDate = a.getStartDate().format(formatter);
                    String endDate = a.getEndDate().format(formatter);
                    string += String.format("\n(%d, %d, '%s', '%s')", doctorId, patientId, startDate, endDate);
                } else {
                    throw new AppointmentFailedException("Appointment not possible on " + a.getStartDate().format(dayFormatter) + ".\nPlease notice that the working hours are 09:00 to 17:00 everyday except weekends.");
                }
            } catch (AppointmentFailedException e) {
                e.printStackTrace();
            }
        }

        executeStatement(string);
    }


    //read
    public boolean dateTimeFree(Appointment appointment, Doctor doctor) {
        boolean isDateFree = isDateFree(appointment.getStartDate());
        boolean isTimeFree = isTimeFree(appointment, doctor);

        return isDateFree && isTimeFree;
    }

    private boolean isTimeFree(Appointment appointment, Doctor doctor) {
        LocalTime startTime = Doctor.getStartTime();
        LocalTime endTime = Doctor.getEndTime();

        LocalTime start = appointment.getStartDate().toLocalTime();
        LocalTime end = appointment.getEndDate().toLocalTime();

        if (start.compareTo(startTime) >= 0 && end.compareTo(endTime) <= 0) {
            String string = Utils.querySelect(APPOINTMENTS_TABLE_NAME);
            string += String.format("\nWHERE ('%s' > startDateTime AND '%s' < endDateTime) AND " +
                            "('%s' > startDateTime AND '%s' < endDateTime) AND " +
                            "doctorId = %d",
                    appointment.getStartDate(), appointment.getStartDate(), appointment.getEndDate(), appointment.getEndDate(), doctor.getId());
            executeStatement(string);

            try {
                ResultSet set = getStatement().getResultSet();
                if (set.next()) {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            return false;
        }

        return true;
    }
    private boolean isDateFree(LocalDateTime startDate) {
        for (DayOfWeek day : Doctor.getWorkDays()) {
            if (startDate.getDayOfWeek().equals(day)) {
                return true;
            }
        }
        return false;
    }

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

        string += String.format("\nWHERE doctorId = %d AND patientId = %d AND startDateTime = '%s' AND endDateTime = '%s'",
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

    public List<Appointment> getAll(int doctorId, LocalDate day) {
        String string = Utils.querySelect(APPOINTMENTS_TABLE_NAME);
        string += String.format("\nWHERE doctorId = %d AND startDateTime BETWEEN '%s' AND '%s'",
                doctorId, Utils.toSQLDateTimeString(day.atTime(9, 0)), Utils.toSQLDateTimeString(day.atTime(17, 0)));
        string += "\nORDER BY startDateTime";

        return getAllFromSQLString(string);

//        executeStatement(string);
//
//        List<Appointment> appointments = new ArrayList<>();
//
//        try {
//            ResultSet set = getStatement().getResultSet();
//            while (set.next()) {
//                appointments.add(getFromSet(set));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return appointments;
    }

    //todo: de terminat metoda asta
    public List<Appointment> getFreeSlots(int doctorId, LocalDate day) {
        List<Appointment> appointments = getAll(doctorId, day);
        List<Appointment> freeSlots = new ArrayList<>();
        Iterator<Appointment> iterator = appointments.iterator();

        LocalTime previousEndTime = Doctor.getStartTime();
        LocalTime nextStartTime;

        Appointment appointment;

        while (iterator.hasNext()) {
            Appointment next = iterator.next();

            nextStartTime = next.getStartDate().toLocalTime();

            appointment = createFreeSlotAppointment(doctorId, previousEndTime, nextStartTime, day);
            if (appointment != null) {
                freeSlots.add(appointment);
            }

            previousEndTime = next.getEndDate().toLocalTime();
        }

        nextStartTime = Doctor.getEndTime();

        appointment = createFreeSlotAppointment(doctorId, previousEndTime, nextStartTime, day);
        if (appointment != null) {
            freeSlots.add(appointment);
        }

        return freeSlots;
    }
    public List<Appointment> getFreeSlots(int doctorId, int year, int month, int dayOfMonth) {
        LocalDate day = LocalDate.of(year, month, dayOfMonth);
        return getFreeSlots(doctorId, day);
    }
    private Appointment createFreeSlotAppointment(int doctorId, LocalTime startTime, LocalTime endTime, LocalDate day) {
        if (startTime.isBefore(endTime)) {
            LocalDateTime start = startTime.atDate(day);
            LocalDateTime end = endTime.atDate(day);
            return getNewAppointment(doctorId, -1, start, end);
        }
        return null;
    }

    public List<Appointment> getFreeSlots(int howMany, LocalDateTime startDateTime) {
        return null;
    }
    //update
    @Override
    public void update(Appointment o) {
        String string = "";

        string += "UPDATE " + APPOINTMENTS_TABLE_NAME;
        string += String.format("\nSET doctorId = %d, patientId = %d, startDateTime = '%s', endDateTime = '%s'",
                o.getDoctorId(), o.getPatientId(), Utils.toSQLDateTimeString(o.getStartDate()), Utils.toSQLDateTimeString(o.getEndDate()));
        string += String.format("\nWHERE appointmentId = '%d'", o.getAppointmentId());

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

        return getAllFromSQLString(string);

//        executeStatement(string);
//
//        List<Appointment> appointments = new ArrayList<>();
//
//        try {
//            ResultSet set = getStatement().getResultSet();
//            while (set.next()) {
//                appointments.add(getFromSet(set));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//
//        if (appointments.size() == 0) {
//            appointments = null;
//        }
//
//        return appointments;
    }

    private List<Appointment> getAllFromSQLString(String string) {
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

}


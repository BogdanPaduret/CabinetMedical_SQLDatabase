package models.appointments;

import helpers.Utils;

import java.time.Duration;
import java.time.LocalDateTime;

import static helpers.Constants.STRING_SEPARATOR;

public class Appointment implements Comparable<Appointment> {

    //instance variables
    private int appointmentId;
    private int doctorId;
    private int patientId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    //constructor
    private Appointment(int appointmentId, int doctorId, int patientId) {
        this.appointmentId = appointmentId;
        this.doctorId = doctorId;
        this.patientId = patientId;
    }

    public Appointment(int appointmentId, int doctorId, int patientId, LocalDateTime startDate, LocalDateTime endDate) {
        this(appointmentId, doctorId, patientId);
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public Appointment(int appointmentId,
                       int doctorId,
                       int patientId,
                       String startDate,
                       String endDate) {
        this(appointmentId, doctorId, patientId);

        String[] start = startDate.split(STRING_SEPARATOR);
        String[] end = endDate.split(STRING_SEPARATOR);

        int syy = Integer.parseInt(start[0]);
        int smm = Integer.parseInt(start[1]);
        int sdd = Integer.parseInt(start[2]);
        int shh = Integer.parseInt(start[3]);
        int smin = Integer.parseInt(start[4]);

        int eyy = Integer.parseInt(end[0]);
        int emm = Integer.parseInt(end[1]);
        int edd = Integer.parseInt(end[2]);
        int ehh = Integer.parseInt(end[3]);
        int emin = Integer.parseInt(end[4]);

        this.startDate = LocalDateTime.of(syy, smm, sdd, shh, smin);
        this.endDate = LocalDateTime.of(eyy, emm, edd, ehh, emin);
    }
    public Appointment(int appointmentId,
                       int doctorId,
                       int patientId,
                       int startYear,
                       int startMonth,
                       int startDay,
                       int startHour,
                       int startMinute,
                       int endYear,
                       int endMonth,
                       int endDay,
                       int endHour,
                       int endMinute) {

        this(appointmentId, doctorId, patientId);

        this.startDate = LocalDateTime.of(startYear, startMonth, startDay, startHour, startMinute);
        this.endDate = LocalDateTime.of(endYear, endMonth, endDay, endHour, endMinute);
    }

    //read
    public int getAppointmentId() {
        return appointmentId;
    }
    public int getDoctorId() {
        return doctorId;
    }
    public int getPatientId() {
        return patientId;
    }
    public LocalDateTime getStartDate() {
        return startDate;
    }
    public LocalDateTime getEndDate() {
        return endDate;
    }
    public Duration getDuration() {
        return Duration.between(startDate, endDate);
    }

    //update
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }
    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    public void set(Appointment appointment) {
        this.appointmentId = appointment.appointmentId;
        this.doctorId = appointment.doctorId;
        this.patientId = appointment.patientId;
        this.startDate = appointment.startDate;
        this.endDate = appointment.endDate;
    }

    //implement methods
    @Override
    public String toString() {
        String string = "Appointment #"+appointmentId;
        string += "\nAppointment of patient with ID " + patientId + " with doctor with ID " + doctorId + ":";
        string += "\n" + Utils.toStringAppointmentTimeline(this);

        return string;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Appointment appointment) &&
                this.appointmentId == appointment.appointmentId &&
                this.doctorId == appointment.doctorId &&
                this.patientId == appointment.patientId &&
                this.startDate.equals(appointment.startDate) &&
                this.endDate.equals(appointment.endDate);
    }

    @Override
    public int compareTo(Appointment o) {
        return this.startDate.compareTo(o.startDate);
    }




}

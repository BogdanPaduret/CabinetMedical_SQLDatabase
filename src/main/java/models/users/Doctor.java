package models.users;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static helpers.Constants.USER_DOCTOR;

public class Doctor extends User {

    //instance variables
    private static LocalTime startTime = LocalTime.of(9, 0);
    private static LocalTime endTime = LocalTime.of(17, 0);
    private static DayOfWeek[] workDays = {
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
    };

    //constructor
    public Doctor(int userId, String firstName, String lastName) {
        super(USER_DOCTOR, userId, firstName, lastName);
    }

    //read


    public static LocalTime getStartTime() {
        return startTime;
    }
    public static LocalTime getEndTime() {
        return endTime;
    }
    public static DayOfWeek[] getWorkDays() {
        return workDays;
    }

    //implement methods
    @Override
    public String toString() {
        String string = "";

        string += "Doctor ID: " + this.getId();
        string += "\nDoctor Name: " + this.getFirstName() + " " + this.getLastName();

        return string;
    }
    @Override
    public boolean equals(Object o) {
        return (o instanceof Doctor doctor) &&
                super.equals(o);
    }

}


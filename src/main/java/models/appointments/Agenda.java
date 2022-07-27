package models.appointments;

import models.users.User;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Agenda {

    //instance variables
    private Map<User, Set<Appointment>> userAppointmentsMap;

    //constructor
    public Agenda() {
        userAppointmentsMap = new TreeMap<>();
    }
}

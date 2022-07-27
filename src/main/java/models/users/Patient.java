package models.users;

import static helpers.Constants.USER_PATIENT;

public class Patient extends User {

    //constructor
    public Patient(int userId, String firstName, String lastName) {
        super(USER_PATIENT, userId, firstName, lastName);
    }

    //implement methods
    @Override
    public String toString() {
        String string = "";

        string += "Patient ID: " + getUserId();
        string += "\nPatient name: " + getFirstName() + " " + getLastName();

        return string;
    }
    @Override
    public boolean equals(Object o) {
        return (o instanceof Patient patient) &&
                super.equals(o);
    }

}

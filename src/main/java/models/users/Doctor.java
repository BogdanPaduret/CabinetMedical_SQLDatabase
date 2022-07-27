package models.users;

import static helpers.Constants.USER_DOCTOR;

public class Doctor extends User {

    //constructor
    public Doctor(int userId, String firstName, String lastName) {
        super(USER_DOCTOR, userId, firstName, lastName);
    }

    //implement methods
    @Override
    public String toString() {
        String string = "";

        string += "Doctor ID: " + this.getUserId();
        string += "\nDoctor Name: " + this.getFirstName() + this.getLastName();

        return string;
    }
    @Override
    public boolean equals(Object o) {
        return (o instanceof Doctor doctor) &&
                super.equals(o);
    }

}


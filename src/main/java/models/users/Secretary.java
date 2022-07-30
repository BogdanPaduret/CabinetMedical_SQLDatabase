package models.users;

import static helpers.Constants.USER_SECRETARY;

public class Secretary extends User {

    //constructor
    public Secretary(int userId, String firstName, String lastName) {
        super(USER_SECRETARY, userId, firstName, lastName);
    }

    //implement methods
    @Override
    public String toString() {
        String string = "";

        string += "Secretary ID: " + getId();
        string += "\nSecretary name: " + getFirstName() + " " + getLastName();

        return string;
    }
    @Override
    public boolean equals(Object o) {
        return (o instanceof Secretary secretary) &&
                super.equals(o);
    }

}

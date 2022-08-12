package models.users;

import java.util.Objects;

import static helpers.Constants.NAME_SEPARATOR;

public abstract class User implements Comparable<User> {

    //instance variables
    private String type;
    private int id;
    private String firstName;
    private String lastName;

    //constructor
    public User(String type, int id, String firstName, String lastName) {
        this.type = type;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    //read
    public String getType() {
        return type;
    }
    public int getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getUserName() {
        return getFirstName() + NAME_SEPARATOR + getLastName();
    }

    //updateAdd
    public void setType(String type) {
        this.type = type;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    //implemented methods
    @Override
    public boolean equals(Object o) {
        return (o instanceof User user) &&
                this.getType().equals(user.getType()) &&
                this.getId() == user.getId() &&
                this.getFirstName().equals(user.getFirstName()) &&
                this.getLastName().equals(user.getLastName());
    }
    @Override
    public int compareTo(User o) {
        User user = (User) o;

        Integer thisId = this.getId();
        Integer userId = user.getId();
        String thisFN = this.getFirstName();
        String userFN = user.getFirstName();
        String thisLN = this.getLastName();
        String userLN = user.getLastName();

        int id = thisId.compareTo(userId);
        int firstName = thisFN.compareTo(userFN);
        int lastName = thisLN.compareTo(userLN);

        if (id != 0) {
            return id;
        } else if (lastName != 0) {
            return lastName;
        } else if (firstName != 0) {
            return firstName;
        } else return this.getType().compareTo(user.getType());

    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id, firstName, lastName);
    }
    @Override
    public String toString() {
        String string = "";

        string += "User type: " + getType();
        string += "\nUser ID: " + getId();
        string += "\nUser Name: " + getFirstName() + " " + getLastName();

        return string;
    }


}

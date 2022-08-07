package models.users;

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

        return thisId.compareTo(userId);
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

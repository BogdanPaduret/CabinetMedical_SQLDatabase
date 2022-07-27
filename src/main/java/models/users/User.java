package models.users;

public abstract class User implements Comparable<User> {

    //instance variables
    private String type;
    private int userId;
    private String firstName;
    private String lastName;

    //constructor
    public User(String type, int userId, String firstName, String lastName) {
        this.type = type;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    //read
    public String getType() {
        return type;
    }
    public int getUserId() {
        return userId;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }

    //updateAdd
    public void setType(String type) {
        this.type = type;
    }
    public void setUserId(int userId) {
        this.userId = userId;
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
                this.getUserId() == user.getUserId() &&
                this.getFirstName().equals(user.getFirstName()) &&
                this.getLastName().equals(user.getLastName());
    }
    @Override
    public int compareTo(User o) {
        User user = (User) o;

        Integer thisId = this.getUserId();
        Integer userId = user.getUserId();

        return thisId.compareTo(userId);
    }
    @Override
    public String toString() {
        String string = "";

        string += "User type: " + getType();
        string += "\nUser ID: " + getUserId();
        string += "\nUser Name: " + getFirstName() + " " + getLastName();

        return string;
    }


}

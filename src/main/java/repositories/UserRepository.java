package repositories;

import models.users.User;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class UserRepository implements Repository<User> {

    //instance variables
    private SQLDatabase repository;

    //constructor
    public UserRepository() {
        repository = SQLDatabase.getInstance();
    }


    @Override
    public void insert(User o) {
        String string = "";

        string += "INSERT INTO users";
        string += "\n(type,firstName,lastName)";
        string += "\nVALUES(";
        string += String.format("'%s','%s','%s'", o.getType(), o.getFirstName(), o.getLastName());
        string+=""
    }

    @Override
    public User get(int id) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public void update(User o) {

    }

    @Override
    public void delete(User o) {

    }
}

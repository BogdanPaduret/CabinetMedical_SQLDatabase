package repositories;

import exceptions.NoUserTypeException;
import exceptions.UserDoesNotExistException;
import helpers.Utils;
import models.users.Doctor;
import models.users.Patient;
import models.users.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static helpers.Constants.USERS_TABLE_NAME;
import static helpers.Utils.*;

public class UserRepository extends Repository<User> {

    //constructor
    public UserRepository() {
        super();
    }
    public UserRepository(String JdbcURL, String userName, String password) {
        super(JdbcURL, userName, password);
    }

    @Override
    public void insert(User o) {
        String string = "";

        string += "INSERT INTO " + USERS_TABLE_NAME + "(type,firstName,lastName)";
        string += String.format("\nVALUES('%s','%s','%s')", o.getType(), o.getFirstName(), o.getLastName());

        executeStatement(string);
    }

    @Override
    public User get(int id) {
        String string = Utils.querySelect(USERS_TABLE_NAME);

        string += String.format("\nWHERE id = %d", id);

        executeStatement(string);

        User user = null;

        try {
            ResultSet set = getStatement().getResultSet();
            while (set.next()) {
                User buffer = getFromSet(set);
                if (buffer.getId() == id) {
                    user = buffer;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;

//        if (user != null) {
//            return user;
//        } else {
//            throw new UserDoesNotExistException("User ID not found");
//        }
    }

    @Override
    public List<User> getAll() {
        String string = querySelect(USERS_TABLE_NAME);

        executeStatement(string);

        List<User> users = new ArrayList<>();

        try {
            ResultSet set = getStatement().getResultSet();
            while (set.next()) {
                users.add(getFromSet(set));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (users.size() == 0) {
            users = null;
        }

        return users;
    }
    public <T extends User> List<T> getAll(String type) {
        T objType = null;

        try {
            objType = getCastingUser(type);
        } catch (NoUserTypeException e) {
            e.printStackTrace();
        }

        List<T> users = new ArrayList<>();

        if (objType != null) {
            String string = querySelect(USERS_TABLE_NAME);
            string += String.format("\nWHERE type = '%s'", objType.getClass().getSimpleName());

            executeStatement(string);

            try {
                ResultSet set = getStatement().getResultSet();
                while (set.next()) {
                    try {
                        users.add((T) getFromSet(set));
                    } catch (ClassCastException e) {
                        //do nothing
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        if (objType == null || users.size() == 0) {
            users = null;
        }

        return users;
    }

    @Override
    public void update(User o) {
        String string = "";

        string += "UPDATE " + USERS_TABLE_NAME;
        string += String.format("SET firstName = '%s', lastname = '%s'", o.getFirstName(), o.getLastName());
        string += String.format("\nWHERE id = '%d'", o.getId());

        executeStatement(string);
    }

    @Override
    public void delete(User o) {

    }

    @Override
    public void delete(int id) {

    }

    //helpers
    private User getFromSet(ResultSet set) throws SQLException {
        String type = set.getString(1);
        int id = set.getInt(2);
        String firstName = set.getString(3);
        String lastName = set.getString(4);

        return getNewUser(type, id, firstName, lastName);
    }

}

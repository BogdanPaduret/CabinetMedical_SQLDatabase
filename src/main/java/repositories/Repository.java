package repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class Repository<T> {

    //instance variables
    private static String JdbcURL;
    private static String userName;
    private static String password;
    private static Connection connection = null;
    private static Statement statement = null;

    //constructor
    public Repository(String JdbcURL, String userName, String password) {
        this.JdbcURL = JdbcURL;
        this.userName = userName;
        this.password = password;

        try {
            connection = DriverManager.getConnection(JdbcURL, userName, password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Repository() {
        this(
                "jdbc:mysql://localhost:3306/cabinetmedical?" + "autoReconnect=true&useSSL=false",
                "root",
                "root"
        );
    }

    //statements
    protected void executeStatement(String execute) {
        try {
            statement.execute(execute);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //getter
    public String getJdbcURL() {
        return JdbcURL;
    }
    public String getUserName() {
        return userName;
    }
    public String getPassword() {
        return password;
    }

    public Statement getStatement() {
        return statement;
    }

    //setter
    public void setJdbcURL(String jdbcURL) {
        JdbcURL = jdbcURL;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    //implement methods
    //create
    public abstract void insert(T obj);
    //read
    public abstract T get(int id);
    public abstract int get(T obj);
    public abstract List<T> getAll();
    //update
    public abstract void update(T obj);
    //delete
    public abstract void delete(T obj);
    public abstract void delete(int id);
}

package repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLDatabase {

    //instance variables
    private static SQLDatabase database;
    private static String databaseName;

    private static String JdbcURL;
    private static String userName;
    private static String password;
    private static Connection connection = null;
    private static Statement statement = null;

    //constructor
    private SQLDatabase(String JdbcURL, String userName, String password) {
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
    private SQLDatabase() {
        this(
                "jdbc:mysql://localhost:3306/?" + databaseName + "autoReconnect=true&useSSL=false",
                "root",
                "root"
        );
    }

    //create
    public static void changeInstance(String JdbcURL, String userName, String password) {
        database = new SQLDatabase(JdbcURL, userName, password);
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
    public final static SQLDatabase getInstance() {
        if (database == null) {
            database = new SQLDatabase();
        }
        return database;
    }

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

    public final void setDatabase(String databaseName) {
        this.databaseName = databaseName;
        String string = "";
        string += "USE " + this.databaseName;
        if (database != null) {
            executeStatement(string);
        }
    }
}

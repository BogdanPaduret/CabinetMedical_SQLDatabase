package repositories;

public final class RepositoryLoad<T> {


    //instance variables
    private static String[] databaseCredentials = {
            "jdbc:mysql://localhost:3306/?" + "autoReconnect=true&useSSL=false",
            "root",
            "root"
    };
    public static UserRepository userRepository;
    public static AppointmentRepository appointmentRepository;


    //constructor
    private RepositoryLoad() {

    }


    //initialize repositories
    public static void init(String[] databaseCredentials) {
        RepositoryLoad.databaseCredentials = databaseCredentials;

        userRepository = new UserRepository(
                databaseCredentials[0],
                databaseCredentials[1],
                databaseCredentials[2]
        );

        appointmentRepository = new AppointmentRepository(
                databaseCredentials[0],
                databaseCredentials[1],
                databaseCredentials[2]
        );
    }
}

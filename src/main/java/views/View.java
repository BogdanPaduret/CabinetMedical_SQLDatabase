package views;

import repositories.RepositoryLoad;

public interface View {

    default void init(String databaseName) {
        String[] credentials = {
                "jdbc:mysql://localhost:3306/" + databaseName + "?" + "autoReconnect=true&useSSL=false",
                "root",
                "root"
        };

        RepositoryLoad.init(credentials);
    }

    void play();

}

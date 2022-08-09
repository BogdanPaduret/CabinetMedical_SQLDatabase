import helpers.Utils;
import repositories.RepositoryLoad;
import views.ViewLogIn;

public class Main {
    public static void main(String[] args) {
        viewLogInTest();
    }

    public static void viewLogInTest() {
        String databaseName = "testCabinetMedical";
        ViewLogIn viewLogIn = new ViewLogIn(databaseName);
        RepositoryLoad.userRepository.clear();
        Utils.fillRepository(Utils.generateRandomUsers(20, false), RepositoryLoad.userRepository);
        viewLogIn.play();
    }
}

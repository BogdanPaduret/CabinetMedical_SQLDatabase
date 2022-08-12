import helpers.Utils;
import models.appointments.Appointment;
import repositories.Repository;
import repositories.RepositoryLoad;
import views.ViewLogIn;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        viewLogInTest();
//        methodNamesTest();
    }

    public static void viewLogInTest() {
        String databaseName = "testCabinetMedical";
        ViewLogIn viewLogIn = new ViewLogIn(databaseName);
        RepositoryLoad.appointmentRepository.clear();
        RepositoryLoad.userRepository.clear();
        Utils.fillRepository(Utils.generateRandomUniqueUsers(200, false), RepositoryLoad.userRepository);
        viewLogIn.play();
    }

    public static void methodNamesTest() {
        Method[] allMethods = Appointment.class.getDeclaredMethods();
        List<Method> setters = new ArrayList<Method>();
        for (Method method : allMethods) {
            if (method.getName().startsWith("set")) {
                setters.add(method);
            }
        }
//        setters.sort(new Comparator<Method>() {
//            @Override
//            public int compare(Method o1, Method o2) {
//                return o1.getName().compareTo(o2.getName());
//            }
//        });

        for (Method method : setters) {
            System.out.println(method.getName());
        }
    }
}

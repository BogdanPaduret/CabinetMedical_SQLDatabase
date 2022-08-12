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
//        toSQLStringArrayTest();
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

    public static void toSQLStringArrayTest() {
        Integer[] ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        String[] strings = {"ala", "bala", "portocala"};
        String[] int2Strings = toSQLStringArray(ints, "%d");
        String[] strings2Strings = toSQLStringArray(strings, "'%s'");
        String[][] arrays = {int2Strings, strings2Strings};
        for (String[] a : arrays) {
            System.out.println("\n\n");
            for (String string : a) {
                System.out.println(string);
            }
        }
    }
    private static <T> String[] toSQLStringArray(T[] data, String format) {
        String[] stringValues = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            stringValues[i] = String.format(format, data[i]);
        }
        return stringValues;
    }


    /*
    todo
        care-i cea mai rapida programare intr-o zi
            mai intai in java o lista cu locuri libere intr-o zi (se poate prelua din SQL direct o lista sortata cu toate programarile)
        care-i slotul cel mai mare la un anumit doctor
        ce doctori sunt disponibili intr-o anumita perioada
     */
}

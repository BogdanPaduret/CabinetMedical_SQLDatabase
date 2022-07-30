package repositories;

import models.users.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static helpers.Constants.*;
import static helpers.Utils.*;
import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {


    //helpers
    private void clearRepository() {
        String[] strings = {
                "DELETE FROM " + USERS_TABLE_NAME,
                "ALTER TABLE " + USERS_TABLE_NAME + " AUTO_INCREMENT = 0"
        };

        for (int i = 0; i < strings.length; i++) {
            repository.executeStatement(strings[i]);
        }
    }
    private List<User> generateRandomUsers(int howMany, boolean withId) {
        String[] types = USERS_ARRAY;
        String[] firstNames = {
                "Andrei",
                "Bogdan",
                "Dumitru",
                "Vlad",
                "Costel",
                "Vasile",
                "Ana",
                "Dana",
                "Victor",
                "Maria",
                "Bianca",
                "Georgeta",
                "Georgiana",
                "Victoria",
                "Angela",
                "Cristian"
        };
        String[] lastNames = {
                "Dumitriu",
                "Vasilescu",
                "Neculce",
                "Aciobanesei",
                "Pietraru",
                "Carstescu",
                "Mircea",
                "Atudoresei"
        };

        List<User> users = new ArrayList<>();

        int coefficient = 0;

        int c = 1;

        for (int i = 0; i < howMany; i++) {
            String type = types[(int) Math.floor(Math.random() * types.length + coefficient)];
            String firstName = firstNames[(int) Math.floor(Math.random() * firstNames.length + coefficient)];
            String lastName = lastNames[(int) Math.floor(Math.random() * lastNames.length + coefficient)];
            User user;
            if (withId == true) {
                user = getNewUser(type, c, firstName, lastName);
            } else {
                user = getNewUser(type, firstName, lastName);
            }
            if (user != null) {
                users.add(user);
                c++;
            }
        }

        return users;
    }
    private void fillRepository(List<User> list) {
        for (User user : list) {
            repository.insert(user);
        }
    }
    private int getRandomNumber(int min, int max) {
        return (int) Math.floor(Math.random() * (max + 1 - min)) + min;
    }


    //instance variables
    UserRepository repository;
    String databaseName = "testCabinetMedical";


    //tests
    @BeforeEach
    void setUp() {
        repository = new UserRepository(
                "jdbc:mysql://localhost:3306/" + databaseName + "?" + "autoReconnect=true&useSSL=false",
                "root",
                "root"
        );
        clearRepository();
    }
    @AfterEach
    void tearDown() {
//        clearRepository();
    }

    @Test
    void insertTest() {
        String[] types = USERS_ARRAY;
        String[] firstNames = {
                "Andrei",
                "Bogdan",
                "Costel"
        };
        String[] lastNames = {
                "Vasilescu",
                "Petrescu",
                "Popescu"
        };

        assertEquals(types.length, firstNames.length, lastNames.length);

        for (int i = 0; i < types.length; i++) {
            User user = getNewUser(types[i], firstNames[i], lastNames[i]);
            if (user != null) {
                repository.insert(user);
            }
        }

        assertEquals(3, repository.getAll().size());
    }
    @Test
    void getTest() {
        int n = (int) Math.floor(Math.random() * 100);
        List<User> users = generateRandomUsers(n, true);
        fillRepository(users);
        for (User user : users) {
            int userId = user.getId();
            User buffer = repository.get(userId);
            assertEquals(user, buffer);
        }
    }

    @Test
    void getAllTest() {
        int n = getRandomNumber(3,6);
        List<User> users = generateRandomUsers(n, false);
        fillRepository(users);
        List<User> loadedUsers = repository.getAll();
        assertEquals(users.size(), loadedUsers.size());

        List<Doctor> doctors = repository.getAll(USER_DOCTOR);
        List<Patient> patients = repository.getAll(USER_PATIENT);
        List<Secretary> secretaries = repository.getAll(USER_SECRETARY);
        List<User> errorList = repository.getAll("user");

        for (User user : loadedUsers) {
            System.out.println(user);
        }
        System.out.println("===========DOCTORS============");
        if (doctors != null) {
            for (Doctor doctor : doctors) {
                System.out.println(doctor);
            }
        }
        System.out.println("===========PATIENTS===========");
        if (patients != null) {
            for (Patient patient : patients) {
                System.out.println(patient);
            }
        }
        System.out.println("============SECRETARIES==========");
        if (secretaries != null) {
            for (Secretary secretary : secretaries) {
                System.out.println(secretary);
            }
        }
    }

    @Test
    void testGetAllTest() {
    }

    @Test
    void updateTest() {
    }

    @Test
    void deleteTest() {
    }

}
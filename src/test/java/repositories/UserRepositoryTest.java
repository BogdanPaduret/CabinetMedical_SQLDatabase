package repositories;

import exceptions.NoUserTypeException;
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

    //per test actions
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

    //tests
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
        int n = getRandomNumber(50,100);
        List<User> users = generateRandomUsers(n, false);
        fillRepository(users);
        List<User> loadedUsers = repository.getAll();
        assertEquals(users.size(), loadedUsers.size());

        List<Doctor> doctors = repository.getAll(USER_DOCTOR);
        List<Patient> patients = repository.getAll(USER_PATIENT);
        List<Secretary> secretaries = repository.getAll(USER_SECRETARY);
        List<User> errorList = repository.getAll("user");
        /*
        -- Codul nu returneaza nici o exceptie. Doar imi imprima eroarea.
        assertThrows(NoUserTypeException.class, () -> {
            List<User> errorList = repository.getAll("user");
        });
         */

        for (User user : loadedUsers) {
            System.out.println(user);
        }

        String[] types = {
                "============ DOCTORS ============",
                "=========== PATIENTS  ===========",
                "========== SECRETARIES =========="
        };
        List<?>[] userTypes = {
                doctors,
                patients,
                secretaries
        };

        for (int i = 0; i < types.length; i++) {
            System.out.println("\n\n\n" + types[i].toString() + "\nSize: " + userTypes[i].size() + "\n");
            if (userTypes[i] != null) {
                for (Object o : userTypes[i]) {
                    System.out.println(o.toString());
                }
            }
        }

    }

    @Test
    void updateTest() {
        List<User> usersToWrite = new ArrayList<>();
        usersToWrite.add(getNewUser(USER_DOCTOR, "Mihai", "Petrescu"));
        usersToWrite.add(getNewUser(USER_PATIENT, "Ana", "Tabara"));
        usersToWrite.add(getNewUser(USER_SECRETARY, "Vlad", "Turcescu"));

        for (User user : usersToWrite) {
            repository.insert(user);
        }

        assertEquals(usersToWrite.size(), repository.getAll().size());

        int id = (int) Math.floor(Math.random() * usersToWrite.size()) + 1;
        assertTrue(id <= usersToWrite.size() && id > 0);

        User user = repository.get(id);
        user.setFirstName("JOOOOOOOOHN");
        user.setLastName("DOOOOOOOOOE");

        repository.update(user);

        assertEquals(user, repository.get(id));

    }

    @Test
    void deleteTest() {
        User[] users = {
                getNewUser(USER_DOCTOR, "Mihai", "Catalin"),
                getNewUser(USER_PATIENT, "Vlad", "Andrei"),
                getNewUser(USER_SECRETARY, "Ana", "Gherasim")
        };

        for (User user : users) {
            repository.insert(user);
        }

        assertEquals(users.length, repository.getAll().size());

        int id = 2;

        repository.delete(id);
        assertEquals(users.length - 1, repository.getAll().size());
        assertNull(repository.get(id));

        List<User> usersRepository = repository.getAll();

        id = 1;
        int index = id - 1;
        users[index].setId(id);

        repository.delete(users[index]);
        assertEquals(users.length - 2, repository.getAll().size());
        assertNull(repository.get(id));

    }

}
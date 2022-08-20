package repositories;

import models.appointments.Appointment;
import models.users.Doctor;
import models.users.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.print.Doc;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static helpers.Constants.*;
import static helpers.Utils.getNewAppointment;
import static helpers.Utils.getNewUser;
import static org.junit.jupiter.api.Assertions.*;

class AppointmentRepositoryTest {

    //helpers
    private void clearRepositories() {
        appointmentRepository.clear();
        userRepository.clear();
    }


    //instance variables
    AppointmentRepository appointmentRepository;
    UserRepository userRepository;
    String databaseName = "testCabinetMedical";


    @BeforeEach
    void setUp() {

        appointmentRepository = new AppointmentRepository(
                "jdbc:mysql://localhost:3306/" + databaseName + "?" + "autoReconnect=true&useSSL=false",
                "root",
                "root"
        );

        userRepository = new UserRepository(
                "jdbc:mysql://localhost:3306/" + databaseName + "?" + "autoReconnect=true&useSSL=false",
                "root",
                "root"
        );

        clearRepositories();

    }

    @AfterEach
    void tearDown() {
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
                userRepository.insert(user);
            }
        }

        assertEquals(3, userRepository.getAll().size());

        appointmentRepository.insert(new Appointment[]{
                getNewAppointment(1, 2,
                        LocalDateTime.of(2022, 9, 20, 18, 30),
                        LocalDateTime.of(2022, 9, 20, 19, 0)),
                getNewAppointment(1, 2,
                        LocalDateTime.of(2022, 9, 21, 18, 30),
                        LocalDateTime.of(2022, 9, 21, 19, 0)),
                getNewAppointment(1, 2,
                        LocalDateTime.of(2022, 9, 22, 18, 30),
                        LocalDateTime.of(2022, 9, 22, 19, 0)),
        });

    }

    @Test
    void insertFromArrayTest() {
    }

    @Test
    void getTest() {
    }

    @Test
    void getAllTest() {
    }

    @Test
    void updateTest() {
    }

    @Test
    void deleteTest() {
    }


    @Test
    void mapTest() {
        Map<Doctor, List<Appointment>> freeSlots = new TreeMap<>();
//        Doctor doctor = new Doctor();
//        List<Appointment> appointments=freeSlots.get()
    }
}
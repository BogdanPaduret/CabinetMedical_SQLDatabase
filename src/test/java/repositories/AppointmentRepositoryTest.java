package repositories;

import models.appointments.Appointment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentRepositoryTest {


    //helpers
    private void clearRepository() {

    }
    private List<Appointment> generateRandomAppointments(int howMany, boolean withId) {
        int[] doctorIds = new int[howMany];
        int[] patientIds = new int[howMany];
        String[] startDates = new String[howMany];
        String[] endDates = new String[howMany];

        int[][] idList = {doctorIds, patientIds};

        for (int[] ids : idList) {
            int c = 1;
            for (int i = 0; i < ids.length; i++) {
                ids[i] = c;
                c++;
            }
        }


        return null;
    }
    private void fillRepository(List<Appointment> list) {

    }
    private int getRandomNumber(int min, int max) {
        return (int) Math.floor(Math.random() * (max + 1 - min)) + min;
    }
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void insertTest() {
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

}
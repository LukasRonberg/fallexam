package test.dao;

import dat.config.HibernateConfig;
import dat.daos.impl.GuideDAO;
import dat.dtos.GuideDTO;
import dat.entities.Guide;
import org.junit.jupiter.api.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GuideDAOTest {

    private static EntityManagerFactory emf;
    private GuideDAO guideDAO;
/*
    @BeforeAll
    public void setUp() {
        emf = HibernateConfig.getEntityManagerFactoryForTest(); // "test-pu" must be defined in persistence.xml
        guideDAO = GuideDAO.getInstance(emf);

        // Prepare test data
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Create test appointments
            Guide guide1 = new Guide("John Doe", LocalDate.of(2024, 10, 15), LocalTime.of(10, 30), "General Check-up");
            Guide guide2 = new Guide("Jane Smith", LocalDate.of(2024, 11, 20), LocalTime.of(14, 0), "Dental Cleaning");

            // Persist test appointments
            em.persist(guide1);
            em.persist(guide2);

            em.getTransaction().commit();
        }
    }

    @Test
    public void testCreateAppointment() {
        GuideDTO newGuideDTO = new GuideDTO("Michael Lee", LocalDate.of(2024, 12, 1), LocalTime.of(9, 0), "Consultation");
        GuideDTO createdAppointment = guideDAO.create(newGuideDTO);

        assertNotNull(createdAppointment);
        assertEquals("Michael Lee", createdAppointment.getClientName());
    }

    @Test
    public void testReadAppointment() {
        GuideDTO guideDTO = guideDAO.read(1);
        assertNotNull(guideDTO);
        assertEquals("John Doe", guideDTO.getClientName());
    }

    @Test
    public void testUpdateAppointment() {
        GuideDTO updatedAppointmentInfo = new GuideDTO();
        updatedAppointmentInfo.setClientName("Johnathan Doe");
        updatedAppointmentInfo.setDate(LocalDate.of(2024, 10, 15));
        updatedAppointmentInfo.setTime(LocalTime.of(11, 0));
        updatedAppointmentInfo.setComment("Updated Check-up");

        GuideDTO updatedAppointment = guideDAO.update(1, updatedAppointmentInfo);
        assertNotNull(updatedAppointment);
        assertEquals("Johnathan Doe", updatedAppointment.getClientName());
        assertEquals("Updated Check-up", updatedAppointment.getComment());
    }

    @Test
    public void testReadAllAppointments() {
        List<GuideDTO> appointments = guideDAO.readAll();
        assertNotNull(appointments);
        assertFalse(appointments.isEmpty());
    }

    @Test
    public void testDeleteAppointment() {
        GuideDTO guideDTO = guideDAO.read(2);
        assertNotNull(guideDTO);
        guideDAO.delete(2);
        guideDTO = guideDAO.read(2);
        assertNull(guideDTO);
    }

    @Test
    public void testValidatePrimaryKey() {
        boolean exists = guideDAO.validatePrimaryKey(1);
        assertTrue(exists);

        boolean nonExistent = guideDAO.validatePrimaryKey(999);
        assertFalse(nonExistent);
    }

    @AfterAll
    public void tearDown() {
        if (emf != null) {
            emf.close();
        }
    }*/
}

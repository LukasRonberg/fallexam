package test.dao;

import dat.config.HibernateConfig;
import dat.daos.impl.TripDAO;
import dat.dtos.TripDTO;
import dat.entities.Guide;
import dat.entities.Trip;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Trip DAO Test class
 * Purpose: To test DAO class methods for the TripDAO class
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TripDAOTest {

    private static EntityManagerFactory emf;
    private TripDAO tripDAO;

    /**
     * Instantiates the emf and tripDAO instances
     */
    @BeforeAll
    public void setUp() {
        emf = HibernateConfig.getEntityManagerFactoryForTest(); // "test-pu" must be defined in persistence.xml
        tripDAO = TripDAO.getInstance(emf);
    }

    /**
     * Before each method is tested, this sets up the test data for it
     */
    @BeforeEach
    public void setUpTestData() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Check if the guide already exists
            TypedQuery<Guide> query = em.createQuery("SELECT g FROM Guide g WHERE g.email = :email", Guide.class);
            query.setParameter("email", "john.doe@example.com");
            List<Guide> existingGuides = query.getResultList();

            Guide guide;
            if (existingGuides.isEmpty()) {
                // Create and persist a new test guide only if it does not exist
                guide = new Guide("John", "Doe", "john.doe@example.com", "123-456-7890", 5);
                em.persist(guide);
            } else {
                // Use the existing guide
                guide = existingGuides.get(0);
            }
            // Create test trips with a reference to the guide
            Trip trip1 = new Trip("Trip to Central City", LocalDate.of(2024, 4, 12), LocalDate.of(2024, 4, 20), 123.456, 78.910, 250.0, Trip.Category.CITY);
            trip1.setGuide(guide); // Associate the guide with the trip

            Trip trip2 = new Trip("Forest Retreat", LocalDate.of(2024, 8, 23), LocalDate.of(2024, 8, 30), 111.222, 33.444, 300.0, Trip.Category.FOREST);
            trip2.setGuide(guide); // Associate the guide with the trip

            // Persist test trips
            em.persist(trip1);
            em.persist(trip2);

            em.getTransaction().commit();
        }
    }


    @Test
    public void testCreateTrip() {


        // Create a new TripDTO and set the guide
        TripDTO newTripDTO = new TripDTO("Beach Getaway", LocalDate.of(2024, 6, 15), LocalDate.of(2024, 6, 22), 98.765, 43.210, 150.0, Trip.Category.BEACH);
        //newTripDTO.setGuideId(guide.getId()); // Assuming you have a method to set the guide ID

        // Create the trip using the guide
        TripDTO createdTrip = tripDAO.create(newTripDTO);

        assertNotNull(createdTrip);
        assertEquals("Beach Getaway", createdTrip.getName());
    }



    @Test
    public void testReadTrip() {
        TripDTO tripDTO = tripDAO.read(1);
        assertNotNull(tripDTO);
        assertEquals("Trip to Central City", tripDTO.getName());
    }

    @Test
    public void testUpdateTrip() {
        TripDTO updatedTripInfo = new TripDTO();
        updatedTripInfo.setName("Trip to Central City - Updated");
        updatedTripInfo.setStartTime(LocalDate.of(2024, 4, 15));
        updatedTripInfo.setEndTime(LocalDate.of(2024, 4, 25));
        updatedTripInfo.setLongitude(123.456);
        updatedTripInfo.setLatitude(78.910);
        updatedTripInfo.setPrice(300.0);
        updatedTripInfo.setCategory(Trip.Category.CITY);

        TripDTO updatedTrip = tripDAO.update(1, updatedTripInfo);
        assertNotNull(updatedTrip);
        assertEquals("Trip to Central City - Updated", updatedTrip.getName());
    }

    @Test
    public void testReadAllTrips() {
        List<TripDTO> trips = tripDAO.readAll();
        assertNotNull(trips);
        assertFalse(trips.isEmpty());
    }



    @Test
    public void testDeleteTrip() {
        TripDTO tripDTO = tripDAO.read(2);
        assertNotNull(tripDTO);
        tripDAO.delete(2);
        tripDTO = tripDAO.read(2);
        assertNull(tripDTO);
    }

    @Test
    public void testValidatePrimaryKey() {
        boolean exists = tripDAO.validatePrimaryKey(1);
        assertTrue(exists);

        boolean nonExistent = tripDAO.validatePrimaryKey(999);
        assertFalse(nonExistent);
    }

    @Test
    public void testAddGuideToTrip() {
        // Create and persist a test guide
        Guide guide = new Guide("Test", "Guide", "test.guide@example.com", "128-456-7890", 5);
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(guide);
            em.getTransaction().commit();
        }

        // Create a test trip without a guide
        Trip trip = new Trip("Test Trip", LocalDate.of(2024, 6, 15), LocalDate.of(2024, 6, 22), 98.765, 43.210, 150.0, Trip.Category.BEACH);

        // Persist the trip
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(trip);
            em.getTransaction().commit();
        }

        // Add guide to trip
        tripDAO.addGuideToTrip(trip.getId(), Math.toIntExact(guide.getId()));

        // Verify that the guide is associated with the trip
        try (EntityManager em = emf.createEntityManager()) {
            Trip updatedTrip = em.find(Trip.class, trip.getId());
            assertNotNull(updatedTrip.getGuide());
            assertEquals(guide.getId(), updatedTrip.getGuide().getId());
        }
    }

    @Test
    public void testGetTripsByGuide() {
        // Create and persist a test guide
        Guide guide = new Guide("Test", "Guide", "test.guide@example.com", "128-456-7890", 5);
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(guide);
            em.getTransaction().commit();
        }

        // Create and persist two test trips without a guide
        Trip trip1 = new Trip("Test Trip 1", LocalDate.of(2024, 6, 15), LocalDate.of(2024, 6, 22), 98.765, 43.210, 150.0, Trip.Category.BEACH);
        Trip trip2 = new Trip("Test Trip 2", LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 10), 98.765, 43.210, 250.0, Trip.Category.SEA);

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(trip1);
            em.persist(trip2);
            em.getTransaction().commit();
        }

        // Add both trips to the guide
        tripDAO.addGuideToTrip(trip1.getId(), Math.toIntExact(guide.getId()));
        tripDAO.addGuideToTrip(trip2.getId(), Math.toIntExact(guide.getId()));

        // Retrieve trips by guide
        Set<TripDTO> tripsByGuide = tripDAO.getTripsByGuide(Math.toIntExact(guide.getId()));

        // Verify the trips are correctly retrieved
        assertNotNull(tripsByGuide);
        assertEquals(2, tripsByGuide.size());
        assertTrue(tripsByGuide.stream().anyMatch(tripDTO -> tripDTO.getName().equals("Test Trip 1")));
        assertTrue(tripsByGuide.stream().anyMatch(tripDTO -> tripDTO.getName().equals("Test Trip 2")));
    }





    /**
     * After each test is run, this cleans up the test data
     */
    @AfterEach
    public void cleanUpData() {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            // First, delete trips that reference guides
            em.createQuery("DELETE FROM Trip").executeUpdate(); // Delete trips first
            em.createQuery("DELETE FROM Guide").executeUpdate(); // Then delete guides
            em.createNativeQuery("ALTER SEQUENCE guide_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE trip_trip_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * After all tests are run, this closes down the emf
     */
    @AfterAll
    public void tearDown() {
        if (emf != null) {
            emf.close();
        }
    }
}


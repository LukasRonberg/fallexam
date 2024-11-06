package test.routes;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.daos.impl.TripDAO;
import dat.daos.impl.UserDAO;
import dat.dtos.TripDTO;
import dat.entities.Guide;
import dat.entities.Trip;
import dat.security.controllers.SecurityController;
import dat.security.daos.SecurityDAO;
import dat.security.exceptions.ValidationException;
import dk.bugelhartmann.UserDTO;
import io.javalin.Javalin;
import io.restassured.common.mapper.TypeRef;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.containsInAnyOrder;

/**
 * Doctor Route Test class
 * Purpose: To test the routes we have in our DoctorRoute class
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TripRouteTest {

    private static Javalin app;
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private final static SecurityController securityController = SecurityController.getInstance();
    private final static SecurityDAO securityDAO = new SecurityDAO(emf);
    private static String BASE_URL = "http://localhost:7070/api";
    private static TripDAO tripDAO = new TripDAO(emf);
    private static UserDAO userDAO = new UserDAO(emf);
    private static Populate populator = new Populate(tripDAO, userDAO, emf);

    private static Trip trip1, trip2;
    private static Guide guide1;

    private static List<Trip> trips;

    private static UserDTO userDTO, adminDTO;
    private static String userToken, adminToken;

    /**
     * Starts the server for the tests
     */
    @BeforeAll
    void init() {
        app = ApplicationConfig.startServer(7070);
    }

    /**
     * Sets up the data we need for our tests, by using various Populate methods
     * Sets up the user and admin tokens, which is required for authorization on the endpoints
     */
    @BeforeEach
    void setUp() {
        trips = populator.createTripsAndGuides();
        trip1 = trips.get(0);
        trip2 = trips.get(1);
        guide1 = trip1.getGuide();

        UserDTO[] users = Populate.populateUsers(emf);
        userDTO = users[0];
        adminDTO = users[1];

        try {
            UserDTO verifiedUser = securityDAO.getVerifiedUser(userDTO.getUsername(), userDTO.getPassword());
            UserDTO verifiedAdmin = securityDAO.getVerifiedUser(adminDTO.getUsername(), adminDTO.getPassword());
            userToken = "Bearer " + securityController.createToken(verifiedUser);
            adminToken = "Bearer " + securityController.createToken(verifiedAdmin);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cleans up data
     */
    @AfterEach
    void tearDown() {
        populator.cleanUpData();
    }

    @Test
    void testGetTrips() {
        List<TripDTO> tripDTOS =
                given()
                        .header("Authorization", userToken)
                        .when()
                        .get(BASE_URL + "/trips")
                        .then()
                        .statusCode(200)
                        .body("size()", is(2))
                        .log().all()
                        .extract()
                        .as(new TypeRef<List<TripDTO>>() {
                        });

        assertThat(tripDTOS.size(), is(2));
        assertThat(tripDTOS.stream()
                        .map(TripDTO::getName)
                        .collect(Collectors.toList()),
                hasItems("Trip to Paris", "Trip to Miami Beach"));
    }

    @Test
    void testGetTripById() {
        int doctorId = 1;
        TripDTO tripDTO =
                given()
                        .header("Authorization", userToken)
                        .when()
                        .get(BASE_URL + "/trips/" + doctorId)
                        .then()
                        .statusCode(200)
                        .log().all()
                        .extract()
                        .as(TripDTO.class);

        assertThat(tripDTO.getName(), is("Trip to Paris"));
    }



    @Test
    void testUpdateTrip() {
        int tripId = 1;
        TripDTO updatedTrip = new TripDTO("Trip to the Beach Updated", LocalDate.of(2024, 6, 15), LocalDate.of(2024, 6, 22), 98.765, 43.210, 150.0, Trip.Category.BEACH);

        TripDTO tripDTO =
                given()
                        .header("Authorization", adminToken)
                        .contentType("application/json")
                        .body(updatedTrip)
                        .when()
                        .put(BASE_URL + "/trips/" + tripId)
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .as(TripDTO.class);

        assertThat(tripDTO.getName(), is("Trip to the Beach Updated"));
    }

    @Test
    void testDeleteTrip() {
        int doctorId = 2;

        given()
                .header("Authorization", adminToken)
                .when()
                .delete(BASE_URL + "/trips/" + doctorId)
                .then()
                .statusCode(204);
    }

    @Test
    void testGetTripsByCategory() {
        String category = "CITY";

        List<TripDTO> tripsByCategory =
                given()
                        .header("Authorization", userToken)
                        .when()
                        .get(BASE_URL + "/trips/category/" + category)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(new TypeRef<List<TripDTO>>() {});

        assertThat(tripsByCategory.stream()
                        .map(TripDTO::getName)
                        .collect(Collectors.toList()),
                hasItems("Trip to Paris"));
    }


    @Test
    void testCreateTrip() {
        TripDTO newTrip = new TripDTO("Trip to London", LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 10), 150.0, 51.5074, -0.1278, Trip.Category.CITY);

        TripDTO createdTrip =
                given()
                        .header("Authorization", adminToken)
                        .contentType("application/json")
                        .body(newTrip)
                        .when()
                        .post(BASE_URL + "/trips")
                        .then()
                        .statusCode(201)
                        .extract()
                        .as(TripDTO.class);

        assertThat(createdTrip.getName(), is("Trip to London"));
    }

}


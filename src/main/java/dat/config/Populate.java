package dat.config;


import dat.daos.impl.TripDAO;
import dat.daos.impl.UserDAO;
import dat.dtos.GuideDTO;
import dat.entities.Guide;
import dat.entities.Trip;
import dk.bugelhartmann.UserDTO;
import dat.security.entities.User;
import dat.security.entities.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Populate class
 * Purpose: To populate data for our tests
 * When main is run in Main, this class's main method is also run, which populates data we can use for our .http file
 */

public class Populate {
    private static TripDAO tripDAO;
    private static UserDAO userDAO;
    private static EntityManagerFactory emf;

    public Populate(TripDAO _tripDAO, UserDAO _userDAO, EntityManagerFactory emf) {
        this.tripDAO = _tripDAO;
        this.userDAO = _userDAO;
        this.emf = emf;
    }

    public List<Trip> createTripsAndGuides() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Create Guides first
            Guide guide1 = new Guide("Alice", "Johnson", "alice.johnson@example.com", "123-456-7890", 5);
            Guide guide2 = new Guide("Bob", "Smith", "bob.smith@example.com", "098-765-4321", 3);
            Guide guide3 = new Guide("Carol", "White", "carol.white@example.com", "555-123-4567", 7);

            // Persist guides first to avoid null reference when creating trips
            em.persist(guide1);
            em.persist(guide2);
            em.persist(guide3);

            // Create Trips and associate them with Guides
            Trip tripParis = new Trip("Trip to Paris", LocalDate.of(2024, 5, 15), LocalDate.of(2024, 5, 20),
                    2.3522, 48.8566, 1500.00, Trip.Category.CITY);
            tripParis.setGuide(guide1); // Associate with Alice

            Trip tripMiami = new Trip("Trip to Miami Beach", LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 10),
                    -80.1918, 25.7617, 2500.00, Trip.Category.BEACH);
            tripMiami.setGuide(guide2); // Associate with Bob

            // Persist trips
            em.persist(tripParis);
            em.persist(tripMiami);

            em.getTransaction().commit();

            List<Trip> trips = new ArrayList<>();
            trips.add(tripParis);
            trips.add(tripMiami);

            return trips;
        }
    }


    public static UserDTO[] populateUsers(EntityManagerFactory emf) {
        User user, admin;
        Role userRole, adminRole;

        user = new User("usertest", "user123");
        admin = new User("admintest", "admin123");
        /*userRole = new Role("USER");
        adminRole = new Role("ADMIN");
        user.addRole(userRole);
        admin.addRole(adminRole);*/

        try (var em = emf.createEntityManager())
        {
            em.getTransaction().begin();

            userRole = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                    .setParameter("name", "USER")
                    .getResultStream()
                    .findFirst()
                    .orElseGet(() -> {
                        Role newRole = new Role("USER");
                        em.persist(newRole);
                        return newRole;
                    });

            adminRole = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                    .setParameter("name", "ADMIN")
                    .getResultStream()
                    .findFirst()
                    .orElseGet(() -> {
                        Role newRole = new Role("ADMIN");
                        em.persist(newRole);
                        return newRole;
                    });

            user.addRole(userRole);
            admin.addRole(adminRole);

            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.getTransaction().commit();
        }
        UserDTO userDTO = new UserDTO(user.getUsername(), "user123");
        UserDTO adminDTO = new UserDTO(admin.getUsername(), "admin123");
        return new UserDTO[]{userDTO, adminDTO};
    }

    public void cleanUpData(){
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            // Delete rooms first to avoid foreign key constraint violations
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Trip").executeUpdate();
            em.createQuery("DELETE FROM Guide").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE trip_trip_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE guide_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
/*

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip tripParis = new Trip("Trip to Paris", LocalDate.of(2024, 5, 15),
                    LocalDate.of(2024, 5, 20),
                    2.3522, 48.8566,
                    1500.00,
                    Trip.Category.CITY);

            Trip tripMiami = new Trip("Trip to Miami Beach", LocalDate.of(2024, 6, 1),
                    LocalDate.of(2024, 6, 10),
                    -80.1918, 25.7617,
                    2500.00,
                    Trip.Category.BEACH);


            Guide guide1 = new Guide("Alison Johnson", LocalDate.of(2024, 11, 5), LocalTime.of(10, 0), "Regular check-up");
            Guide guide2 = new Guide("Bob Smith", LocalDate.of(2024, 11, 6), LocalTime.of(14, 0), "Follow-up consultation");
            Guide guide3 = new Guide("Carol White", LocalDate.of(2024, 11, 7), LocalTime.of(9, 30), "Initial consultation");
            Guide guide4 = new Guide("Benny Whiskers", LocalDate.of(2024, 11, 7), LocalTime.of(9, 30), "Initial consultation");
            Guide guide5 = new Guide("Joel Doot", LocalDate.of(2024, 11, 8), LocalTime.of(9, 30), "Initial consultation");

            guide1.setTrip(tripSmith);
            guide2.setTrip(tripTaylor);
            guide3.setTrip(tripLee);
            guide4.setTrip(tripChen);
            guide5.setTrip(tripChen);

            em.persist(tripChen);
            em.persist(tripLee);
            em.persist(tripMartinez);
            em.persist(tripSmith);
            em.persist(tripTaylor);

            em.persist(guide1);
            em.persist(guide2);
            em.persist(guide3);
            em.persist(guide4);
            em.persist(guide5);

            em.getTransaction().commit();

        }
        */
    }

    private static Set<User> getUsers() {
        User r111 = new User("Bob", "testtest", 12,62748596,"biboi5@fck.ok");
        User r112 = new User("Boblicia", "testtest", 72,62748596,"biboi4@fck.ok");

        User[] UserGroupArray = {r111, r112};
        return Set.of(UserGroupArray);
    }

}

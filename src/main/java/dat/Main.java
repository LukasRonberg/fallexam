package dat;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.daos.impl.TripDAO;
import dat.daos.impl.UserDAO;
import dat.security.controllers.SecurityController;
import dat.security.daos.SecurityDAO;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static void main(String[] args) {
        ApplicationConfig.startServer(7070);

        // Initialize EntityManagerFactory
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        // Create DAOs
        TripDAO tripDAO = new TripDAO(emf);
        UserDAO userDAO = new UserDAO(emf);

        // Instantiate Populate class
        Populate populator = new Populate(tripDAO, userDAO, emf);

        // Call methods to populate data
        //populator.createTripsAndGuides(); // Assuming this method creates trips
    }
}



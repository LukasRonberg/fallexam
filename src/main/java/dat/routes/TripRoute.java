package dat.routes;

import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.controllers.impl.TripController;
import dat.daos.impl.TripDAO;
import dat.daos.impl.UserDAO;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class TripRoute {
    // Initialize EntityManagerFactory
    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    // Create DAOs
    TripDAO tripDAO = new TripDAO(emf);
    UserDAO userDAO = new UserDAO(emf);

    // Instantiate Populate class
    Populate populator = new Populate(tripDAO, userDAO, emf);
    private final TripController tripController = new TripController(populator);


    protected EndpointGroup getRoutes() {

        return () -> {
            post("/", tripController::create, Role.ADMIN);
            get("/", tripController::readAll, Role.USER);
            get("/{id}", tripController::read, Role.USER);
            post("/add-guide", tripController::addGuideToTrip, Role.ADMIN);
            get("/guides/{guideId}/trips", tripController::getTripsByGuide, Role.USER);
            put("/{id}", tripController::update, Role.ADMIN);
            delete("/{id}", tripController::delete, Role.ADMIN);
            post("/populate", tripController::populateTripsAndGuides, Role.ADMIN);
            get("/category/{category}", tripController::getByCategory, Role.USER);
            get("/packing-items/category/{category}", tripController::getPackingItemsByCategory, Role.USER);
        };
    }


}

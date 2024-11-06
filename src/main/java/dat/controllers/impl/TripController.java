package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.controllers.IController;
import dat.daos.impl.TripDAO;
import dat.dtos.PackingItemsDTO;
import dat.dtos.TripDTO;
import dat.entities.Trip;
import dat.security.exceptions.ApiException;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TripController implements IController<TripDTO, Integer> {

    private final TripDAO dao;
    private Populate populator;


    public TripController(Populate populator) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = TripDAO.getInstance(emf);
        this.populator = populator;
    }


    public void getByCategory(Context ctx) {
        // request
        String category = ctx.pathParam("category");

        // List of all trips
        List<TripDTO> allTrips = dao.readAll(); // Assuming you have a method to get all trips

        // Use streams to filter for the requested category
        List<TripDTO> filteredTrips = allTrips.stream()
                .filter(trip -> trip.getCategory().name().equalsIgnoreCase(category))
                .collect(Collectors.toList());

        // response
        if (filteredTrips.isEmpty()) {
            throw new ApiException(404, "Trips not found - /api/trips/category/" + category);
        } else {
            ctx.res().setStatus(200);
            ctx.json(filteredTrips, TripDTO.class);
        }
    }


    public void populateTripsAndGuides(Context ctx) {
        try {
            if (populator != null) {
                // Assuming the Populate class has a method to populate trips and guides
                populator.createTripsAndGuides();
                ctx.res().setStatus(200);
                ctx.json("Trips and guides populated successfully.");
            } else {
                ctx.res().setStatus(500);
                ctx.json("Populator is not initialized.");
            }
        } catch (Exception e) {
            ctx.res().setStatus(500);
            ctx.json("An error occurred while populating trips and guides: " + e.getMessage());
        }
    }

    @Override
    public void read(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(integer -> integer > 0, "ID must be a positive integer")
                .get();
        // DTO
        TripDTO tripDTO = dao.read(id);

        // response
        if (tripDTO != null) {
            ctx.res().setStatus(200);
            ctx.json(tripDTO, TripDTO.class);
        } else {
            throw new ApiException(404, "Doctor not found - /api/doctors/" + id);
        }
    }

    public void addGuideToTrip(Context ctx) {
        // Request
        int tripId = Integer.parseInt(ctx.pathParam("tripId")); // Extract tripId from path
        int guideId = Integer.parseInt(ctx.pathParam("guideId")); // Extract guideId from path

        // Call DAO to add the guide to the trip
        try {
            dao.addGuideToTrip(tripId, guideId);
            ctx.res().setStatus(200); // OK
            ctx.json("Guide added to trip successfully.");
        } catch (ApiException e) {
            throw new ApiException(404, "Trip or Guide not found - /api/trips/" + tripId + "/guides/" + guideId);
        } catch (Exception e) {
            ctx.res().setStatus(500); // Internal Server Error
            ctx.json("An error occurred while adding the guide to the trip.");
        }
    }

    public void getTripsByGuide(Context ctx) {
        // Request
        int guideId = Integer.parseInt(ctx.pathParam("guideId")); // Extract guideId from path

        // Call DAO to get trips by guide
        Set<TripDTO> tripDTOS = dao.getTripsByGuide(guideId);

        // Response
        if (tripDTOS.isEmpty()) {
            ctx.res().setStatus(404); // Not Found
            ctx.json("No trips found for guide with ID " + guideId);
        } else {
            ctx.res().setStatus(200); // OK
            ctx.json(tripDTOS, TripDTO.class);
        }
    }



    @Override
    public void readAll(Context ctx) {
        // List of DTOS
        List<TripDTO> tripDTOS = dao.readAll();

        if (tripDTOS.isEmpty()) {
            throw new ApiException(404, "Doctors not found - /api/doctors/");
        } else {
            ctx.res().setStatus(200);
            ctx.json(tripDTOS);
        }
    }

    @Override
    public void create(Context ctx) {
        try {
            // request
            TripDTO jsonRequest = ctx.bodyAsClass(TripDTO.class);
            // DTO
            TripDTO tripDTO = dao.create(jsonRequest);
            // response
            ctx.res().setStatus(201);
            ctx.json(tripDTO, TripDTO.class);
        } catch (Exception e) {
            throw new ApiException(400, "Error creating doctor: " + e.getMessage());
        }
    }

    @Override
    public void update(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(integer -> integer > 0, "ID must be a positive integer")
                .get();
        try {
            // dto
            TripDTO tripDTO = dao.update(id, validateEntity(ctx));

            // response
            if (tripDTO != null) {
                ctx.res().setStatus(200);
                ctx.json(tripDTO, TripDTO.class);
            } else {
                throw new ApiException(404, "Doctor not found - /api/doctors/" + id);
            }
        } catch (Exception e) {
            throw new ApiException(400, "Error updating doctor: " + e.getMessage());
        }
    }

    @Override
    public void delete(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(integer -> integer > 0, "ID must be a positive integer")
                .get();
        try {
            TripDTO tripDTO = dao.read(id);
            // response
            if (tripDTO != null) {
                dao.delete(id);
                ctx.res().setStatus(204);
            } else {
                throw new ApiException(404, "Doctor not found - /api/doctors/" + id);
            }
        } catch (Exception e) {
            throw new ApiException(400, "Error deleting doctor: " + e.getMessage());
        }
    }

    @Override
    public TripDTO validateEntity(Context ctx) {
        return ctx.bodyValidator(TripDTO.class)
                .check(h -> h.getName() != null && !h.getName().isEmpty(), "Doctor name must be set")
                .get();
    }



    public void getPackingItemsByCategory(Context ctx) {
        // request
        String category = ctx.pathParam("category").toLowerCase(); // Ensure category is in lowercase

        // Validate category
        List<String> validCategories = List.of("beach", "city", "forest", "lake", "sea", "snow");
        if (!validCategories.contains(category)) {
            ctx.res().setStatus(400);
            ctx.json("Invalid category. Available categories: " + validCategories);
            return;
        }

        // Fetch packing items from external API
        try {
            String apiUrl = "https://packingapi.cphbusinessapps.dk/packinglist/" + category;
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Check response code
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                // Read the JSON response directly into a list of PackingItemDTO
                PackingItemsDTO packingItemsDTO = objectMapper.readValue(connection.getInputStream(), PackingItemsDTO.class);

                // Extract items
                List<PackingItemsDTO.PackingItem> packingItems = packingItemsDTO.getItems();

                ctx.res().setStatus(200);
                ctx.json(packingItems);
            } else {
                ctx.res().setStatus(500);
                ctx.json("Error fetching packing items: " + connection.getResponseMessage());
            }
        } catch (Exception e) {
            ctx.res().setStatus(500);
            ctx.json("An error occurred: " + e.getMessage());
        }
    }


}

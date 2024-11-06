package dat.daos.impl;

import dat.daos.IDAO;
import dat.daos.ITripGuideDAO;
import dat.dtos.TripDTO;
import dat.entities.Guide;
import dat.entities.Trip;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class TripDAO implements IDAO<TripDTO, Integer>, ITripGuideDAO {

    private static TripDAO instance;
    private static EntityManagerFactory emf;

    public TripDAO(EntityManagerFactory _emf) {
        emf = _emf;
    }

    public static synchronized TripDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TripDAO();
        }
        return instance;
    }

    public List<TripDTO> tripsByCategory(Trip.Category category) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Trip> query = em.createQuery("SELECT d FROM Trip d WHERE d.category = :speciality", Trip.class);
            query.setParameter("speciality", category);
            List<Trip> trips = query.getResultList();
            return trips.stream()
                    .map(TripDTO::new)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public TripDTO read(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            Trip trip = em.find(Trip.class, id);
            return trip != null ? new TripDTO(trip) : null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<TripDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<TripDTO> query = em.createQuery("SELECT new dat.dtos.TripDTO(t) FROM Trip t", TripDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public TripDTO create(TripDTO tripDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();


            Trip trip = new Trip(tripDTO);
            em.persist(trip);
            em.getTransaction().commit();
            return new TripDTO(trip);
        }
    }

    @Override
    public TripDTO update(Integer id, TripDTO tripDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, id);
            if (trip != null) {
                trip.setName(tripDTO.getName());
                trip.setStartTime(tripDTO.getStartTime());
                trip.setEndTime(tripDTO.getEndTime());
                trip.setLongitude(tripDTO.getLongitude());
                trip.setLatitude(tripDTO.getLatitude());
                trip.setPrice(tripDTO.getPrice());
                trip.setCategory(tripDTO.getCategory());
                em.merge(trip);
            }
            em.getTransaction().commit();
            return trip != null ? new TripDTO(trip) : null;
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, id);
            if (trip != null) {
                em.remove(trip);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Trip trip = em.find(Trip.class, id);
            return trip != null;
        }
    }

    @Override
    public void addGuideToTrip(int tripId, int guideId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, tripId);
            Guide guide = em.find(Guide.class, guideId);
            if (trip != null && guide != null) {
                trip.setGuide(guide); // Associate guide with trip
                guide.getTrips().add(trip); // Ensure bidirectional mapping, if required
                em.merge(trip);
                em.merge(guide);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public Set<TripDTO> getTripsByGuide(int guideId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Trip> query = em.createQuery(
                    "SELECT t FROM Trip t WHERE t.guide.id = :guideId", Trip.class);
            query.setParameter("guideId", guideId);
            List<Trip> trips = query.getResultList();
            return trips.stream()
                    .map(TripDTO::new)
                    .collect(Collectors.toSet());
        } finally {
            em.close();
        }
    }
}

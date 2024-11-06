package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.GuideDTO;
import dat.entities.Guide;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class GuideDAO implements IDAO<GuideDTO, Integer> {

    private static GuideDAO instance;
    private static EntityManagerFactory emf;

    public GuideDAO(EntityManagerFactory _emf) {
        emf = _emf;
    }

    public static GuideDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new GuideDAO();
        }
        return instance;
    }

    @Override
    public GuideDTO read(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Guide guide = em.find(Guide.class, id);
            em.getTransaction().commit();
            return guide != null ? new GuideDTO(guide) : null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<GuideDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<GuideDTO> query = em.createQuery(
                    "SELECT new dat.dtos.GuideDTO(a) FROM Guide a", GuideDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public GuideDTO create(GuideDTO guideDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Guide guide = new Guide(guideDTO);
            em.persist(guide);
            em.getTransaction().commit();
            return new GuideDTO(guide);
        }
    }

    @Override
    public GuideDTO update(Integer id, GuideDTO guideDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Find the existing guide by ID
            Guide existingGuide = em.find(Guide.class, id);

            // Create a new Guide instance using the updated constructor
            Guide updatedGuide = new Guide(
                    guideDTO.getFirstName(),
                    guideDTO.getLastName(),
                    guideDTO.getEmail(),
                    guideDTO.getPhone(),
                    guideDTO.getYearsOfExperience()
            );

            // Merge the updated guide into the persistence context
            updatedGuide.setId(existingGuide.getId()); // Preserve the ID of the existing guide
            Guide mergedGuide = em.merge(updatedGuide);

            em.getTransaction().commit();
            return mergedGuide != null ? new GuideDTO(mergedGuide) : null;
        }
    }


    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Guide guide = em.find(Guide.class, id);
            if (guide != null) {
                em.remove(guide);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Guide guide = em.find(Guide.class, id);
            return guide != null;
        }
    }
}

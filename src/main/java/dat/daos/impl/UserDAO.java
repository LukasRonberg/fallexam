package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.UserDTO;
import dat.security.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class UserDAO implements IDAO<UserDTO, Integer> {

    private static UserDAO instance;
    private static EntityManagerFactory emf;

    public UserDAO(EntityManagerFactory _emf){
        this.emf = _emf;
    }

    public static UserDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserDAO();
        }
        return instance;
    }

    @Override
    public UserDTO read(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            User user = em.find(User.class, integer);
            return new UserDTO(user);
        }
    }

    @Override
    public List<UserDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<UserDTO> query = em.createQuery("SELECT new dat.dtos.UserDTO(user) FROM User user", UserDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User user = new User(userDTO);
            em.persist(user);
            em.getTransaction().commit();
            return new UserDTO(user);
        }
    }

    @Override
    public UserDTO update(Integer integer, UserDTO userDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User e = em.find(User.class, integer);
            e.setUsername(userDTO.getUsername());
            e.setAge(userDTO.getAge());
            e.setPhoneNumber(userDTO.getPhoneNumber());
            e.setEmail(userDTO.getEmail());
            User mergedUser = em.merge(e);
            em.getTransaction().commit();
            return mergedUser != null ? new UserDTO(mergedUser) : null;
        }
    }

    @Override
    public void delete(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User User = em.find(User.class, integer);
            if (User != null) {
                em.remove(User);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            User User = em.find(User.class, integer);
            return User != null;
        }
    }
}

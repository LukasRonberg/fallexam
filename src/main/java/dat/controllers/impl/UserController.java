package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.UserDAO;
import dat.dtos.UserDTO;
import dat.security.entities.User;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class UserController implements IController<UserDTO, Integer> {

    private final UserDAO dao;

    public UserController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = UserDAO.getInstance(emf);
    }

    @Override
    public void read(Context ctx)  {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // DTO
        UserDTO userDAO = dao.read(id);
        // response
        ctx.res().setStatus(200);
        ctx.json(userDAO, UserDTO.class);
    }

    @Override
    public void readAll(Context ctx) {
        // List of DTOS
        List<UserDTO> userDTOs = dao.readAll();
        // response
        ctx.res().setStatus(200);
        ctx.json(userDTOs, UserDTO.class);
    }

    @Override
    public void create(Context ctx) {
        // request
        UserDTO jsonRequest = ctx.bodyAsClass(UserDTO.class);
        // DTO
        UserDTO userDTO = dao.create(jsonRequest);
        // response
        ctx.res().setStatus(201);
        ctx.json(userDTO, UserDTO.class);
    }

    @Override
    public void update(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // dto
        UserDTO userDTO = dao.update(id, validateEntity(ctx));
        // response
        ctx.res().setStatus(200);
        ctx.json(userDTO, User.class);
    }

    @Override
    public void delete(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        dao.delete(id);
        // response
        ctx.res().setStatus(204);
    }


    public boolean validatePrimaryKey(Integer integer) {
        return dao.validatePrimaryKey(integer);
    }


    @Override
    public UserDTO validateEntity(Context ctx) {
        return ctx.bodyValidator(UserDTO.class)
                .check( h -> h.getEmail() != null && !h.getEmail().isEmpty(), "Hotel address must be set")
                .get();
    }

}

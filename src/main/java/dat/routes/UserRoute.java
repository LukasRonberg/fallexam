package dat.routes;

import dat.controllers.impl.UserController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class UserRoute {

    private final UserController userController = new UserController();

    protected EndpointGroup getRoutes() {

        return () -> {
            get("/", userController::readAll, Role.USER);
            get("/{id}", userController::read, Role.USER);
            put("/{id}", userController::update, Role.ADMIN);
            delete("/{id}", userController::delete, Role.ADMIN);
        };
    }
}

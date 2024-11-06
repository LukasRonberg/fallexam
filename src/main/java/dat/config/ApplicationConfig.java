package dat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.controllers.impl.ExceptionController;
import dat.routes.Routes;
import dat.security.controllers.AccessController;
import dat.security.controllers.SecurityController;
import dat.security.enums.Role;
import dat.security.exceptions.ApiException;
import dat.security.routes.SecurityRoutes;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationConfig {

    private static Routes routes = new Routes();
    private static AccessController accessController = new AccessController();
    private static ExceptionController exceptionController = new ExceptionController();
    private static Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    public static void configuration(JavalinConfig config) {
        config.showJavalinBanner = false;
        config.bundledPlugins.enableRouteOverview("/routes", Role.ANYONE);
        config.router.contextPath = "/api"; // base path for all endpoints
        config.router.apiBuilder(routes.getRoutes());
        config.router.apiBuilder(SecurityRoutes.getSecuredRoutes());
        config.router.apiBuilder(SecurityRoutes.getSecurityRoutes());
    }

    public static Javalin startServer(int port) {
        Javalin app = Javalin.create(ApplicationConfig::configuration);

        app.beforeMatched(accessController::accessHandler);

        app.beforeMatched(ctx -> accessController.accessHandler(ctx));

        app.exception(Exception.class, (e, ctx) -> exceptionController.generalExceptionHandler(e, ctx,logger));
        app.exception(ApiException.class, (e, ctx) -> exceptionController.apiExceptionHandler(e, ctx,logger));
        app.start(port);
        return app;
    }

    public static void stopServer(Javalin app) {
        app.stop();
    }
}

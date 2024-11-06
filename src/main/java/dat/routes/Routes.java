package dat.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final TripRoute tripRoute = new TripRoute();
    private final UserRoute userRoute = new UserRoute();

    public EndpointGroup getRoutes() {
        return () -> {
                path("/trips", tripRoute.getRoutes());
                path("/user", userRoute.getRoutes());
        };
    }
}

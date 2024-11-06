package dat.controllers.impl;

import dat.security.exceptions.ApiException;
import dat.utils.Utils;
import io.javalin.http.Context;
import org.slf4j.Logger;

public class ExceptionController {
    public void generalExceptionHandler(Exception e, Context ctx, Logger LOGGER) {
        LOGGER.error("An unhandled exception occurred", e.getMessage());
        ctx.json(Utils.convertToJsonMessage(ctx, "error", e.getMessage()));
    }
    public void apiExceptionHandler(ApiException e, Context ctx, Logger LOGGER) {
        ctx.status(e.getStatusCode());
        LOGGER.warn("An API exception occurred: Code: {}, Message: {}", e.getStatusCode(), e.getMessage());

        String jsonResponse = Utils.convertToJsonMessage(ctx, "message", e.getMessage());
        ctx.json(jsonResponse);
    }

}

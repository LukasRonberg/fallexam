package dat.security.exceptions;

import dat.utils.Utils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Purpose: To handle exceptions in the API
 * Author: Thomas Hartmann
 */
public class ApiException extends RuntimeException {
    private int statusCode;
    private String timestamp;

    public ApiException (int statusCode, String msg) {
        super(msg);
        this.statusCode = statusCode;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getTimestamp() { return timestamp; }

   /* public String getErrorResponseJson() {
        return String.format(
                "{\"status\": %d, \"message\": \"%s\", \"timestamp\": \"%s\"}",
                statusCode, getMessage(), timestamp);
    }*/
}

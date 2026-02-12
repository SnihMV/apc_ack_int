package su.petrosoft.apk_ack_integration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class JsonFileException extends RuntimeException {
    public JsonFileException(String message) {
        super(message);
    }

    public JsonFileException(String message, Throwable cause) {
        super(message, cause);
    }
}


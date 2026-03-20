package su.petrosoft.apk_ack_integration.exception;

public class UpdateInstanceException extends RuntimeException {
    public UpdateInstanceException(String message) {
        super(message);
    }

    public UpdateInstanceException(String message, Throwable cause) {
        super(message, cause);
    }
}


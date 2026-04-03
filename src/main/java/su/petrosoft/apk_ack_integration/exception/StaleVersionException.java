package su.petrosoft.apk_ack_integration.exception;

public class StaleVersionException extends RuntimeException {
    public StaleVersionException(String message) {
        super(message);
    }
}

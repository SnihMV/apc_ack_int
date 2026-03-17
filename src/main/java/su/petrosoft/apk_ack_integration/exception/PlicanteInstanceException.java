package su.petrosoft.apk_ack_integration.exception;

public class PlicanteInstanceException extends RuntimeException {
    public PlicanteInstanceException(String message) {
        super(message);
    }

    public PlicanteInstanceException(String message, Throwable cause) {
        super(message, cause);
    }
}

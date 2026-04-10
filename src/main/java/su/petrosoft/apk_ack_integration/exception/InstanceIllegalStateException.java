package su.petrosoft.apk_ack_integration.exception;

public class InstanceIllegalStateException extends RuntimeException {
    public InstanceIllegalStateException(String message) {
        super(message);
    }

    public InstanceIllegalStateException(String message, Throwable cause) {
        super(message, cause);
    }
}

package su.petrosoft.apk_ack_integration.exception;

public class InvalidXmlException extends RuntimeException {
    public InvalidXmlException(String message) {
        super(message);
    }

    public InvalidXmlException(String message, Throwable cause) {
        super(message, cause);
    }
}

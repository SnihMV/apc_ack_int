package su.petrosoft.apk_ack_integration.exception;

public class DictionaryException extends RuntimeException {
    public DictionaryException(String message, Throwable cause) {
        super(message, cause);
    }

    public DictionaryException(String message) {
        super(message);
    }
}

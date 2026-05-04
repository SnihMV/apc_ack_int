package su.petrosoft.apk_ack_integration.exception;

public class OperationalReportJsonParsingException extends RuntimeException {
    public OperationalReportJsonParsingException(String message) {
        super(message);
    }

    public OperationalReportJsonParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}

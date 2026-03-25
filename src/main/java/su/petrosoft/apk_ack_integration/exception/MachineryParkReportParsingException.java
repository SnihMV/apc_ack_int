package su.petrosoft.apk_ack_integration.exception;

public class MachineryParkReportParsingException extends RuntimeException {
    public MachineryParkReportParsingException(String message) {
        super(message);
    }

    public MachineryParkReportParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}

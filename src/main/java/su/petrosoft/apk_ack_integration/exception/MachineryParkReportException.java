package su.petrosoft.apk_ack_integration.exception;

public class MachineryParkReportException extends RuntimeException {
    public MachineryParkReportException(String message) {
        super(message);
    }

    public MachineryParkReportException(String message, Throwable cause) {
        super(message, cause);
    }
}

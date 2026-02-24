package su.petrosoft.apk_ack_integration.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

@Schema(description = "Default error response")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponseDto(
        @Schema(description = "Error time (Unix timestamp ms)", example = "1771961420193")
        long timestamp,

        @Schema(description = "Http status code", example = "400")
        int status,

        @Schema(description = "Http status description", example = "Bad Request")
        String error,

        @Schema(description = "Problem details", example = "division by zero")
        String message,

        @Schema(description = "Request URL that leaded to error", example = "/api/v1/super/powerful/endpoint")
        String path
) {

    public static ErrorResponseDto of(HttpStatus status, String message, String path) {
        return new ErrorResponseDto(
                System.currentTimeMillis(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );
    }

    public static ErrorResponseDto badRequest(String message, String path) {
        return of(HttpStatus.BAD_REQUEST, message, path);
    }

    public static ErrorResponseDto notFound(String message, String path) {
        return of(HttpStatus.NOT_FOUND, message, path);
    }
}

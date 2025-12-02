package su.petrosoft.apk_ack_integration.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record FillingMainFormRequestDto(
        @Schema(description = "Crop Production Main Form Instance id", example = "1337")
        Long id,

        @Schema(description = "Crop Production Main Form Instance version", example = "0")
        Long version,

        @Schema(description = "Crop Production Main Form 'formation date' attribute value in " +
                "Epoch Unix Timestamp format", example = "1764536400000")
        Long date
) {
}

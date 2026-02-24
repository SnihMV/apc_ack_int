package su.petrosoft.apk_ack_integration.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record GetSummaryReportTypeResponseDto(
        @Schema(description = "System report type code", example = "FORM_1")
        String code,

        @Schema(description = "Display title for the report type", example = "Отчет по посевной кампании")
        String title
) {
}

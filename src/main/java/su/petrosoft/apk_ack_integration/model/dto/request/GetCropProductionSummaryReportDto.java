package su.petrosoft.apk_ack_integration.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import su.petrosoft.apk_ack_integration.model.enums.ReportType;

import java.time.LocalDate;

@Schema(description = "DTO для запроса отчета по растениеводству")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record GetCropProductionSummaryReportDto(
        @NotNull
        @JsonProperty(required = true)
        ReportType type,

        @JsonProperty(required = true)
        boolean isDetailed,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate from,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate to
)  {
}

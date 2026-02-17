package su.petrosoft.apk_ack_integration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import su.petrosoft.apk_ack_integration.model.dto.request.FillingMainFormRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.GetCropProductionSummaryReportDto;
import su.petrosoft.apk_ack_integration.model.dto.response.GetSummaryReportTypeResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.ReportType;
import su.petrosoft.apk_ack_integration.service.CropProductionService;

@RestController
@RequestMapping("api/v1/cropProduction")
@RequiredArgsConstructor
@Tag(name = "Crop Production controller",
        description = "Provides endpoints for managing instances associated with Crop Production")
public class CropProductionController {
    private final CropProductionService service;

    @Operation(
            summary = "Update main form",
            description = "Updates main form by filling fields with associated operational reports files values")
    @PatchMapping("fillMainForm")
    public void fillMainForm(@RequestBody FillingMainFormRequestDto dto) {
        service.fillMainForm(dto);
    }

    @Operation(
            summary = "Generate summary report",
            description = "Creates an excel file with a summary report on crop production based on operational" +
                    "reports accepted in the specified interval")
    @GetMapping(value = "summaryReport/excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> getSummaryReport(@RequestBody GetCropProductionSummaryReportDto dto) {

        byte[] fileContent = service.createExcelSummaryReport(dto);

        String fileName = "Crop production summary report " + LocalDate.now() + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(fileContent);
    }

    @GetMapping("summaryReport/types")
    public List<GetSummaryReportTypeResponseDto> getReportTypes() {
        return Arrays.stream(ReportType.values())
                .map(type -> new GetSummaryReportTypeResponseDto(
                        type.name(),
                        type.getTitle()))
                .toList();
    }
}

package su.petrosoft.apk_ack_integration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateInstancesFromFileResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.response.UpdateCashPlanLimitResponseDto;
import su.petrosoft.apk_ack_integration.service.CashPlanLimitService;
import su.petrosoft.apk_ack_integration.service.ExcelExtractor;

@RestController
@Slf4j
@RequestMapping("api/v1/cashPlanLimits")
@RequiredArgsConstructor
public class CashPlanLimitController {
    private final ExcelExtractor excelExtractor;
    private final CashPlanLimitService service;

    @Operation(
            summary = "Upload Excel file with Cash Plan Limits",
            description = "Upload an Excel file to create new Cash Plan Limits. " +
                    "File should contain specific columns and format.")
    @PostMapping("excel")
    @ResponseStatus(HttpStatus.OK)
    public CreateInstancesFromFileResponseDto createFromExcel(
            @Parameter(description = "Excel file with Cash Plan Limits data",
                    required = true,
                    content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            @RequestParam("file") MultipartFile file) {
        log.debug("Received file [{}] to create CashPlanLimits", file.getOriginalFilename());
        CreateInstancesFromFileResponseDto fromUniBudgetExcel = service.createFromUniBudgetExcel(file);
        return fromUniBudgetExcel;
    }

    @PatchMapping("xml")
    @ResponseStatus(HttpStatus.OK)
    public UpdateCashPlanLimitResponseDto updateByXml() {
        return service.updateByXml();
    }

    @Operation(
            summary = "Upload Excel file with Cash Plan Limits",
            description = "Upload an Excel file to update existing Cash Plan Limits. " +
                    "File should contain specific columns and format.")
    @PatchMapping("excel")
    @ResponseStatus(HttpStatus.OK)
    public UpdateCashPlanLimitResponseDto updateByExcel(
            @Parameter(description = "Excel file with Cash Plan Limits data",
                    required = true,
                    content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            @RequestParam MultipartFile file) {
        return service.updateByExcel(file);
    }
}

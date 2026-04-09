package su.petrosoft.apk_ack_integration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.model.enums.UpsertAction;
import su.petrosoft.apk_ack_integration.service.FinancingSourceService;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("api/v1/financingSources")
@RequiredArgsConstructor
public class FinancingSourceController {
    private final FinancingSourceService service;

    @Operation(
            summary = "Upload Excel file with financing sources",
            description = "Upload an Excel file to create new financing sources. " +
                    "File should contain specific columns and format.")
    @PostMapping("excel")
    @ResponseStatus(HttpStatus.OK)
    public Map<UpsertAction, Map<Long, Set<Long>>> createFinancingSources(
            @Parameter(description = "Excel file with financing sources data",
                    required = true,
                    content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            @RequestParam("file") MultipartFile file) {
        return service.createFinancingSources(file);
    }
}

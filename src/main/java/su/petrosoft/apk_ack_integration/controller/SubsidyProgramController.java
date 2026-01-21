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
import su.petrosoft.apk_ack_integration.model.excel.BudgetItemExcelRow;
import su.petrosoft.apk_ack_integration.service.BudgetItemService;
import su.petrosoft.apk_ack_integration.service.ExcelExtractor;

import java.util.List;

@RestController
@RequestMapping("api/v1/subsidyPrograms")
@RequiredArgsConstructor
public class SubsidyProgramController {
    private final ExcelExtractor excelExtractor;
    private final BudgetItemService service;

    @Operation(
            summary = "Upload Excel file with subsidy programs",
            description = "Upload an Excel file to create new subsidy programs. " +
                    "File should contain specific columns and format."
    )
    @PostMapping("excel")
    @ResponseStatus(HttpStatus.OK)
    public void uploadExcel(
            @Parameter(
                    description = "Excel file with subsidy programs data",
                    required = true,
                    content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            @RequestParam("file") MultipartFile file) {
//        List<BaseUniBudgetExcelRow> uniBudgetRows = excelExtractor.getUniBudgetCodedRows(file);
        List<BudgetItemExcelRow> uniBudgetRows = excelExtractor.getUniBudget2026ClarifiedRows(file);
        service.createSubsidyProgramsTree(uniBudgetRows);
    }
}

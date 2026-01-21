package su.petrosoft.apk_ack_integration.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateBudgetItemsResponseDto;
import su.petrosoft.apk_ack_integration.model.excel.BudgetItemExcelRow;
import su.petrosoft.apk_ack_integration.service.BudgetItemService;
import su.petrosoft.apk_ack_integration.service.ExcelExtractor;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/budgetItems")
@RequiredArgsConstructor
public class BudgetItemController {

    private final BudgetItemService service;
    private final ExcelExtractor excelExtractor;

    @PostMapping("excel")
    public CreateBudgetItemsResponseDto createBudgetItemsFromExcel(@RequestParam MultipartFile file) {
        List<BudgetItemExcelRow> uniBudgetRows = excelExtractor.getUniBudgetCodedRows(file);
        if (uniBudgetRows.isEmpty()) {
            return new CreateBudgetItemsResponseDto(Collections.emptyMap());
        }
        return service.createBudgetItems(uniBudgetRows);
    }
}

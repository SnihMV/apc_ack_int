package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.model.excel.UniBudgetExcelRow;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinancingSourceService {
    private final ExcelExtractor excelExtractor;
    private final BudgetItemService budgetItemService;

    public void createFinancingSources(MultipartFile file) {
        List<UniBudgetExcelRow> dtoList = excelExtractor.getUniBudgetRows(file);
        log.debug("Extracted from excel file: [{}] effective rows", dtoList.size());

        if (!dtoList.isEmpty()) {
            budgetItemService.createFinancingSources(dtoList);
        }
    }
}

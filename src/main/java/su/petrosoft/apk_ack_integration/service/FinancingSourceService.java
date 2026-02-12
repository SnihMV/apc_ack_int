package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.model.data.DescriptedBudgetItemData;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinancingSourceService {
    private final ExcelExtractor excelExtractor;
    private final BudgetItemService budgetItemService;

    public Map<String, Set<Long>> createFinancingSources(MultipartFile file) {
//        List<UniBudgetCodedExcelRow> dtoList = excelExtractor.getUniBudgetCodedRows(file);
//        List<DescriptedBudgetItemData> dtoList = excelExtractor.getUniBudget20262801Rows(file);
        List<DescriptedBudgetItemData> dtoList = excelExtractor.getUniBudget2026ClarifiedRows(file);

            return budgetItemService.createBudgetItems(dtoList);
    }
}

package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.excel.UniBudgetExcelRow;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubsidyProgramService {

    private final ExcelExtractor excelExtractor;
    private final BudgetItemService budgetService;

    public void createNewProgramsFromExcel(MultipartFile file) {
        List<UniBudgetExcelRow> dtoList = excelExtractor.getUniBudgetRows(file);
        log.debug("Extracted from excel file: [{}] effective rows", dtoList.size());
        Set<SubsidyProgram> subsidyProgramsTree = new LinkedHashSet<>();
        if (!dtoList.isEmpty()) {
            subsidyProgramsTree = budgetService.createSubsidyProgramsTree(dtoList);
        }
    }
}

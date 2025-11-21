package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateBudgetItemsResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateFromExcelResponseDto;
import su.petrosoft.apk_ack_integration.model.excel.UniBudgetExcelRow;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExtendedLimitService {
    private final ExcelExtractor excelExtractor;
    private final CashPlanLimitService cplService;
    private final BudgetItemService budgetItemService;

    public CreateFromExcelResponseDto createLimits(MultipartFile file) {
        List<UniBudgetExcelRow> dtoList = excelExtractor.getUniBudgetRows(file);

        CreateFromExcelResponseDto responseDto = CreateFromExcelResponseDto.builder()
                .persistedIds(Collections.emptyList())
                .build();

        if (!dtoList.isEmpty()) {
            responseDto = cplService.createFromUniBudgetExcel(dtoList);
        }
        return responseDto;
    }

    public CreateBudgetItemsResponseDto createBudgetItem(MultipartFile file) {
        List<UniBudgetExcelRow> uniBudgetRows = excelExtractor.getUniBudgetRows(file);
        if (!uniBudgetRows.isEmpty()) {
            budgetItemService.createAll(uniBudgetRows);
        }
        return new CreateBudgetItemsResponseDto(Collections.emptyMap());
    }
}

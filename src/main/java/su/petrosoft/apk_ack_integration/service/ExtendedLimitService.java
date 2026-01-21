package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateBudgetItemsResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateInstancesFromFileResponseDto;
import su.petrosoft.apk_ack_integration.model.excel.BaseUniBudgetExcelRow;
import su.petrosoft.apk_ack_integration.model.excel.UniBudgetCodedExcelRow;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExtendedLimitService {
    private final ExcelExtractor excelExtractor;
    private final CashPlanLimitService cplService;
    private final BudgetItemService budgetItemService;

    public CreateInstancesFromFileResponseDto createLimits(MultipartFile file) {
        List<BaseUniBudgetExcelRow> dtoList = excelExtractor.getUniBudgetCodedRows(file);

        CreateInstancesFromFileResponseDto responseDto = CreateInstancesFromFileResponseDto.builder()
                .persistedIds(Collections.emptyList())
                .build();

        if (!dtoList.isEmpty()) {
            responseDto = cplService.createFromUniBudgetExcel(file);
        }
        return responseDto;
    }

    public CreateBudgetItemsResponseDto createBudgetItem(MultipartFile file) {
        List<BaseUniBudgetExcelRow> uniBudgetRows = excelExtractor.getUniBudgetCodedRows(file);
        if (!uniBudgetRows.isEmpty()) {
            budgetItemService.createBudgetItems(uniBudgetRows);
        }
        return new CreateBudgetItemsResponseDto(Collections.emptyMap());
    }
}

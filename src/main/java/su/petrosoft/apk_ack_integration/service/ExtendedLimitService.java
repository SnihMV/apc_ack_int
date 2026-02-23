package su.petrosoft.apk_ack_integration.service;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.model.data.DescriptedBudgetItemData;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateBudgetItemsResponseDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExtendedLimitService {
    private final ExcelExtractor excelExtractor;
    private final CashPlanLimitService cplService;
    private final BudgetItemService budgetItemService;

//    public CreatingInstancesFromFileResponseDto createLimits(MultipartFile file) {
//        List<DescriptedBudgetItemData> dtoList = excelExtractor.getUniBudgetCodedRows(file);
//
//        CreatingInstancesFromFileResponseDto responseDto = CreatingInstancesFromFileResponseDto.builder()
//                .persistedIds(Collections.emptyList())
//                .build();
//
//        if (!dtoList.isEmpty()) {
//            responseDto = cplService.createFromUniBudgetExcel(file);
//        }
//        return responseDto;
//    }

    public CreateBudgetItemsResponseDto createBudgetItem(MultipartFile file) {
        List<DescriptedBudgetItemData> uniBudgetRows = excelExtractor.getUniBudgetCodedRows(file);
        if (!uniBudgetRows.isEmpty()) {
            budgetItemService.createNewBudgetItems(uniBudgetRows);
        }
        return new CreateBudgetItemsResponseDto(Collections.emptyMap());
    }
}

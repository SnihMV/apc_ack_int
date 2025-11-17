package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateFromExcelResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.CashPlanLimitExcelRow;
import su.petrosoft.apk_ack_integration.model.excel.UniBudgetExcelRowDto;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CashPlanLimitService {

    private final ApkPlicanteService apkService;
    private final ExcelExtractor excelExtractor;
    private final CashPlanLimitMapper mapper;

    public CreateFromExcelResponseDto createFromExcel(MultipartFile file) {

        List<CashPlanLimitExcelRow> dtoList = excelExtractor.getCashPlanLimitsRows(file);
        log.debug("Extracted from excel file: [{}] CashPlanLimit rows", dtoList.size());
        List<CashPlanLimit> createdLimits = new ArrayList<>();
        if (!dtoList.isEmpty()) {
            List<CashPlanLimit> existedLimits = apkService.getAllCashPlanLimits();
            log.debug("Found in Plicante {} CashPlanLimits in total", existedLimits.size());

            List<CashPlanLimit> limitsFromExcel = dtoList.stream()
                    .map(mapper::toCpl)
                    .collect(Collectors.toList());

            limitsFromExcel.removeAll(existedLimits);
            if (!limitsFromExcel.isEmpty()) {
                Map<CodeType, Map<Long, String>> codesMap = apkService.getCodesMap();
                limitsFromExcel.stream()
                        .map(cpl -> apkService.createCashPlanLimit(cpl, codesMap))
                        .forEach(createdLimits::add);
            }
        }
        return new CreateFromExcelResponseDto(
                dtoList.size(),
                createdLimits.size(),
                createdLimits.stream()
                        .map(CashPlanLimit::getId)
                        .toList()
        );
    }
}

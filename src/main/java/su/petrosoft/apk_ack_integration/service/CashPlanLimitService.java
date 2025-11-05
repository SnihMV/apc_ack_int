package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateFromExcelResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class CashPlanLimitService {

    private final ApkPlicanteService apkService;
    private final CashPlanLimitExtractor extractor;

    public CreateFromExcelResponseDto createFromExcel(MultipartFile file) {
        Set<CashPlanLimit> limitsFromExcel = extractor.getFromExcelUnique(file);
        log.debug("Extracted from excel {} unique CashPlanLimits", limitsFromExcel.size());

        List<CashPlanLimit> existedLimits = apkService.getAllCashPlanLimits();
        log.debug("Found in Plicante {} CashPlanLimits in total", existedLimits.size());

        ArrayList<CashPlanLimit> limitsToCreate = new ArrayList<>(limitsFromExcel);
        limitsToCreate.removeAll(existedLimits);
        log.debug("{} CashPlanLimits to be create as new", limitsToCreate.size());

        List<InstanceDto> created = apkService.createAll(limitsToCreate);
        return new CreateFromExcelResponseDto(
                limitsFromExcel.size(),
                created.size(),
                created.stream()
                        .map(InstanceDto::id)
                        .toList()
        );
    }
}

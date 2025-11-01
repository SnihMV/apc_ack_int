package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CashPlanLimitService {

    private final CashPlanLimitExtractor extractor;

    public void createFromExcel(MultipartFile file) {
        List<CashPlanLimit> cashPlanLimitList = extractor.getFromExcel(file);
        log.debug("Extracted from excel {} CashPlanLimits", cashPlanLimitList.size());
    }
}

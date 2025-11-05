package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CashPlanLimitExtractor {

    private final ExcelParser excelParser;

    public HashSet<CashPlanLimit> getFromExcelUnique(MultipartFile file) {
        try {
            List<CashPlanLimit> allLimits = excelParser.getCashPlanLimits(file);
            log.debug("Extracted {} CashPlanLimits from excel file", allLimits.size());
            return new HashSet<>(allLimits);
        } catch (IOException e) {
            log.error("Excel file reading error: {}", e.getMessage());
            throw new IllegalArgumentException("Excel file reading error: " + e.getMessage());
        }
    }
}

package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CashPlanLimitExtractor {

    private final ExcelParser excelParser;

    public List<CashPlanLimit> getFromExcel(MultipartFile file) {
        try {
            return excelParser.getCashPlanLimits(file);
        } catch (IOException e) {
            log.error("Excel file reading error: {}", e.getMessage());
            throw new IllegalArgumentException("Excel file reading error: " + e.getMessage());
        }
    }
}

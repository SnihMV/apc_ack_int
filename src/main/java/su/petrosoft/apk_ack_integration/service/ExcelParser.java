package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.CreateCashPlanLimitExcel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExcelParser {
    private final ApkPlicanteService apkService;
    private final CashPlanLimitMapper mapper;

    public List<CashPlanLimit> getCashPlanLimits(MultipartFile file) throws IOException {
        List<CashPlanLimit> limits = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            TableParams tableParams = analyzeSheetStructure(sheet);
            if (tableParams.lastRowIndex < tableParams.firstRowIndex) {
                log.debug("No one effective row in received file");
                return Collections.emptyList();
            }
            Map<CodeType, Map<Long, String>> allCodes = apkService.getAllCodes();
            for (int i = tableParams.firstRowIndex; i <= tableParams.lastRowIndex; i++) {
                Row row = sheet.getRow(i);
                CreateCashPlanLimitExcel cplExcel = mapRowToPojo(row);
                log.debug("Excel row mapped to pojo: {}", cplExcel);
                CashPlanLimit cpl = mapper.toCpl(cplExcel, allCodes);
                log.debug("Pojo mapped to CashPlanLimit: {}", cpl);
                limits.add(cpl);
            }
        }
        log.debug("");
        return limits;
    }

    private CreateCashPlanLimitExcel mapRowToPojo(Row row) {
        return CreateCashPlanLimitExcel.builder()
                .section(row.getCell(0).getStringCellValue())
                .subsection(row.getCell(1).getStringCellValue())
                .kcsr(row.getCell(2).getStringCellValue())
                .additionalKr(row.getCell(3).getStringCellValue())
                .kvr(row.getCell(4).getStringCellValue())
                .kosgu(row.getCell(5).getStringCellValue())
                .kvsr(row.getCell(6).getStringCellValue())
                .additionalFk(row.getCell(7).getStringCellValue())
                .additionalEk(row.getCell(8).getStringCellValue())
                .purposeCode(row.getCell(9).getStringCellValue())
                .assignTotal(row.getCell(10).getNumericCellValue())
                .assignFederal(row.getCell(11).getNumericCellValue())
                .assignRegional(row.getCell(12).getNumericCellValue())
                .financeTotal(row.getCell(13).getNumericCellValue())
                .requested(row.getCell(14).getNumericCellValue())
                .m01Amt(row.getCell(15).getNumericCellValue())
                .m02Amt(row.getCell(16).getNumericCellValue())
                .m03Amt(row.getCell(17).getNumericCellValue())
                .m04Amt(row.getCell(18).getNumericCellValue())
                .m05Amt(row.getCell(19).getNumericCellValue())
                .m06Amt(row.getCell(20).getNumericCellValue())
                .m07Amt(row.getCell(21).getNumericCellValue())
                .m08Amt(row.getCell(22).getNumericCellValue())
                .m09Amt(row.getCell(23).getNumericCellValue())
                .m10Amt(row.getCell(24).getNumericCellValue())
                .m11Amt(row.getCell(25).getNumericCellValue())
                .m12Amt(row.getCell(26).getNumericCellValue())
                .build();
    }

    private TableParams analyzeSheetStructure(Sheet sheet) {
        int fstRow = findHeaderRow(sheet) + 1;
        log.debug("First CashPlanLimit row index: {}", fstRow);

        if (fstRow > sheet.getLastRowNum()) {
            log.warn("No one CashPlanLimit in table");
            throw new IllegalArgumentException("No one CashPlanLimit in table");
        }
        int lstRow = findLastRow(sheet, fstRow);
        log.debug("Last CashPlanLimit row index: {}", lstRow);

        return new TableParams(fstRow, lstRow);
    }

    private int findHeaderRow(Sheet sheet) {
        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            if (sheet.getRow(i).getCell(0).getStringCellValue().equalsIgnoreCase("Раздел")) {
                log.debug("Table header row index: {}", i);
                return i;
            }
        }
        log.warn("Could not found table header");
        throw new IllegalArgumentException("Invalid excel file format");
    }

    private int findLastRow(Sheet sheet, int from) {
        for (int i = from; i <= sheet.getLastRowNum(); i++) {
            String cellValue = sheet.getRow(i).getCell(0).getStringCellValue().trim();
            if (cellValue.equalsIgnoreCase("итого") || cellValue.isBlank()) {
                log.debug("Table footer row found at index: {}", i);
                return i - 1;
            }
        }
        log.debug("Table footer row not found");
        return sheet.getLastRowNum();
    }

    private record TableParams(
            int firstRowIndex,
            int lastRowIndex
    ) {
    }
}

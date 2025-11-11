package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.mapper.SubsidyProgramMapper;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.CreateCashPlanLimitExcel;
import su.petrosoft.apk_ack_integration.model.excel.SubsidyProgramExcelRowDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExcelRowParser {
    private final ApkPlicanteService apkService;
    private final CashPlanLimitMapper cplMapper;
    private final SubsidyProgramMapper spMapper;

    public List<CashPlanLimit> getCashPlanLimits(MultipartFile file) throws IOException {
        List<CashPlanLimit> limits = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            TableBounds tableBounds = findTableBounds(sheet, "Раздел", "Итого");

            Map<CodeType, Map<Long, String>> allCodes = apkService.getCodesMap();

            for (int i = tableBounds.firstRowIndex; i <= tableBounds.lastRowIndex; i++) {
                CreateCashPlanLimitExcel cplExcel = mapCreateExcelRowToPojo(sheet.getRow(i));
                log.debug("Excel row mapped to CplPojo: {}", cplExcel);
                CashPlanLimit cpl = cplMapper.toCpl(cplExcel, allCodes);
                log.debug("Pojo mapped to CashPlanLimit: {}", cpl);
                limits.add(cpl);
            }
        }
        log.debug("");
        return limits;
    }

    public List<SubsidyProgramExcelRowDto> getSubsidyProgramDtoList(MultipartFile file) throws IOException {
        List<SubsidyProgramExcelRowDto> dtoList = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            TableBounds bounds = findTableBounds(sheet, "Код", "Итого");

            for (int i = bounds.firstRowIndex; i <= bounds.lastRowIndex; i++) {
                Row row = sheet.getRow(i);
                SubsidyProgramExcelRowDto dto = parseToSubsidyProgramDto(row);
                dtoList.add(dto);
                log.debug("Excel row mapped to DTO: {}", dto);
            }
        }
        return dtoList;
    }

    public SubsidyProgramExcelRowDto parseToSubsidyProgramDto(Row row) {
        return new SubsidyProgramExcelRowDto(
                row.getCell(0).getStringCellValue(),
                row.getCell(1).getStringCellValue(),
                row.getCell(2).getStringCellValue(),
                row.getCell(3).getStringCellValue(),
                row.getCell(4).getStringCellValue(),
                row.getCell(5).getStringCellValue(),
                row.getCell(6).getStringCellValue(),
                row.getCell(7).getStringCellValue(),
                row.getCell(8).getStringCellValue(),
                row.getCell(9).getStringCellValue(),
                row.getCell(10).getStringCellValue(),
                row.getCell(11).getStringCellValue(),
                row.getCell(12).getStringCellValue(),
                row.getCell(13).getStringCellValue(),
                row.getCell(14).getStringCellValue(),
                row.getCell(15).getStringCellValue(),
                row.getCell(16).getStringCellValue(),
                row.getCell(17).getStringCellValue(),
                row.getCell(18).getStringCellValue(),
                row.getCell(19).getStringCellValue(),
                row.getCell(20).getStringCellValue(),
                row.getCell(21).getStringCellValue(),
                row.getCell(22).getNumericCellValue(),
                row.getCell(23).getNumericCellValue(),
                row.getCell(24).getNumericCellValue(),
                row.getCell(25).getNumericCellValue(),
                row.getCell(26).getNumericCellValue(),
                row.getCell(27).getNumericCellValue(),
                row.getCell(28).getNumericCellValue(),
                row.getCell(29).getNumericCellValue(),
                row.getCell(30).getNumericCellValue(),
                row.getCell(31).getNumericCellValue(),
                row.getCell(32).getNumericCellValue(),
                row.getCell(33).getNumericCellValue(),
                row.getCell(34).getNumericCellValue(),
                row.getCell(35).getNumericCellValue(),
                row.getCell(36).getNumericCellValue(),
                row.getCell(37).getNumericCellValue(),
                row.getCell(38).getNumericCellValue(),
                row.getCell(39).getNumericCellValue(),
                row.getCell(40).getNumericCellValue()
        );
    }

    private CreateCashPlanLimitExcel mapCreateExcelRowToPojo(Row row) {
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

    private TableBounds findTableBounds(Sheet sheet, String headerSearchKey, String footerSearchKey) {
        int fstRow = findHeaderRow(sheet, headerSearchKey) + 1;
        log.debug("First effective row index: {}", fstRow);

        if (fstRow > sheet.getLastRowNum()) {
            log.warn("No one effective row in table");
            throw new IllegalArgumentException("No one effective row in table");
        }
        int lstRow = findLastRow(sheet, fstRow, footerSearchKey);
        log.debug("Last effective row index: {}", lstRow);

        return new TableBounds(fstRow, lstRow);
    }

    private int findHeaderRow(Sheet sheet, String searchKey) {
        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            if (sheet.getRow(i).getCell(0).getStringCellValue().equalsIgnoreCase(searchKey)) {
                log.debug("Table header row index found at [{}] by search key [{}]", i, searchKey);
                return i;
            }
        }
        log.error("Could not found table header");
        throw new IllegalArgumentException("Invalid excel file format");
    }

    private int findLastRow(Sheet sheet, int from, String searchKey) {
        for (int i = from; i <= sheet.getLastRowNum(); i++) {
            Cell cell = sheet.getRow(i).getCell(0);
            if (cell == null || cell.getStringCellValue().equalsIgnoreCase(searchKey)) {
                log.debug("Table footer row found at [{}]", i);
                return i - 1;
            }
        }
        log.debug("Table footer row not found. Last file row is considered as last effective row");
        return sheet.getLastRowNum();
    }

    private record TableBounds(
            int firstRowIndex,
            int lastRowIndex
    ) {
    }
}

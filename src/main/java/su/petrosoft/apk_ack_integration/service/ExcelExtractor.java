package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.exception.ExcelFileException;
import su.petrosoft.apk_ack_integration.model.data.excel.RosterKbkItemExcelRow;
import su.petrosoft.apk_ack_integration.model.enums.ExcelFileType;
import su.petrosoft.apk_ack_integration.model.data.DescriptedBudgetItemData;
import su.petrosoft.apk_ack_integration.model.data.excel.CofinancingLevelExcelRow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static su.petrosoft.apk_ack_integration.model.enums.ExcelFileType.COFINANCING_LEVEL;
import static su.petrosoft.apk_ack_integration.model.enums.ExcelFileType.ROSTER_KBK;
import static su.petrosoft.apk_ack_integration.model.enums.ExcelFileType.UNI_BUDGET;
import static su.petrosoft.apk_ack_integration.model.enums.ExcelFileType.UNI_BUDGET_2026_CLARIFIED;
import static su.petrosoft.apk_ack_integration.model.enums.ExcelFileType.UNI_BUDGET_CODED;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExcelExtractor {

    private final ExcelRowMapper mapper;

    public List<DescriptedBudgetItemData> getUniBudget2026ClarifiedRows(MultipartFile file) {
        return extractTableRows(file, UNI_BUDGET_2026_CLARIFIED, mapper::parseToUniBudget2026ClarifiedRow);
    }

    public List<DescriptedBudgetItemData> getUniBudgetCodedRows(MultipartFile file) {
        return extractTableRows(file, UNI_BUDGET_CODED, mapper::parseToUniBudgetCodedRow);
    }

    public List<DescriptedBudgetItemData> uniBudgetExcelRows(MultipartFile file) {
        return extractTableRows(file, UNI_BUDGET, mapper::parseToUniBudgetRow);
    }

    public List<RosterKbkItemExcelRow> getRosterKbkRows(MultipartFile file) {
        return extractTableRows(file, ROSTER_KBK, mapper::parseToRosterKbkRow);
    }

    public List<CofinancingLevelExcelRow> getCofinancingLevelRows(MultipartFile file) {
        return extractTableRows(file, COFINANCING_LEVEL, mapper::toCofinancingLevelRow);
    }

    private <T> List<T> extractTableRows(MultipartFile excelFile, ExcelFileType type, Function<Row, T> rowMapper) {
        List<T> dtoList = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(excelFile.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            TableBounds tableBounds = findTableBounds(sheet, type);

            for (int i = tableBounds.firstRowIndex; i <= tableBounds.lastRowIndex; i++) {
                Row row = sheet.getRow(i);
                T rowDto = rowMapper.apply(row);
                dtoList.add(rowDto);
                log.debug("Excel row [{}] mapped to DTO: [{}]", i, rowDto);
            }
            log.info("Extracted from excel file: [{}] effective rows", dtoList.size());
            return dtoList;
        } catch (IOException e) {
            log.error("Can not read excel file. Error message: [{}]", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private TableBounds findTableBounds(Sheet sheet, ExcelFileType type) {
        int fstRow = findHeaderRow(sheet, type.getColumnNames()) + 1;
        log.debug("First effective row number: [{}]", fstRow);

        if (fstRow > sheet.getLastRowNum()) {
            log.warn("No one effective row in table");
            throw new ExcelFileException("Excel file has no effective rows");
        }
        int lstRow = findLastRow(sheet, fstRow, type.getFooterSearchKey());
        log.debug("Last effective row index: [{}]", lstRow);

        return new TableBounds(fstRow, lstRow);
    }

    private int findHeaderRow(Sheet sheet, List<String> columnNames) {
        for (int i = 0; i <= Math.min(50, sheet.getLastRowNum()); i++) {
            if (isMatching(sheet.getRow(i), columnNames)) {
                log.debug("Table header row index found at [{}]", i);
                return i;
            }
        }
        log.error("Could not found table header");
        throw new ExcelFileException("Could not found table header");
    }

    private int findLastRow(Sheet sheet, int from, String searchKey) {
        for (int i = from; i <= sheet.getLastRowNum(); i++) {
            Cell cell = sheet.getRow(i).getCell(0);
            if (cell == null
                    || cell.getStringCellValue().isBlank()
                    || cell.getStringCellValue().equalsIgnoreCase(searchKey)) {
                log.debug("Table footer row found at [{}]", i);
                return i - 1;
            }
        }
        log.debug("Table footer row not found. Last row is considered as last effective row");
        return sheet.getLastRowNum();
    }

    private boolean isMatching(Row row, List<String> columnNames) {
        for (int i = 0; i < columnNames.size(); i++) {
            String expected = columnNames.get(i);
            Cell cell = row.getCell(i);

            if (cell == null || cell.getCellType() != CellType.STRING) {
                return false;
            }

            String actual = cell.getStringCellValue().trim();
            if (!actual.equalsIgnoreCase(expected)) {
                return false;
            }
        }
        return true;
    }

    private record TableBounds(
            int firstRowIndex,
            int lastRowIndex
    ) {
    }
}

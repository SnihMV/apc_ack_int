package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.model.excel.SubsidyProgramExcelRowDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExcelExtractor {

    private final ExcelRowParser rowParser;

    public List<SubsidyProgramExcelRowDto> getSubsidyProgramDtoList(MultipartFile file) {
        List<SubsidyProgramExcelRowDto> dtoList = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            TableBounds bounds = findTableBounds(sheet, "Код", "Итого");

            for (int i = bounds.firstRowIndex; i <= bounds.lastRowIndex; i++) {
                Row row = sheet.getRow(i);
                SubsidyProgramExcelRowDto dto = rowParser.parseToSubsidyProgramDto(row);
                dtoList.add(dto);
                log.debug("Excel row [{}] mapped to DTO: [{}]", i, dto);
            }

        } catch (IOException e) {
            log.error("Can not read excel file. Error message: [{}]", e.getMessage());
            throw new RuntimeException(e);
        }
        return dtoList;
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

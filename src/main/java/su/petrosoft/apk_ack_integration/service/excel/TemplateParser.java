package su.petrosoft.apk_ack_integration.service.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class TemplateParser {

    public TemplateStructure parseTemplate(InputStream templateStream) {
        try (Workbook workbook = new XSSFWorkbook(templateStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            TemplateStructure structure = new TemplateStructure();


            for (Row row : sheet) {
                for (Cell cell : row) {
                    if (cell.getCellType() == CellType.STRING) {
                        String value = cell.getStringCellValue();
                        if (value != null && value.startsWith("$")) {
                            Marker marker = parseMarker(value, row.getRowNum(), cell.getColumnIndex());
                            structure.addMarker(marker);
                        }
                    }
                }
            }

            structure.determineStructure();
            return structure;

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse template", e);
        }
    }

    private Marker parseMarker(String value, int row, int col) {
        // $cell_year
        // $row_dist_value14
        // $row_org_value621
        String[] parts = value.substring(1).split("_");

        MarkerType type = MarkerType.fromString(parts[0]); // cell или row
        MarkerLevel level = parts.length > 2 ? MarkerLevel.fromString(parts[1]) : null; // dist или org
        String fieldCode = parts.length > 2 ? parts[2] : parts[1]; // год или 14,15

        return new Marker(type, level, fieldCode, row, col);
    }

    enum MarkerType {
        CELL, ROW;

        static MarkerType fromString(String s) {
            if ("cell".equalsIgnoreCase(s)) return CELL;
            if ("row".equalsIgnoreCase(s)) return ROW;
            throw new IllegalArgumentException("Unknown marker type: " + s);
        }
    }

    enum MarkerLevel {
        DIST, ORG;

        static MarkerLevel fromString(String s) {
            if ("dist".equalsIgnoreCase(s)) return DIST;
            if ("org".equalsIgnoreCase(s)) return ORG;
            return null;
        }
    }

    @Data
    @AllArgsConstructor
    class Marker {
        private MarkerType type;
        private MarkerLevel level;
        private String fieldCode; // "year", "14", "15" и т.д.
        private int row;
        private int col;
    }

    @Data
    class TemplateStructure {
        private List<Marker> markers = new ArrayList<>();
        private Map<String, Marker> cellMarkers = new HashMap<>(); // для шапки
        private List<Marker> rowMarkers = new ArrayList<>(); // для данных
        private int dataStartRow = -1;
        private int districtRowOffset = 0; // насколько строка района отличается от строки производителя

        void addMarker(Marker marker) {
            markers.add(marker);
            if (marker.getType() == MarkerType.CELL) {
                cellMarkers.put(marker.getFieldCode(), marker);
            } else {
                rowMarkers.add(marker);
            }
        }

        void determineStructure() {
            // Находим строку с первым маркером данных
            dataStartRow = rowMarkers.stream()
                .mapToInt(Marker::getRow)
                .min()
                .orElse(-1);

            // Определяем, есть ли отдельная строка для района
            boolean hasDistrictRow = rowMarkers.stream()
                .anyMatch(m -> m.getLevel() == MarkerLevel.DIST);

            districtRowOffset = hasDistrictRow ? 1 : 0;
        }
    }
}

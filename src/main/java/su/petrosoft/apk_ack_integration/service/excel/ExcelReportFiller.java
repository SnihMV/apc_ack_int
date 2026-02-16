package su.petrosoft.apk_ack_integration.service.excel;

import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.INVALID_MARKER;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.MARKER_NOT_FOUND;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.exception.ExcelTemplateException;
import su.petrosoft.apk_ack_integration.model.DistrictData;
import su.petrosoft.apk_ack_integration.model.ProducerData;

@Service
public class ExcelReportFiller {

    private static final String MARKER_PREFIX = "$";
    private static final String HEADER_KEY = "header";
    private static final String DISTRICT_KEY = "dist";
    private static final String PRODUCER_KEY = "prod";

    /**
     * Заполняет шаблон отчета данными
     *
     * @param templateStream - шаблон Excel
     * @param districts      - данные по районам (ключ - название района)
     * @param headerData     - данные для шапки
     * @return готовый Excel файл в виде байтов
     */
    public byte[] fillReport(InputStream templateStream,
                             List<DistrictData> districts,
                             Map<String, Object> headerData,
                             boolean isDetailed) {
        try (Workbook workbook = new XSSFWorkbook(templateStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            TemplateStructure structure = analyzeTemplate(sheet);

//            removeMarkerRows(sheet, structure);

            fillHeader(sheet, structure.markers.get(HEADER_KEY), headerData);

            fillData(sheet, structure, districts, isDetailed);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            return baos.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при заполнении отчета", e);
        }
    }

    /**
     * Находит все маркеры в шаблоне
     */
    private TemplateStructure analyzeTemplate(Sheet sheet) {
        TemplateStructure structure = new TemplateStructure();

        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() == CellType.STRING) {
                    String value = cell.getStringCellValue();
                    if (value == null) {
                        continue;
                    }

                    if (value.startsWith(MARKER_PREFIX)) {
                        structure.addMarker(new MarkerInfo(
                                value.substring(MARKER_PREFIX.length()), row.getRowNum(),
                                cell.getColumnIndex(), cell.getCellStyle()
                        ));
                    }
                }
            }
        }
        if (structure.firstDataRow == -1) {
            throw new ExcelTemplateException(
                    MARKER_NOT_FOUND.formatted(MARKER_PREFIX, DISTRICT_KEY));
        }
        return structure;
    }

    /**
     * Заполняет шапку отчета
     */
    private void fillHeader(Sheet sheet, List<MarkerInfo> headerMarkers, Map<String, Object> headerData) {
        for (MarkerInfo marker : headerMarkers) {
            Row row = sheet.getRow(marker.row);
            Cell cell = row.getCell(marker.col);

            String key = marker.key;
            Object value = headerData.get(key);

            setCellValue(cell, value);
        }
    }

    /**
     * Заполняет данные (районы и производители)
     */
    private void fillData(Sheet sheet,
                          TemplateStructure structure,
                          List<DistrictData> districts,
                          boolean isDetailed) {
        int currentRow = structure.firstDataRow;

        DistrictData totalData = new DistrictData("ИТОГО", Collections.emptyList());

        for (DistrictData district : districts) {

            district.calculateDistrictTotal();

            addToTotal(totalData, district);

            Row districtRow = getOrCreateRow(sheet, currentRow);
            fillDistrictRow(districtRow, structure.markers.get(DISTRICT_KEY), district);
            currentRow++;

            if (isDetailed) {
                for (ProducerData producer : district.getProducers()) {
                    Row producerRow = getOrCreateRow(sheet, currentRow);
                    fillProducerRow(producerRow, structure.markers.get(PRODUCER_KEY), producer);
                    currentRow++;
                }
            }
        }
        Row totalRow = sheet.createRow(currentRow);
        fillDistrictRow(totalRow, structure.markers.get(DISTRICT_KEY), totalData);
    }

    private void addToTotal(DistrictData total, DistrictData district) {
        for (Map.Entry<String, BigDecimal> entry : district.getSums().entrySet()) {
            total.getSums().merge(entry.getKey(), entry.getValue(), BigDecimal::add);
        }
    }

    /**
     * Заполняет строку района
     */
    private void fillDistrictRow(Row row, List<MarkerInfo> districtMarkers,
                                 DistrictData district) {
        for (MarkerInfo marker : districtMarkers) {
            Cell cell = row.getCell(marker.col);
            if (cell == null) {
                cell = row.createCell(marker.col);
            }
            cell.setCellStyle(marker.style);

            String key = marker.key;

            if (key.equals("name")) {
                cell.setCellValue(district.getName() != null ? district.getName() : "");
            } else if (key.split("_").length == 1) {
                cell.setCellValue(district.getSum(key).doubleValue());
            } else {
                String[] parts = key.split("_");
                if (parts.length == 3) {
                    String key1 = parts[1];
                    String key2 = parts[2];

                    cell.setCellValue(district.percent(key1, key2).doubleValue());
                } else {
                    throw new ExcelTemplateException(INVALID_MARKER.formatted(key));
                }
            }
        }
    }

    /**
     * Заполняет строку производителя
     */
    private void fillProducerRow(Row row, List<MarkerInfo> producerMarkers,
                                 ProducerData producer) {
        for (MarkerInfo marker : producerMarkers) {
            Cell cell = row.getCell(marker.col);
            if (cell == null) {
                cell = row.createCell(marker.col);
            }
            cell.setCellStyle(marker.style);

            String key = marker.key;

            if (key.equals("name")) {
                cell.setCellValue(producer.getName() != null ? producer.getName() : "");
            } else if (key.equals("inn")) {
                cell.setCellValue(producer.getInn() != null ? producer.getInn() : "");
            } else {
                cell.setCellValue(producer.getValue(key).doubleValue());
            }
        }
    }

    /**
     * Удаляет строки с маркерами
     */
    private void removeMarkerRows(Sheet sheet, TemplateStructure structure) {
        int distMarkersRowNum = structure.markers.get(DISTRICT_KEY).get(0).row;
        int prodMarkersRowNum = structure.markers.get(PRODUCER_KEY).get(0).row;
        Row distRow = sheet.getRow(distMarkersRowNum);
        Row prodRow = sheet.getRow(prodMarkersRowNum);
        sheet.removeRow(prodRow);
        sheet.removeRow(distRow);
    }

    /**
     * Устанавливает значение в ячейку с учетом типа
     */
    private void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof LocalDate) {
            cell.setCellValue(java.sql.Date.valueOf((LocalDate) value));
        } else {
            cell.setCellValue(value.toString());
        }
    }

    /**
     * Получает или создает строку
     */
    private Row getOrCreateRow(Sheet sheet, int rowIndex) {
        Row row = sheet.getRow(rowIndex);
        return row != null ? row : sheet.createRow(rowIndex);
    }

    /**
     * Внутренний класс для хранения информации о маркере
     */
    private static class MarkerInfo {

        private String type;
        private String key;
        private int row;
        private int col;
        private CellStyle style;

        public MarkerInfo(String key, int row, int col, CellStyle style) {
            this.type = key.split("_")[0];
            this.key = key.substring(type.length() + 1);
            this.row = row;
            this.col = col;
            this.style = style;
        }
    }

    /**
     * Внутренний класс для хранения нформации о структуре шаблона
     */
    private static class TemplateStructure {

        private final Map<String, List<MarkerInfo>> markers = new HashMap<>();
        private int firstDataRow = -1;

        public void addMarker(MarkerInfo marker) {
            if (DISTRICT_KEY.equalsIgnoreCase(marker.type) && firstDataRow == -1) {
                firstDataRow = marker.row;
            }
            markers.computeIfAbsent(marker.type, k -> new ArrayList<>()).add(marker);
        }
    }
}

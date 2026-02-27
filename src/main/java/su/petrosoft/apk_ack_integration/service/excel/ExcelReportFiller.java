package su.petrosoft.apk_ack_integration.service.excel;

import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.INVALID_MARKER;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.MARKER_NOT_FOUND;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
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
    private static final String STATIC_CELL_KEY = "cell";
    private static final String DISTRICT_ROW_CELL_KEY = "dist";
    private static final String PRODUCER_ROW_CELL_KEY = "prod";

    /**
     * Заполняет шаблон отчета данными
     *
     * @param templateStream - шаблон Excel
     * @param districts      - данные по районам (ключ - название района)
     * @param headerData     - данные для шапки
     * @return готовый Excel файл в виде байтов
     */
    public byte[] fillReport(
            InputStream templateStream,
            Collection<DistrictData> districts,
            Map<String, Object> headerData,
            boolean isDetailed
    ) {
        try (Workbook workbook = new XSSFWorkbook(templateStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            TemplateStructure structure = analyzeTemplate(sheet);

            fillHeader(sheet, structure, headerData);

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
                                value.substring(MARKER_PREFIX.length()), row.getRowNum(), cell.getColumnIndex()));
                    }
                }
            }
        }
        if (structure.districtMarkerRowNum == -1) {
            throw new ExcelTemplateException(
                    MARKER_NOT_FOUND.formatted(MARKER_PREFIX, DISTRICT_ROW_CELL_KEY));
        }
        if (structure.producerMarkerRowNum == -1) {
            throw new ExcelTemplateException(
                    MARKER_NOT_FOUND.formatted(MARKER_PREFIX, PRODUCER_ROW_CELL_KEY));
        }
        structure.districtStyles = extractStylesFromRow(
                sheet.getRow(structure.districtMarkerRowNum));
        structure.producerStyles = extractStylesFromRow(
                sheet.getRow(structure.producerMarkerRowNum));
        return structure;
    }

    /**
     * Заполняет шапку отчета
     */
    private void fillHeader(Sheet sheet, TemplateStructure structure,
                            Map<String, Object> headerData) {
        List<MarkerInfo> headerMarkers = structure.markers.getOrDefault(STATIC_CELL_KEY, new ArrayList<>());
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
                          Collection<DistrictData> districts,
                          boolean isDetailed) {

        int currentRow = structure.districtMarkerRowNum;

        sheet.removeRow(sheet.getRow(structure.producerMarkerRowNum));
        sheet.removeRow(sheet.getRow(structure.districtMarkerRowNum));

        DistrictData totalData = new DistrictData("ИТОГО", Collections.emptyList());

        for (DistrictData district : districts) {

            district.calculateDistrictTotal();

            addToTotal(totalData, district);

            Row districtRow = getOrCreateRow(sheet, currentRow);
            applyStyles(districtRow, structure.districtStyles);
            fillDistrictRow(districtRow, structure.markers.get(DISTRICT_ROW_CELL_KEY), district);
            currentRow++;

            if (isDetailed) {
                for (ProducerData producer : district.getProducers()) {
                    Row producerRow = getOrCreateRow(sheet, currentRow);
                    applyStyles(producerRow, structure.producerStyles);
                    fillProducerRow(producerRow, structure.markers.get(PRODUCER_ROW_CELL_KEY), producer);
                    currentRow++;
                }
            }
        }
        Row totalRow = sheet.createRow(currentRow);
        applyStyles(totalRow, structure.districtStyles);
        fillDistrictRow(totalRow, structure.markers.get(DISTRICT_ROW_CELL_KEY), totalData);
    }

    /**
     * Извлекает стили всех ячеек из строки
     */
    private Map<Integer, CellStyle> extractStylesFromRow(Row row) {
        Map<Integer, CellStyle> styles = new HashMap<>();
        if (row == null) {
            return styles;
        }

        for (Cell cell : row) {
            styles.put(cell.getColumnIndex(), cell.getCellStyle());
        }
        return styles;
    }

    /**
     * Применяет стили к строке
     */
    private void applyStyles(Row row, Map<Integer, CellStyle> styles) {
        for (Map.Entry<Integer, CellStyle> entry : styles.entrySet()) {
            int col = entry.getKey();
            CellStyle style = entry.getValue();

            Cell cell = row.getCell(col);
            if (cell == null) {
                cell = row.createCell(col);
            }
            cell.setCellStyle(style);
        }
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
        fillRowWithMarkers(row, districtMarkers, new RowDataAdapter() {
            @Override
            public String getName() {
                return district.getName();
            }

            @Override
            public BigDecimal getValue(String key) {
                return district.getSum(key);
            }

            @Override
            public boolean hasInn() {
                return false;
            }
        });
    }

    /**
     * Заполняет строку производителя
     */
    private void fillProducerRow(Row row, List<MarkerInfo> producerMarkers,
                                 ProducerData producer) {
        fillRowWithMarkers(row, producerMarkers, new RowDataAdapter() {
            @Override
            public String getName() {
                return producer.getName();
            }

            @Override
            public BigDecimal getValue(String key) {
                return producer.getValue(key);
            }

            @Override
            public String getInn() {
                return producer.getInn();
            }

            @Override
            public boolean hasInn() {
                return true;
            }
        });
    }

    /**
     * Единый метод для заполнения строки с маркерами
     */
    private void fillRowWithMarkers(Row row, List<MarkerInfo> markers, RowDataAdapter data) {
        for (MarkerInfo marker : markers) {
            Cell cell = row.getCell(marker.col);
            if (cell == null) {
                cell = row.createCell(marker.col);
            }

            String key = marker.key;

            if (key.equals("name")) {
                cell.setCellValue(data.getName() != null ? data.getName() : "");
            } else if (data.hasInn() && key.equals("inn")) {
                cell.setCellValue(data.getInn() != null ? data.getInn() : "");
            }
            else if (key.startsWith("div_")) {
                String[] parts = key.split("_");
                if (parts.length == 3) {
                    BigDecimal numerator = data.getValue(parts[1]);
                    BigDecimal denominator = data.getValue(parts[2]);
                    cell.setCellValue(divide(numerator, denominator).doubleValue());
                } else {
                    throw new ExcelTemplateException(INVALID_MARKER.formatted(key));
                }
            } else if (key.startsWith("pct_")) {
                // Percent: pct_x_y -> x * 100 / y
                String[] parts = key.split("_");
                if (parts.length == 3) {
                    BigDecimal numerator = data.getValue(parts[1]);
                    BigDecimal denominator = data.getValue(parts[2]);
                    cell.setCellValue(percent(numerator, denominator).doubleValue());
                } else {
                    throw new ExcelTemplateException(INVALID_MARKER.formatted(key));
                }
            }  else if (key.startsWith("pdc_")) {
                // Per decile: pdc_x_y -> x * 10 / y
                String[] parts = key.split("_");
                if (parts.length == 3) {
                    BigDecimal numerator = data.getValue(parts[1]);
                    BigDecimal denominator = data.getValue(parts[2]);
                    cell.setCellValue(perDecile(numerator, denominator).doubleValue());
                } else {
                    throw new ExcelTemplateException(INVALID_MARKER.formatted(key));
                }

            } else if (key.startsWith("pml_")) {
                // Per mille: pdc_x_y -> x * 1000 / y
                String[] parts = key.split("_");
                if (parts.length == 3) {
                    BigDecimal numerator = data.getValue(parts[1]);
                    BigDecimal denominator = data.getValue(parts[2]);
                    cell.setCellValue(perMille(numerator, denominator).doubleValue());
                } else {
                    throw new ExcelTemplateException(INVALID_MARKER.formatted(key));
                }

            } else if (key.startsWith("pctofsum_")) {
                // Percent of sum: pct_a_b_c -> (a + b) / c * 100
                String[] parts = key.split("_");
                if (parts.length == 4) {
                    BigDecimal a = data.getValue(parts[1]);
                    BigDecimal b = data.getValue(parts[2]);
                    BigDecimal numerator = a.add(b);
                    BigDecimal denominator = data.getValue(parts[3]);
                    cell.setCellValue(percent(numerator, denominator).doubleValue());
                } else {
                    throw new ExcelTemplateException(INVALID_MARKER.formatted(key));
                }
            } else if (key.startsWith("pctof2sum_")) {
                // Percent of 2 sums: pct_a_b_c_d -> (a + b + c) / d * 100
                String[] parts = key.split("_");
                if (parts.length == 5) {
                    BigDecimal a = data.getValue(parts[1]);
                    BigDecimal b = data.getValue(parts[2]);
                    BigDecimal c = data.getValue(parts[3]);
                    BigDecimal numerator = a.add(b).add(c);
                    BigDecimal denominator = data.getValue(parts[4]);
                    cell.setCellValue(percent(numerator, denominator).doubleValue());
                } else {
                    throw new ExcelTemplateException(INVALID_MARKER.formatted(key));
                }
            } else if (key.startsWith("sub_")) {
                // Subtract: sub_a_b -> a - b
                String[] parts = key.split("_");
                if (parts.length == 3) {
                    BigDecimal minuend = data.getValue(parts[1]);
                    BigDecimal subtrahend = data.getValue(parts[2]);
                    cell.setCellValue(minuend.subtract(subtrahend).doubleValue());
                } else {
                    throw new ExcelTemplateException(INVALID_MARKER.formatted(key));
                }
            } else if (key.startsWith("2sub_")) {
                // 2 subtracts: sub_a_b_c -> a - b - c
                String[] parts = key.split("_");
                if (parts.length == 4) {
                    BigDecimal minuend = data.getValue(parts[1]);
                    BigDecimal sub1 = data.getValue(parts[2]);
                    BigDecimal sub2 = data.getValue(parts[3]);
                    cell.setCellValue(
                            minuend.subtract(sub1).subtract(sub2).doubleValue());
                } else {
                    throw new ExcelTemplateException(INVALID_MARKER.formatted(key));
                }
            } else if (key.startsWith("3sub_")) {
                // 3 subtracts: sub_a_b_c_d -> a - b - c - d
                String[] parts = key.split("_");
                if (parts.length == 5) {
                    BigDecimal minuend = data.getValue(parts[1]);
                    BigDecimal sub1 = data.getValue(parts[2]);
                    BigDecimal sub2 = data.getValue(parts[3]);
                    BigDecimal sub3 = data.getValue(parts[4]);
                    cell.setCellValue(
                            minuend.subtract(sub1).subtract(sub2).subtract(sub3).doubleValue());
                } else {
                    throw new ExcelTemplateException(INVALID_MARKER.formatted(key));
                }
            } else {
                BigDecimal value = data.getValue(key);
                cell.setCellValue(value != null ? value.doubleValue() : 0.0);
            }
        }
    }

    private BigDecimal divide(BigDecimal numerator, BigDecimal denominator) {
        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal perDecile(BigDecimal value1, BigDecimal value2) {
        return divide(value1, value2).multiply(BigDecimal.TEN);
    }

    private BigDecimal percent(BigDecimal value1, BigDecimal value2) {
        return divide(value1, value2).multiply(BigDecimal.valueOf(100));
    }

    private BigDecimal perMille(BigDecimal value1, BigDecimal value2) {
        return divide(value1, value2).multiply(BigDecimal.valueOf(1000));
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
     * Адаптер для доступа к данным строки
     */
    private interface RowDataAdapter {

        String getName();

        BigDecimal getValue(String key);

        default String getInn() {
            return null;
        }

        default boolean hasInn() {
            return false;
        }
    }

    /**
     * Внутренний класс для хранения информации о маркере
     */
    private static class MarkerInfo {

        private String type;
        private String key;
        private int row;
        private int col;

        public MarkerInfo(String key, int row, int col) {
            this.type = key.split("_")[0];
            this.key = key.substring(type.length() + 1);
            this.row = row;
            this.col = col;
        }
    }

    /**
     * Внутренний класс для хранения нформации о структуре шаблона
     */
    private static class TemplateStructure {

        private int districtMarkerRowNum = -1;
        private int producerMarkerRowNum = -1;
        private Map<Integer, CellStyle> districtStyles;
        private Map<Integer, CellStyle> producerStyles;
        private final Map<String, List<MarkerInfo>> markers = new HashMap<>();


        public void addMarker(MarkerInfo marker) {
            if (districtMarkerRowNum == -1 && DISTRICT_ROW_CELL_KEY.equalsIgnoreCase(marker.type)) {
                districtMarkerRowNum = marker.row;
            }
            if (producerMarkerRowNum == -1 && PRODUCER_ROW_CELL_KEY.equalsIgnoreCase(marker.type)) {
                producerMarkerRowNum = marker.row;
            }
            markers.computeIfAbsent(marker.type, k -> new ArrayList<>()).add(marker);
        }
    }
}

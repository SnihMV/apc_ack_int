package su.petrosoft.apk_ack_integration.mapper;

import static su.petrosoft.apk_ack_integration.util.OperationalReportUtil.CURRENT_DATE_ATTR;
import static su.petrosoft.apk_ack_integration.util.OperationalReportUtil.FILE_JSON_ATTR;
import static su.petrosoft.apk_ack_integration.util.OperationalReportUtil.REPORT_TYPE_ATTR;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractData;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.OperationalReport;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.ReportType;

@Slf4j
@Component
@RequiredArgsConstructor
public class OperationalReportMapper {

    private final ObjectMapper objectMapper;

    public OperationalReport toEntity(InstanceDto dto) {
        List<Attribute<?>> attributes = dto.attributes();
        return OperationalReport.builder()
                .id(dto.id())
                .version(dto.version())
                .reportType(ReportType.getById(extractData(attributes, REPORT_TYPE_ATTR)))
                .reportDate(extractData(attributes, CURRENT_DATE_ATTR))
                .reportValues(extractDataAsBigDecimalMap(extractData(attributes, FILE_JSON_ATTR)))
                .build();
    }

    private Map<String, BigDecimal> extractDataAsBigDecimalMap(String attrData) {
        byte[] rawData = Base64.getDecoder().decode(attrData);
        try {
            JsonNode root = objectMapper.readTree(rawData);
            JsonNode dataNode = root.path("data");

            if (dataNode.isMissingNode() || !dataNode.isObject()) {
                return Map.of();
            }

            Map<String, BigDecimal> result = new HashMap<>();

            dataNode.properties().forEach(entry -> {
                String key = entry.getKey();
                JsonNode valueNode = entry.getValue();

                if (!valueNode.isNull()) {
                    BigDecimal decimalValue = convertJsonNodeToBigDecimal(valueNode);
                    if (decimalValue != null) {
                        result.put(key, decimalValue);
                    }
                }
            });
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON: " + e.getMessage(), e);
        }
    }

    private BigDecimal convertJsonNodeToBigDecimal(JsonNode node) {
        try {
            if (node.isNumber()) {
                return node.decimalValue();
            } else if (node.isTextual()) {
                String text = node.asText().trim();
                if (!text.isEmpty()) {
                    return new BigDecimal(text);
                }
            }
        } catch (NumberFormatException e) {
            log.error("Cannot convert value to BigDecimal: [{}]", node);
        }
        return null;
    }

}

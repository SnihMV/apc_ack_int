package su.petrosoft.apk_ack_integration.mapper;

import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.CONVERT_TO_BIGDECIMAL_ERROR;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.FAILED_TO_PARSE_JSON;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.JSON_NODE_UNEXPECTABLE_TYPE;
import static su.petrosoft.apk_ack_integration.util.OperationalReportUtil.RECIPIENT_ATTR;
import static su.petrosoft.apk_ack_integration.util.OperationalReportUtil.REPORT_DATE_ATTR;
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
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.exception.JsonParsingException;
import su.petrosoft.apk_ack_integration.exception.OperationalReportJsonParsingException;
import su.petrosoft.apk_ack_integration.model.OperationalReport;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.ReportType;

@Slf4j
@Component
@RequiredArgsConstructor
public class OperationalReportMapper {

    private static final Set<String> EXCLUDED_KEYS = Set.of("entName", "districtName", "date");
    private final ObjectMapper objectMapper;

    public OperationalReport toEntity(InstanceDto dto) {
        List<Attribute<?>> attributes = dto.attributes();
        return OperationalReport.builder()
            .id(dto.id())
            .version(dto.version())
            .recipientId(extractData(attributes, RECIPIENT_ATTR))
            .reportType(ReportType.getById(extractData(attributes, REPORT_TYPE_ATTR)))
            .reportDate(extractData(attributes, REPORT_DATE_ATTR))
            .reportValues(extractDataAsBigDecimalMap(dto.id(), extractData(attributes, FILE_JSON_ATTR)))
            .build();
    }

    private Map<String, BigDecimal> extractDataAsBigDecimalMap(long reportId, String codedData) {
        byte[] rawData = Base64.getDecoder().decode(codedData);
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

                if (!EXCLUDED_KEYS.contains(key) && !valueNode.isNull()) {
                    BigDecimal decimalValue = convertJsonNodeToBigDecimal(valueNode);
                    if (decimalValue != null) {
                        result.put(key, decimalValue);
                    }
                }
            });
            return result;
        } catch (Exception e) {
            String errorMsg = FAILED_TO_PARSE_JSON.formatted(reportId, e.getMessage());
            log.error(errorMsg);
            throw new OperationalReportJsonParsingException(errorMsg, e);
        }
    }

    private BigDecimal convertJsonNodeToBigDecimal(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        try {
            if (node.isNumber()) {
                return node.decimalValue();
            }

            if (node.isTextual()) {
                String text = node.asText().trim();
                if (text.isEmpty()) {
                    return null;
                }
                return new BigDecimal(text);
            }
            log.warn(JSON_NODE_UNEXPECTABLE_TYPE.formatted(node));
            return null;

        } catch (NumberFormatException e) {
            throw new JsonParsingException(CONVERT_TO_BIGDECIMAL_ERROR.formatted(node.asText()), e);
        }
    }

}

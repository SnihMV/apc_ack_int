package su.petrosoft.apk_ack_integration.mapper;

import static su.petrosoft.apk_ack_integration.util.OperationalReportUtil.FILE_JSON_ATTR;
import static su.petrosoft.apk_ack_integration.util.OperationalReportUtil.REPORT_TYPE_ATTR;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.getAttrData;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.OperationalReport;
import su.petrosoft.apk_ack_integration.model.dto.plicante.ReportFieldDto;
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
            .reportType(ReportType.getById((long) getAttrData(attributes, REPORT_TYPE_ATTR)))
            .reportFile(converting((String) getAttrData(attributes, FILE_JSON_ATTR)))
            .build();
    }

    private List<ReportFieldDto> converting(String attrData) {
        byte[] rawData = Base64.getDecoder().decode(attrData);
        try {
            return objectMapper.readValue(rawData, new TypeReference<>() {
            });
        } catch (IOException e) {
            log.error("Converting JSON to List<ReportFieldDto> error: [{}]", e.getMessage());
            throw new RuntimeException("Converting JSON error: ".concat(e.getMessage()));
        }
    }

}

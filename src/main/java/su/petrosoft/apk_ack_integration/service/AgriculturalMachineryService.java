package su.petrosoft.apk_ack_integration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.client.ApkPlicanteRestClient;
import su.petrosoft.apk_ack_integration.model.AgriculturalMachineryReport;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.response.AttributeRepresentationDto;
import su.petrosoft.apk_ack_integration.util.AgriculturalMachineryUtil;

import java.util.List;

import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryUtil.buildRequestDtoForReportProcessing;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgriculturalMachineryService {
    private final ApkPlicanteRestClient plicanteRestClient;
//    private final AgriculturalMachineryReportMapper amrMapper;
    private final ObjectMapper objectMapper;


    public void processReport(Long id) {
        getAgriculturalMachineryReport(id);
    }

    @SneakyThrows
    private AgriculturalMachineryReport getAgriculturalMachineryReport(Long id) {
        List<AttributeRepresentationDto> attributes = plicanteRestClient.getInstanceRepresentation(
                buildRequestDtoForReportProcessing(id));
        String json = objectMapper.writeValueAsString(attributes);
        log.debug("Get instance [{}] attributes response json [{}]", id, json);
        return null;
    }
}

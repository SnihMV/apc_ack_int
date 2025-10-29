package su.petrosoft.apk_ack_integration.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.model.dto.response.EsbGetMessageResponseDto;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class XmlExtractorService {

    private final XmlMapper xmlMapper;

    public <T> T extractXml(EsbGetMessageResponseDto dto, Class<T> xmlType) {
        byte[] decodedRawData = Base64.getDecoder().decode(dto.data());
        String xml = new String(decodedRawData, StandardCharsets.UTF_8);
        try {
            return xmlMapper.readValue(xml, xmlType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

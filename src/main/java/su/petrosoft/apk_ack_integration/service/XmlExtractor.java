package su.petrosoft.apk_ack_integration.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.exception.InvalidXmlException;
import su.petrosoft.apk_ack_integration.model.dto.response.AckGetUpdateMessageResponseDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.FILE_IS_EMPTY;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.INVALID_XML_FORMAT;

@Service
@RequiredArgsConstructor
public class XmlExtractor {

    private final XmlMapper xmlMapper;

    public <T> T extractFromFile(MultipartFile file, Class<T> xmlType) {
        if (file.isEmpty()) {
            throw new InvalidXmlException(FILE_IS_EMPTY.formatted(file.getOriginalFilename()));
        }
        try {
            return xmlMapper.readValue(file.getInputStream(), xmlType);
        } catch (IOException e) {
            throw new InvalidXmlException(INVALID_XML_FORMAT.formatted(e.getMessage()), e);
        }
    }

    public <T> T convertBase64String(AckGetUpdateMessageResponseDto dto, Class<T> xmlType) {
        byte[] decodedRawData = Base64.getDecoder().decode(dto.data());
        String xml = new String(decodedRawData, StandardCharsets.UTF_8);
        return convertString(xml, xmlType);
    }

    private <T> T convertString(String str, Class<T> xmlType) {
        try {
            return xmlMapper.readValue(str, xmlType);
        } catch (Exception e) {
            throw new InvalidXmlException(INVALID_XML_FORMAT.formatted(e.getMessage()), e);
        }
    }
}

package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.exception.InvalidXmlException;
import su.petrosoft.apk_ack_integration.model.xml.CreateSubsidiesEstimatedAmountsXml;

import java.util.List;
import java.util.stream.Collectors;

import static su.petrosoft.apk_ack_integration.model.xml.CreateSubsidiesEstimatedAmountsXml.*;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessage.FILE_IS_EMPTY;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessage.NO_CONTENT;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubsidyEstimatedAmountService {
    private final XmlExtractor xmlExtractor;

    public void createAllFromXml(MultipartFile file) {
        CreateSubsidiesEstimatedAmountsXml xml =
                xmlExtractor.extractFromFile(file, CreateSubsidiesEstimatedAmountsXml.class);
        List<SubsidyEstimatedAmountXml> amounts = xml.objects();
        if (amounts == null || amounts.isEmpty()) {
            throw new InvalidXmlException(NO_CONTENT.formatted(file.getOriginalFilename()));
        }
        List<String> innList = amounts.stream()
                .map(SubsidyEstimatedAmountXml::applicantINN)
                .toList();

    }
}

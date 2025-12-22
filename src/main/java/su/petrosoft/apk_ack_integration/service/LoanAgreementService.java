package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.client.AckRestClient;
import su.petrosoft.apk_ack_integration.client.ApkPlicanteRestClient;
import su.petrosoft.apk_ack_integration.exception.InvalidXmlException;
import su.petrosoft.apk_ack_integration.mapper.SubsidyRecipientMapper;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.xml.CreatingSubsidiesAmountsXml;
import su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static su.petrosoft.apk_ack_integration.model.xml.CreatingSubsidiesAmountsXml.SubsidyAmountXml;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessage.NO_CONTENT;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.INN_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.buildGettingRecipientsByInnsRequestDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanAgreementService {
    private final XmlExtractor xmlExtractor;
    private final ApkPlicanteRestClient plicanteRestClient;
    private final AckRestClient ackRestClient;
    private final SubsidyRecipientMapper srMapper;

    public void createSubsidiesAmountsFromXml(MultipartFile file) {
        CreatingSubsidiesAmountsXml xml =
                xmlExtractor.extractFromFile(file, CreatingSubsidiesAmountsXml.class);
        List<SubsidyAmountXml> amounts = xml.objects();
        if (amounts == null || amounts.isEmpty()) {
            throw new InvalidXmlException(NO_CONTENT.formatted(file.getOriginalFilename()));
        }
        List<String> innListFromXml = amounts.stream()
                .map(SubsidyAmountXml::recipientINN)
                .collect(Collectors.toList());

        log.debug("INNs from xml: [{}]", innListFromXml);

        List<InstanceDto> foundInstances = plicanteRestClient.getTableAttributesList(
                buildGettingRecipientsByInnsRequestDto(innListFromXml));

        Map<String, Long> innToIdMap = foundInstances.stream()
                .collect(Collectors.toMap(
                        ins -> PlicanteInstanceUtil.extractData(ins.attributes(), INN_ATTR),
                        InstanceDto::id
                ));

        innListFromXml.removeAll(innToIdMap.keySet());
        for (String inn : innListFromXml) {
        }

    }
}

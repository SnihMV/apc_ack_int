package su.petrosoft.apk_ack_integration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.client.NiFiRestClient;
import su.petrosoft.apk_ack_integration.client.ApkPlicanteRestClient;
import su.petrosoft.apk_ack_integration.exception.InvalidXmlException;
import su.petrosoft.apk_ack_integration.mapper.SubsidyAmountMapper;
import su.petrosoft.apk_ack_integration.mapper.SubsidyProgramMapper;
import su.petrosoft.apk_ack_integration.mapper.SubsidyRecipientMapper;
import su.petrosoft.apk_ack_integration.model.SubsidyAmount;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.SubsidyRecipient;
import su.petrosoft.apk_ack_integration.model.dto.nifi.GetCompanyByInnResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.xml.CreatingSubsidiesAmountsXml;
import su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static su.petrosoft.apk_ack_integration.model.xml.CreatingSubsidiesAmountsXml.SubsidyAmountXml;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessage.NO_CONTENT;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.*;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.*;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.INN_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.buildGettingRecipientsByInnsRequestDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanAgreementService {
    private final XmlExtractor xmlExtractor;
    private final ApkPlicanteRestClient plicanteRestClient;
    private final ApkPlicanteService apkPlicanteService;
    private final NiFiRestClient niFiRestClient;
    private final SubsidyRecipientMapper srMapper;
    private final SubsidyProgramMapper spMapper;
    private final SubsidyAmountMapper saMapper;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void createSubsidyAmountsFromXml(MultipartFile file) {
        CreatingSubsidiesAmountsXml xml =
                xmlExtractor.extractFromFile(file, CreatingSubsidiesAmountsXml.class);
        List<SubsidyAmountXml> amountXmlList = xml.objects();
        if (amountXmlList == null || amountXmlList.isEmpty()) {
            throw new InvalidXmlException(NO_CONTENT.formatted(file.getOriginalFilename()));
        }
        List<String> innListFromXml = amountXmlList.stream()
                .map(SubsidyAmountXml::recipientINN)
                .collect(Collectors.toList());

        log.debug("INNs from xml: [{}]", innListFromXml);

        List<InstanceDto> foundRecipientInstances = plicanteRestClient.getTableAttributesList(
                buildGettingRecipientsByInnsRequestDto(innListFromXml));

        Map<String, Long> innToIdMap = foundRecipientInstances.stream()
                .collect(Collectors.toMap(
                        ins -> extractData(ins.attributes(), INN_ATTR),
                        InstanceDto::id));
        log.debug("Existed recipients' inn map: [{}]", innToIdMap);

        List<InstanceDto> foundSpInstances = plicanteRestClient.getTableAttributesList(getThirdLevelSpRequestDto());
        if (foundSpInstances.isEmpty()) {
            log.warn("Third level subsidy programs not found");
            return;
        }

        Map<SubsidyProgram, Long> spMap = foundSpInstances.stream()
                .map(spMapper::toEntity)
                .collect(Collectors.toMap(
                        Function.identity(),
                        SubsidyProgram::getId));
        log.debug("Found [{}] third level subsidy programs", spMap.size());

        List<Long> createdAmountIdList = new ArrayList<>();
        for (SubsidyAmountXml amountXml : amountXmlList) {

            String kcsr = amountXml.kcsr();
            String dopKr = amountXml.dopKR();
            SubsidyProgram searchKey = SubsidyProgram.builder()
                    .level(3L)
                    .kcsr(kcsr)
                    .dopKr(dopKr)
                    .build();

            Long spId = spMap.get(searchKey);
            if (spId == null) {
                log.warn("Subsidy Program with kcsr=[{}] and dopKR=[{}] not found", kcsr, dopKr);
                continue;
            }

            String inn = amountXml.recipientINN();
            Long recipientId = innToIdMap.get(inn);
            if (recipientId == null) {
                log.info("Recipient with inn = [{}] not existed. Trying to find it in NiFi", inn);
                GetCompanyByInnResponseDto dto = niFiRestClient.getCompanyByInn(inn);
                log.debug("=== FROM NiFi: [{}]", dto);
                SubsidyRecipient recipient = srMapper.toEntity(dto);
                recipient.setInn(inn);
                CreateInstanceRequestDto creatingDto = srMapper.toCreatingDto(recipient);
                String s = objectMapper.writeValueAsString(creatingDto);
                log.debug("Recipient creating JSON: {}", s);
                InstanceDto createdInstance = plicanteRestClient.createInstance(creatingDto);
                SubsidyRecipient createdRecipient = srMapper.toEntity(createdInstance);
                log.debug("Created Recipient: [{}]", createdRecipient);
            }

            SubsidyAmount subsidyAmount = SubsidyAmount.builder()
                    .year(amountXml.year())
                    .subsidyProgramId(spId)
                    .recipientId(recipientId)
                    .sst(amountXml.sst())
                    .sob(amountXml.sob())
                    .sn(amountXml.sn())
                    .build();

            CreateInstanceRequestDto creatingDto = saMapper.toCreatingDto(subsidyAmount);
            Long createdAmountId = plicanteRestClient.createInstance(creatingDto).id();
            createdAmountIdList.add(createdAmountId);
        }
        log.info("Created SubsidyAmounts: [{}]", createdAmountIdList.size());
    }
}

package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.client.ApkPlicanteRestClient;
import su.petrosoft.apk_ack_integration.client.NiFiRestClient;
import su.petrosoft.apk_ack_integration.client.TechPlicanteSoapClient;
import su.petrosoft.apk_ack_integration.mapper.SubsidyRecipientMapper;
import su.petrosoft.apk_ack_integration.model.SubsidyRecipient;
import su.petrosoft.apk_ack_integration.model.dto.nifi.GetCompanyByInnResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;

import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.requestDtoToFindRecipientsByAppTypeForUpdate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScriptService {
    private final ApkPlicanteRestClient apkRestClient;
    private final NiFiRestClient niFiRestClient;
    private final SubsidyRecipientMapper recipientMapper;
    private final TechPlicanteSoapClient techPlicanteSoapClient;

    public void refreshMunicipalitiesData() {
        List<InstanceDto> dtoList = apkRestClient.getTableAttributesList(
                requestDtoToFindRecipientsByAppTypeForUpdate(652));
        int i = 0;
        for (InstanceDto dto : dtoList) {
            log.debug("=== DTO : [{}]", dto);
            SubsidyRecipient recipient = recipientMapper.toEntity(dto);
            log.debug("Recipient: [{}]", recipient);
            System.out.print(++i + " Inn: " + recipient.getInn() + " Data: ");
                    GetCompanyByInnResponseDto company = niFiRestClient.getCompanyByInn(recipient.getInn());
            System.out.println(company);

        }
    }

    public void deleteInstancesByRange(long from, long to) {
        List<Long> list = LongStream.range(from, to + 1).boxed().toList();
        System.out.println(list);
        techPlicanteSoapClient.deleteInstancesList(list);
    }

    private void updateRecipientByNotNullValues(SubsidyRecipient recipient, GetCompanyByInnResponseDto company) {
        if (company.kpp() != null && !company.kpp().isBlank()) {
            recipient.setKpp(company.kpp());
        }
    }
}

package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.client.ApkPlicanteRestClient;
import su.petrosoft.apk_ack_integration.client.NiFiRestClient;
import su.petrosoft.apk_ack_integration.mapper.SubsidyRecipientMapper;
import su.petrosoft.apk_ack_integration.model.SubsidyRecipient;
import su.petrosoft.apk_ack_integration.model.dto.nifi.GetCompanyByInnResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;

import java.util.List;

import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.buildRequestDtoToFindRecipientInnsByAppType;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScriptService {
    private final ApkPlicanteRestClient apkRestClient;
    private final NiFiRestClient niFiRestClient;
    private final SubsidyRecipientMapper recipientMapper;

    public void refreshMunicipalitiesData() {
        List<InstanceDto> dtoList = apkRestClient.getTableAttributesList(
                buildRequestDtoToFindRecipientInnsByAppType(652));
        int i = 0;
        for (InstanceDto dto : dtoList) {
            SubsidyRecipient recipient = recipientMapper.toEntity(dto);
            System.out.print(++i + " Inn: " + recipient.getInn() + " Data: ");
                    GetCompanyByInnResponseDto company = niFiRestClient.getCompanyByInn(recipient.getInn());
            System.out.println(company);
            updateRecipientByNotNullValues(recipient, company);


        }
    }

    private void updateRecipientByNotNullValues(SubsidyRecipient recipient, GetCompanyByInnResponseDto company) {
        if (company.kpp() != null && !company.kpp().isBlank()) {
            recipient.setKpp(company.kpp());
        }
    }
}

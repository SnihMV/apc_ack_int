package su.petrosoft.apk_ack_integration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.client.PlicanteRestClient;
import su.petrosoft.apk_ack_integration.client.NiFiRestClient;
import su.petrosoft.apk_ack_integration.client.PlicanteSoapClient;
import su.petrosoft.apk_ack_integration.mapper.SubsidyRecipientMapper;
import su.petrosoft.apk_ack_integration.model.SubsidyRecipient;
import su.petrosoft.apk_ack_integration.model.dto.nifi.GetDataFromEgrulByInnDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.defineUpdatedAttributes;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.requestDtoForUpdateRecipientData;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.requestDtoToFindRecipientsByAppTypeForUpdate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScriptService {
    private final PlicanteRestClient apkRestClient;
    private final NiFiRestClient niFiRestClient;
    private final SubsidyRecipientMapper recipientMapper;
    private final PlicanteSoapClient plicanteSoapClient;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void updateMunicipalitiesData() {
        List<InstanceDto> dtoList = apkRestClient.getTableAttributesList(
                requestDtoToFindRecipientsByAppTypeForUpdate(652));
        log.info("Found [{}] recipients with AppType=652 to update", dtoList.size());
        for (InstanceDto dto : dtoList) {
            SubsidyRecipient recipient = recipientMapper.toEntity(dto);
            log.debug("Recipient to update: [{}]", recipient);
            GetDataFromEgrulByInnDto egrulData = niFiRestClient.getCompanyByInn(recipient.getInn());
            log.debug("Data from EGRUL: [{}]", egrulData);

            if (egrulData.inn() == null) {
                continue;
            }
            UpdateInstanceRequestDto updateInstanceRequestDto = requestDtoForUpdateRecipientData(recipient, egrulData);
            if (updateInstanceRequestDto.instance().attributes().isEmpty()) {
                continue;
            }
            UpdateInstanceResponseDto responseDto = apkRestClient.updateInstance(updateInstanceRequestDto);
            log.info("Recipient [{}] updated", responseDto.id());
            System.out.println(egrulData);
        }
    }



    public void deleteInstancesByRange(long fromInclusive, long toInclusive) {
        List<Long> list = LongStream.range(fromInclusive, toInclusive + 1).boxed().toList();
        System.out.println(list);
        plicanteSoapClient.deleteInstancesList(list);
    }

    private void updateRecipientByNotNullValues(SubsidyRecipient recipient, GetDataFromEgrulByInnDto company) {
        if (company.kpp() != null && !company.kpp().isBlank()) {
            recipient.setKpp(company.kpp());
        }
    }
}

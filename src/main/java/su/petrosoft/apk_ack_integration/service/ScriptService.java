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
import su.petrosoft.apk_ack_integration.model.dto.plicante.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.RequestedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DateAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.DateFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.Filter;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.*;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.requestDtoForUpdateRecipientData;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.requestDtoToFindRecipientsByAppTypeForUpdate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScriptService {
    private final PlicanteRestClient plicanteRestClient;
    private final NiFiRestClient niFiRestClient;
    private final SubsidyRecipientMapper recipientMapper;
    private final PlicanteSoapClient plicanteSoapClient;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void updateMunicipalitiesData() {
        List<InstanceDto> dtoList = plicanteRestClient.getTableAttributesList(
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
            UpdateInstanceResponseDto responseDto = plicanteRestClient.updateInstance(updateInstanceRequestDto);
            log.info("Recipient [{}] updated", responseDto.id());
            System.out.println(egrulData);
        }
    }


    public void deleteInstancesByRange(long fromInclusive, long toInclusive) {
        List<Long> list = LongStream.range(fromInclusive, toInclusive + 1).boxed().toList();
        System.out.println(list);
        plicanteSoapClient.deleteInstancesList(list);
    }

    public void normalizeDate(long templateId, long attributeId, LocalDate date) {
        List<InstanceDto> dtoList = plicanteRestClient.getTableAttributesList(GetAttributesListRequestDto.builder()
                .templateId(templateId)
                .attributes(List.of(
                        new RequestedAttribute(attributeId)
                ))
                .filter(new Filter(List.of(
                        new DateFilterAttribute(attributeId, toEpochMilli(date))
                )))
                .build());
        dtoList.stream()
                .map(dto -> new UpdateInstanceRequestDto(
                        InstanceDto.builder()
                                .id(dto.id())
                                .version(dto.version())
                                .attributes(List.of(
                                        new DateAttribute(attributeId, toEpochMilli(date, ZoneId.of("UTC")))
                                ))
                                .build()
                ))
                .forEach(plicanteRestClient::updateInstance);
    }

    private void updateRecipientByNotNullValues(SubsidyRecipient recipient, GetDataFromEgrulByInnDto company) {
        if (company.kpp() != null && !company.kpp().isBlank()) {
            recipient.setKpp(company.kpp());
        }
    }
}

package su.petrosoft.apk_ack_integration.mapper;

import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractAllData;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractData;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.toEpochMilli;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.toLocalDate;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.APP_TYPE_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.DISTRICT_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.FULL_TITLE_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.INN_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.KPP_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.MACHINE_PARK_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.OGRN_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.OGRN_DATE_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.SHORT_TITLE_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.TEMPLATE_ID;

import java.util.List;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.SubsidyRecipient;
import su.petrosoft.apk_ack_integration.model.dto.nifi.GetDataFromEgrulByInnDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.AttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DateAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil;

@Component
public class SubsidyRecipientMapper {
    public SubsidyRecipient toEntity(InstanceDto dto) {
        List<AttributeDto<?>> attributeDtos = dto.attributeDtos();
        return SubsidyRecipient.builder()
            .id(dto.id())
            .version(dto.version())
            .fullTitle(extractData(attributeDtos, FULL_TITLE_ATTR))
            .shortTitle(extractData(attributeDtos, SHORT_TITLE_ATTR))
            .inn(extractData(attributeDtos, INN_ATTR))
            .kpp(extractData(attributeDtos, KPP_ATTR))
            .ogrn(extractData(attributeDtos, OGRN_ATTR))
            .ogrnDate(toLocalDate(extractData(attributeDtos, OGRN_DATE_ATTR)))
            .appType(extractData(attributeDtos, APP_TYPE_ATTR))
            .machineParkIds(extractAllData(attributeDtos, MACHINE_PARK_ATTR))
            .districtId(extractData(attributeDtos, DISTRICT_ATTR))
            .build();

    }

    public SubsidyRecipient toEntity(GetDataFromEgrulByInnDto dto) {
        return SubsidyRecipient.builder()
                .ogrn(dto.ogrn())
                .ogrnDate(dto.ogrnDate())
                .kpp(dto.kpp())
                .fullTitle(dto.fullTitle())
                .shortTitle(dto.shortTitle())
                .build();
    }

    public CreateInstanceRequestDto toCreatingDto(SubsidyRecipient recipient) {
        return new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(CashPlanLimitUtil.TEMPLATE_ID)
                        .attributes(List.of(
                                new StringAttributeDto(FULL_TITLE_ATTR, recipient.getFullTitle()),
                                new StringAttributeDto(SHORT_TITLE_ATTR, recipient.getShortTitle()),
                                new StringAttributeDto(KPP_ATTR, recipient.getKpp()),
                                new StringAttributeDto(INN_ATTR, recipient.getInn()),
                                new StringAttributeDto(OGRN_ATTR, recipient.getOgrn()),
                                new DateAttributeDto(OGRN_DATE_ATTR, toEpochMilli(recipient.getOgrnDate()))))
                        .build()
        );
    }

    public UpdateInstanceRequestDto toUpdatingDtoByNotNullValues(SubsidyRecipient recipient) {
        return new UpdateInstanceRequestDto(
                InstanceDto.builder()
                        .id(recipient.getId())
                        .templateId(TEMPLATE_ID)
                        .version(recipient.getVersion())
                        .build()
        );
    }
}

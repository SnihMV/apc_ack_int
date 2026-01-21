package su.petrosoft.apk_ack_integration.mapper;

import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.SubsidyRecipient;
import su.petrosoft.apk_ack_integration.model.dto.nifi.GetCompanyByInnResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DateAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil;
import su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil;
import su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil;

import java.util.List;

import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.*;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractData;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.*;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.TEMPLATE_ID;

@Component
public class SubsidyRecipientMapper {
    public SubsidyRecipient toEntity(InstanceDto dto) {
        List<Attribute<?>> attributes = dto.attributes();
        return SubsidyRecipient.builder()
                .id(dto.id())
                .version(dto.version())
                .fullTitle(extractData(attributes, FULL_TITLE_ATTR))
                .shortTitle(extractData(attributes, SHORT_TITLE_ATTR))
                .inn(extractData(attributes, INN_ATTR))
                .kpp(extractData(attributes, KPP_ATTR))
                .ogrn(extractData(attributes, OGRN_ATTR))
                .ogrnDate(toLocalDate(extractData(attributes, OGRN_DATE_ATTR)))
                .build();

    }

    public SubsidyRecipient toEntity(GetCompanyByInnResponseDto dto) {
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
                                new StringAttribute(FULL_TITLE_ATTR, recipient.getFullTitle()),
                                new StringAttribute(SHORT_TITLE_ATTR, recipient.getShortTitle()),
                                new StringAttribute(KPP_ATTR, recipient.getKpp()),
                                new StringAttribute(INN_ATTR, recipient.getInn()),
                                new StringAttribute(OGRN_ATTR, recipient.getOgrn()),
                                new DateAttribute(OGRN_DATE_ATTR, toEpochMilli(recipient.getOgrnDate()))))
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

package su.petrosoft.apk_ack_integration.util;

import su.petrosoft.apk_ack_integration.model.SubsidyRecipient;
import su.petrosoft.apk_ack_integration.model.dto.nifi.GetDataFromEgrulByInnDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.RequestedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DateAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.Filter;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.LinkedFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.LongFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.StringFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.Value;
import su.petrosoft.apk_ack_integration.model.enums.ViewType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static su.petrosoft.apk_ack_integration.model.enums.SqlOperation.*;
import static su.petrosoft.apk_ack_integration.model.enums.ViewType.*;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.*;

public class SubsidyRecipientUtil {
    public static final long TEMPLATE_ID = 3318;
    public static final String TEMPLATE_TITLE = "Журнал учета получателей государственной поддержки";
    public static final long ID_ATTR = 531;
    public static final long SHORT_TITLE_ATTR = 543;
    public static final long FULL_TITLE_ATTR = 544;
    public static final long INN_ATTR = 545;
    public static final long KPP_ATTR = 546;
    public static final long OGRN_ATTR = 550;
    public static final long OGRN_DATE_ATTR = 554;
    public static final long APP_TYPE_ATTR = 3420;
    public static final long MACHINE_PARK_ATTR = 4321;

    public static GetAttributesListRequestDto buildRequestDtoToFindRecipientById(long id) {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .attributes(List.of(
                        new RequestedAttribute(MACHINE_PARK_ATTR)))
                .filter(new Filter(List.of(
                        new LongFilterAttribute(ID_ATTR, id))))
                .build();
    }

    public static GetAttributesListRequestDto buildGettingRecipientsByInnsRequestDto(List<String> innListFromXml) {
return GetAttributesListRequestDto.builder()
        .templateId(TEMPLATE_ID)
        .viewType(DETAILED_FORM_VIEW)
        .attributes(List.of(
                new RequestedAttribute(INN_ATTR)
        ))
        .filter(new Filter(List.of(
                new StringFilterAttribute(INN_ATTR, List.of(IN), innListFromXml.toArray(String[]::new))
        )))
        .build();
    }

    public static GetAttributesListRequestDto requestDtoToFindRecipientsByAppTypeForUpdate(long appTypeId) {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(ViewType.DETAILED_FORM_VIEW)
                .attributes(List.of(
                        new RequestedAttribute(INN_ATTR),
                        new RequestedAttribute(FULL_TITLE_ATTR),
                        new RequestedAttribute(SHORT_TITLE_ATTR),
                        new RequestedAttribute(OGRN_ATTR),
                        new RequestedAttribute(OGRN_DATE_ATTR),
                        new RequestedAttribute(KPP_ATTR)
                ))
                .filter(new Filter(List.of(
                        new LinkedFilterAttribute(APP_TYPE_ATTR, appTypeId)
                )))
                .build();
    }

    public static UpdateInstanceRequestDto requestDtoForUpdateRecipientData(SubsidyRecipient recipient, GetDataFromEgrulByInnDto egrulData) {
        return new UpdateInstanceRequestDto(
                InstanceDto.builder()
                        .id(recipient.getId())
                        .version(recipient.getVersion())
                        .attributes(defineUpdatedAttributes(recipient, egrulData))
                        .build()
        );
    }

    public static List<Attribute<?>> defineUpdatedAttributes(SubsidyRecipient recipient, GetDataFromEgrulByInnDto egrulData) {
        List<Attribute<?>> attributes = new ArrayList<>();
        if (!Objects.equals(recipient.getFullTitle(), egrulData.fullTitle())) {
            attributes.add(new StringAttribute(FULL_TITLE_ATTR, egrulData.fullTitle()));
        }
        if (!Objects.equals(recipient.getShortTitle(), egrulData.shortTitle())) {
            attributes.add(new StringAttribute(SHORT_TITLE_ATTR, egrulData.shortTitle()));
        }
        if (!Objects.equals(recipient.getOgrn(), egrulData.ogrn())) {
            attributes.add(new StringAttribute(OGRN_ATTR, egrulData.ogrn()));
        }
        if (!Objects.equals(recipient.getOgrnDate(), egrulData.ogrnDate())) {
            attributes.add(new DateAttribute(OGRN_DATE_ATTR, toEpochMilli(egrulData.ogrnDate())));
        }
        if (!Objects.equals(recipient.getKpp(), egrulData.kpp())) {
            attributes.add(new StringAttribute(KPP_ATTR, egrulData.kpp()));
        }
        return attributes;
    }
}

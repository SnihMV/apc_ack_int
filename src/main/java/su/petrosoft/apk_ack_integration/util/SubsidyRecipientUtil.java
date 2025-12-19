package su.petrosoft.apk_ack_integration.util;

import su.petrosoft.apk_ack_integration.model.dto.plicante.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.RequestedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.Filter;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.LongFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.request.GettingInstanceRepresentationRequestDto;

import java.util.List;

public class SubsidyRecipientUtil {
    public static final long TEMPLATE_ID = 3318;
    public static final String TITLE = "Журнал учета получателей государственной поддержки";
    public static final long ID_ATTR = 531;
    public static final long MACHINE_PARK_ATTR = 4321;
    public static final long INN_ATTR = 3360;
    public static final long KPP_ATTR = 3361;

    public static GetAttributesListRequestDto buildRequestDtoToFindById(Long id) {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .attributes(List.of(
                        new RequestedAttribute(MACHINE_PARK_ATTR)))
                .filter(new Filter(List.of(
                        new LongFilterAttribute(ID_ATTR, id))))
                .build();
    }

}

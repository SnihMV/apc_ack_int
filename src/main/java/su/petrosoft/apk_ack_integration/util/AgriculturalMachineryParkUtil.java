package su.petrosoft.apk_ack_integration.util;

import su.petrosoft.apk_ack_integration.model.dto.plicante.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.RequestedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.BooleanFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.Filter;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.LongFilterAttribute;
import su.petrosoft.apk_ack_integration.model.enums.SqlOperation;

import java.util.Collection;
import java.util.List;

import static su.petrosoft.apk_ack_integration.model.enums.ViewType.DETAILED_FORM_VIEW;

public class AgriculturalMachineryParkUtil {
    public static final long TEMPLATE_ID = 3297;
    public static final String TITLE = "Парк сельскохозяйственной техники и оборудования";

    public static final long STATUS_ACTIVE = 4840;
    public static final long STATUS_INACTIVE = 20674;

    public static final long ID_ATTR = 519;
    public static final long RECIPIENT_ATTR = 4322;
    public static final long DISTRICT_ATTR = 3032;
    public static final long INDICATOR_ATTR = 749;
    public static final long MACH_EQUIP_ATTR = 750;
    public static final long BRAND_MODEL_ATTR = 526;
    public static final long SERIAL_NUMBER_ATTR = 4444;
    public static final long COUNT_ATTR = 4320;
    public static final long POWER_ATTR = 527;
    public static final long COST_ATTR = 528;
    public static final long PROD_COUNTRY_ATTR = 744;
    public static final long PROD_YEAR_ATTR = 3257;
    public static final long STATE_SUPPORT_ATTR = 764;
    public static final long TECH_STATE_ATTR = 3910;

    public static GetAttributesListRequestDto requestDtoForGetParksByIdsAndSupportToValidation(Collection<Long> ids, boolean support) {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(DETAILED_FORM_VIEW)
                .attributes(List.of(
                        new RequestedAttribute(MACH_EQUIP_ATTR),
                        new RequestedAttribute(COUNT_ATTR)
                ))
                .filter(new Filter(List.of(
                        new LongFilterAttribute(ID_ATTR, List.of(SqlOperation.IN), ids),
                        new BooleanFilterAttribute(STATE_SUPPORT_ATTR, support)
                )))
                .build();
    }
}

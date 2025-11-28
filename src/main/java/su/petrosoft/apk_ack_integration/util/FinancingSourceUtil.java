package su.petrosoft.apk_ack_integration.util;

import su.petrosoft.apk_ack_integration.model.dto.request.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.ViewType;

import java.time.LocalDateTime;
import java.util.Map;

import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.makeSimpleLongAttributeFilter;

public class FinancingSourceUtil {
    public static final long TEMPLATE_ID = 25387;
    public static final String FS_TITLE = "Источник финансирования";

    public static final long YEAR_ATTR = 3393;
    public static final long KVSR_ATTR = 3449;
    public static final long KFSR_ATTR = 3450;
    public static final long KCSR_ATTR = 3451;
    public static final long KVR_ATTR = 3452;
    public static final long KOSGU_ATTR = 3453;
    public static final long DOPFK_ATTR = 3454;
    public static final long DOPEK_ATTR = 3455;
    public static final long DOPKR_ATTR = 3456;
    public static final long PURPOSE_ATTR = 3457;
    public static final long OWNERSHIP_FORM_ATTR = 3394;
    public static final long SUBSIDY_PROGRAM_ATTR = 3461;
    public static final long CASH_PLAN_LIMIT_ATTR = 3835;
    public static final long CONCAT_KBK_ATTR = 3842;

    public static InstanceDto getAllFsByCurrentYearRequestDto() {
        return InstanceDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(ViewType.DETAILED_FORM_VIEW)
                .filter(makeSimpleLongAttributeFilter(Map.of(YEAR_ATTR, LocalDateTime.now().getYear())))
                .build();
    }
}

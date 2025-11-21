package su.petrosoft.apk_ack_integration.util;

import static java.util.stream.Collectors.groupingBy;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.makeSimpleFilter;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.Filter;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.FilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.LongAttribute;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.StringAttribute;
import su.petrosoft.apk_ack_integration.model.enums.ValueType;
import su.petrosoft.apk_ack_integration.model.enums.ViewType;

@Slf4j
public class SubsidyProgramUtil {

    public static final long TEMPLATE_ID = 9492;
    public static final long LEVEL_ATTR = 3399;
    public static final long PARENT_ATTR = 3400;
    public static final long NAME_ATTR = 1759;
    public static final long CODE_ATTR = 3398;
    public static final long KCSR_ATTR = 3548;
    public static final long DOPKR_ATTR = 3549;

    public static boolean validate(SubsidyProgram sp) {
        Long lvl = sp.getLevel();
        if (lvl == null) {
            return false;
        }
        if (lvl == 1) {
            return sp.getCode() != null
                    && sp.getKcsr() == null
                    && sp.getDopKr() == null
                    && sp.getParentId() == null;
        }
        if (lvl == 2) {
            return sp.getKcsr() != null &&
                    sp.getDopKr() == null;
        }
        if (lvl == 3) {
            return sp.getKcsr() != null &&
                    sp.getDopKr() != null;
        }
        return false;
    }

    public static InstanceDto subsidyProgramsRequestDto(Map<Long, Object> filters) {
        return InstanceDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(ViewType.DETAILED_FORM_VIEW)
                .attributes(List.of(
                        new StringAttribute(NAME_ATTR),
                        new LongAttribute(CODE_ATTR),
                        new LongAttribute(LEVEL_ATTR),
                        new LinkedAttribute(PARENT_ATTR),
                        new LinkedAttribute(KCSR_ATTR),
                        new LinkedAttribute(DOPKR_ATTR)
                ))
                .filter(makeSimpleFilter(filters))
                .build();
    }

}

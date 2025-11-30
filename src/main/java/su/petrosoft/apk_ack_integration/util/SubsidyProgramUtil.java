package su.petrosoft.apk_ack_integration.util;

import lombok.extern.slf4j.Slf4j;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LongAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.Filter;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.LongFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.InstanceDto;

import java.util.List;

import static su.petrosoft.apk_ack_integration.model.enums.ViewType.DETAILED_FORM_VIEW;

@Slf4j
public class SubsidyProgramUtil {

    public static final long TEMPLATE_ID = 9492;
    public static final String SP_TITLE = "Направления (программы) субсидирования";

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

    public static InstanceDto getAllSubsidyProgramsRequestDto() {
        return InstanceDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(DETAILED_FORM_VIEW)
                .build();
    }

    public static InstanceDto getThirdLevelSpRequestDto() {
        return InstanceDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(DETAILED_FORM_VIEW)
                .attributes(List.of(
                        new StringAttribute(NAME_ATTR),
                        new LongAttribute(CODE_ATTR),
                        new LongAttribute(LEVEL_ATTR),
                        new LinkedAttribute(PARENT_ATTR),
                        new LinkedAttribute(KCSR_ATTR),
                        new LinkedAttribute(DOPKR_ATTR)
                ))
                .filter(new Filter(List.of(new LongFilterAttribute(LEVEL_ATTR, 3))))
                .build();
    }

}

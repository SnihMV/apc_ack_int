package su.petrosoft.apk_ack_integration.util;

import static su.petrosoft.apk_ack_integration.model.enums.ViewType.DETAILED_FORM_VIEW;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.dto.plicante.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.RequestedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.Filter;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.LinkedFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.LongFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;

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
    public static final long COFIN_LVL_ATTR = 3418;

    public static boolean validate(SubsidyProgram sp) {
        Long lvl = sp.getLevel();
        if (lvl == null) {
            return false;
        }
//        if (lvl == 1) {
//            return sp.getCode() != null
//                    && sp.getKcsr() == null
//                    && sp.getDopKr() == null
//                    && sp.getParentId() == null;
//        }
        if (lvl == 1) {
            return sp.getKcsr() != null &&
                    sp.getDopKr() == null;
        }
        if (lvl == 2) {
            return sp.getKcsr() != null &&
                    sp.getDopKr() != null;
        }
        return false;
    }

    public static GetAttributesListRequestDto requestDtoToFindAllSubsidyPrograms() {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(DETAILED_FORM_VIEW)
                .build();
    }

    public static GetAttributesListRequestDto requestDtoToFindSecondLevelSubsidyPrograms() {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(DETAILED_FORM_VIEW)
                .attributes(List.of(
                        new RequestedAttribute(NAME_ATTR),
                        new RequestedAttribute(LEVEL_ATTR),
                        new RequestedAttribute(PARENT_ATTR),
                        new RequestedAttribute(KCSR_ATTR),
                        new RequestedAttribute(DOPKR_ATTR)
                ))
                .filter(new Filter(List.of(
                        new LongFilterAttribute(LEVEL_ATTR, 2))))
                .build();
    }

    public static GetAttributesListRequestDto getThirdLevelSpRequestDto() {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(DETAILED_FORM_VIEW)
                .attributes(List.of(
                        new RequestedAttribute(NAME_ATTR),
                        new RequestedAttribute(CODE_ATTR),
                        new RequestedAttribute(LEVEL_ATTR),
                        new RequestedAttribute(PARENT_ATTR),
                        new RequestedAttribute(KCSR_ATTR),
                        new RequestedAttribute(DOPKR_ATTR)
                ))
                .filter(new Filter(List.of(
                        new LongFilterAttribute(LEVEL_ATTR, 3))))
                .build();
    }

    public static GetAttributesListRequestDto requestDtoToFindSubsidyProgramsForCreationCofinancingLevels() {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(DETAILED_FORM_VIEW)
                .attributes(List.of(
                        new RequestedAttribute(LEVEL_ATTR),
                        new RequestedAttribute(KCSR_ATTR),
                        new RequestedAttribute(DOPKR_ATTR),
                        new RequestedAttribute(COFIN_LVL_ATTR)))
                .filter(new Filter(List.of(
                        new LongFilterAttribute(LEVEL_ATTR, 2))))
                .build();
    }

    public static UpdateInstanceRequestDto buildUpdatingByCofinLevelsRequestDto(SubsidyProgram updatedSP) {
        return new UpdateInstanceRequestDto(
                InstanceDto.builder()
                        .id(updatedSP.getId())
                        .templateId(TEMPLATE_ID)
                        .version(updatedSP.getVersion())
                        .attributes(List.of(
                                new LinkedAttribute(COFIN_LVL_ATTR, updatedSP.getCofinancingLevelIds())))
                        .build());
    }

    public static UpdateInstanceRequestDto requestDtoForUpdatingParentId(SubsidyProgram sp) {
        return new UpdateInstanceRequestDto(
            InstanceDto.builder()
                .id(sp.getId())
                .templateId(TEMPLATE_ID)
                .version(sp.getVersion())
                .attributes(List.of(
                    new LinkedAttribute(PARENT_ATTR, sp.getParentId())))
                .build());
    }
}

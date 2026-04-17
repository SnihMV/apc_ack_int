package su.petrosoft.apk_ack_integration.util;

import lombok.extern.slf4j.Slf4j;
import su.petrosoft.apk_ack_integration.model.DictionaryData;
import su.petrosoft.apk_ack_integration.model.FinancingSource;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.RequestedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LongAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.Filter;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.LinkedFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.LongFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPKR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KCSR;
import static su.petrosoft.apk_ack_integration.model.enums.SqlOperation.IN;
import static su.petrosoft.apk_ack_integration.model.enums.ViewType.DETAILED_FORM_VIEW;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.ILLEGAL_CREATE_STATE;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.dictionaryDataById;

@Slf4j
public class SubsidyProgramUtil {
    public static final long MAX_LEVEL = 2;
    public static final long LOWEST_LEVEL = 1;


    public static final long TEMPLATE_ID = 9492;
    public static final String SP_TITLE = "Направления (программы) субсидирования";

    public static final long LEVEL_ATTR = 3399;
    public static final long PARENT_ATTR = 3400;
    public static final long TITLE_ATTR = 1759;
    public static final long KCSR_ATTR = 3548;
    public static final long DOPKR_ATTR = 3549;
    public static final long COFIN_LVL_ATTR = 3418;
    public static final long FIN_SRC_ATTR = 3460;

    public static GetAttributesListRequestDto requestDtoToGetAllPrograms() {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(DETAILED_FORM_VIEW)
                .build();
    }

    public static GetAttributesListRequestDto requestDtoToGetProgramsByKcsrIds(Collection<Long> kcsrIds) {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(DETAILED_FORM_VIEW)
                .filter(new Filter(List.of(
                        new LinkedFilterAttribute(KCSR_ATTR, List.of(IN), kcsrIds)
                )))
                .build();
    }

    public static GetAttributesListRequestDto requestDtoToFindSecondLevelSubsidyPrograms() {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(DETAILED_FORM_VIEW)
                .attributes(List.of(
                        new RequestedAttribute(TITLE_ATTR),
                        new RequestedAttribute(LEVEL_ATTR),
                        new RequestedAttribute(PARENT_ATTR),
                        new RequestedAttribute(KCSR_ATTR),
                        new RequestedAttribute(DOPKR_ATTR)
                ))
                .filter(new Filter(List.of(
                        new LongFilterAttribute(LEVEL_ATTR, 2))))
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

    public static CreateInstanceRequestDto requestDtoToCreateSubsidyProgram(SubsidyProgram program) {
        return new CreateInstanceRequestDto(InstanceDto.builder()
                .templateId(TEMPLATE_ID)
                .attributes(getAttributesToCreate(program))
                .build());
    }

    public static UpdateInstanceRequestDto requestDtoToUpdatingProgramByCofinLevels(SubsidyProgram updatedSP) {
        return new UpdateInstanceRequestDto(
                InstanceDto.builder()
                        .id(updatedSP.getId())
                        .templateId(TEMPLATE_ID)
                        .version(updatedSP.getVersion())
                        .attributes(List.of(
                                new LinkedAttribute(COFIN_LVL_ATTR, updatedSP.getCofinLevelIds())))
                        .build());
    }

    public static UpdateInstanceRequestDto requestDtoToUpdatingProgramByParentId(SubsidyProgram sp) {
        return new UpdateInstanceRequestDto(
                InstanceDto.builder()
                        .id(sp.getId())
                        .templateId(TEMPLATE_ID)
                        .version(sp.getVersion())
                        .attributes(List.of(
                                new LinkedAttribute(PARENT_ATTR, sp.getParentId())))
                        .build());
    }

    public static SubsidyProgram extractFstLvlSpFromFs(FinancingSource fs) {
        return SubsidyProgram.builder()
                .level(1L)
                .kcsr(fs.getKcsr())
                .build();
    }

    public static SubsidyProgram extractScdLvlSpFromFs(FinancingSource fs) {
        return SubsidyProgram.builder()
                .level(2L)
                .kcsr(fs.getKcsr())
                .dopKr(fs.getDopKr())
                .build();
    }

    public static String defineTitle(SubsidyProgram program, Map<Dictionary, Map<DictionaryData, Long>> dictionaryMap) {
        if (program.getLevel() == 1) {
            return dictionaryDataById(dictionaryMap, KCSR, program.getKcsr()).getDescription();
        } else if (program.getLevel() == 2) {
            return dictionaryDataById(dictionaryMap, DOPKR, program.getDopKr()).getDescription();
        } else throw new IllegalStateException(ILLEGAL_CREATE_STATE);
    }

    private static List<Attribute<?>> getAttributesToCreate(SubsidyProgram sp) {
        if (!validateToCreate(sp)) {
            throw new IllegalStateException(ILLEGAL_CREATE_STATE);
        }
        List<Attribute<?>> attributes = new ArrayList<>(List.of(
                new LongAttribute(LEVEL_ATTR, sp.getLevel()),
                new LinkedAttribute(KCSR_ATTR, sp.getKcsr()),
                new StringAttribute(TITLE_ATTR, sp.getTitle())
        ));
        if (sp.getLevel() == 2) {
            attributes.add(new LinkedAttribute(DOPKR_ATTR, sp.getDopKr()));
            attributes.add(new LinkedAttribute(PARENT_ATTR, sp.getParentId()));
        }
        if (sp.getFinancingSourceIds() != null && !sp.getFinancingSourceIds().isEmpty()) {
            attributes.add(new LinkedAttribute(FIN_SRC_ATTR, sp.getFinancingSourceIds()));
        }
        if (sp.getCofinLevelIds() != null && !sp.getCofinLevelIds().isEmpty()) {
            attributes.add(new LinkedAttribute(COFIN_LVL_ATTR, sp.getCofinLevelIds()));
        }
        return attributes;
    }

    private static boolean validateToCreate(SubsidyProgram sp) {
        if (sp.getLevel() == null || sp.getTitle() == null || sp.getKcsr() == null) {
            return false;
        }
        if (sp.getLevel() == 1) {
            return sp.getDopKr() == null && sp.getParentId() == null;
        }
        if (sp.getLevel() == 2) {
            return sp.getDopKr() != null && sp.getParentId() != null;
        }
        return false;
    }

    public static SubsidyProgram extractParentKey(SubsidyProgram program) {
        return SubsidyProgram.builder()
                .level(program.getLevel() - 1)
                .kcsr(program.getKcsr())
                .build();
    }
}

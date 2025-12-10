package su.petrosoft.apk_ack_integration.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.CofinancingLevel;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LongAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.LinkedValue;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.CofinancingLevelExcelRow;
import su.petrosoft.apk_ack_integration.model.excel.UniBudgetCodedExcelRow;
import su.petrosoft.apk_ack_integration.util.CropProductionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static su.petrosoft.apk_ack_integration.model.enums.CodeType.DOPKR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KCSR;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractAllData;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractData;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractShortForm;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.getCodeId;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.CODE_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.COFIN_LVL_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.DOPKR_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.KCSR_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.LEVEL_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.NAME_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.PARENT_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.TEMPLATE_ID;

@Slf4j
@Component
public class SubsidyProgramMapper {

    public SubsidyProgram toEntity(InstanceDto dto) {
        List<Attribute<?>> attributes = dto.attributes();
        return SubsidyProgram.builder()
                .id(dto.id())
                .version(dto.version())
                .title(extractData(attributes, NAME_ATTR))
                .code(extractData(attributes, CODE_ATTR))
                .level(extractData(attributes, LEVEL_ATTR))
                .parentId(extractData(attributes, PARENT_ATTR))
                .kcsr(extractShortForm(attributes, KCSR_ATTR))
                .dopKr(extractShortForm(attributes, DOPKR_ATTR))
                .cofinancingLevelIds(extractAllData(attributes, COFIN_LVL_ATTR))
                .build();
    }

    public SubsidyProgram toFirstLevelSP(UniBudgetCodedExcelRow dto) {
        return SubsidyProgram.builder()
                .level(1L)
                .code(dto.code())
                .title("Направление № " + dto.code())
                .build();
    }

    public SubsidyProgram toSecondLevelSP(UniBudgetCodedExcelRow dto) {
        return SubsidyProgram.builder()
                .level(2L)
                .title(dto.kcsrTitle())
                .kcsr(dto.kcsr())
                .build();
    }

    public SubsidyProgram toThirdLevelSP(UniBudgetCodedExcelRow dto) {
        return SubsidyProgram.builder()
                .level(3L)
                .title(dto.dopKrTitle())
                .kcsr(dto.kcsr())
                .dopKr(dto.dopKr())
                .build();
    }

    public CreateInstanceRequestDto toCreateDto(SubsidyProgram sp, Map<CodeType, Map<Long, String>> codesMap) {
        List<Attribute<?>> attributes = new ArrayList<>(List.of(
                new StringAttribute(NAME_ATTR, sp.getTitle()),
                new StringAttribute(CODE_ATTR, sp.getCode()),
                new LongAttribute(LEVEL_ATTR, sp.getLevel()),
                new LinkedAttribute(PARENT_ATTR, sp.getParentId())));
        if (sp.getLevel() != 1) {
            attributes.add(new LinkedAttribute(KCSR_ATTR, getCodeId(codesMap, KCSR, sp.getKcsr())));
        }
        if (sp.getLevel() == 3) {
            attributes.add(new LinkedAttribute(DOPKR_ATTR, getCodeId(codesMap, DOPKR, sp.getDopKr())));
        }
        return new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(TEMPLATE_ID)
                        .attributes(attributes)
                        .build()
        );
    }

    public SubsidyProgram toEntity(CofinancingLevelExcelRow row) {
        return SubsidyProgram.builder()
                .level(3L)
                .kcsr(row.kcsr())
                .dopKr(row.dopKr())
                .build();
    }


    public UpdateInstanceRequestDto toUpdatingDto(SubsidyProgram subsidyProgram) {
        return new UpdateInstanceRequestDto(
                InstanceDto.builder()
                        .id(subsidyProgram.getId())
                        .templateId(CropProductionUtil.TEMPLATE_ID)
                        .version(subsidyProgram.getVersion())
                        .attributes(buildAttributeListToUpdate(subsidyProgram))
                        .build()
        );
    }

    private List<Attribute<?>> buildAttributeListToUpdate(SubsidyProgram subsidyProgram) {
        return List.of(
                new LinkedAttribute(COFIN_LVL_ATTR, getLinkedValuesFromIds(subsidyProgram.getCofinancingLevelIds()))
        );
    }

    private List<LinkedValue> getLinkedValuesFromIds(List<Long> linkedIds) {
        return linkedIds.stream()
                .map(LinkedValue::new)
                .toList();
    }
//
//    private Object getAttrData(List<Attribute<?>> attributes, Long attributeId) {
//        return attributes.stream()
//                .filter(a -> a.id().equals(attributeId))
//                .findFirst()
//                .map(Attribute::getData)
//                .orElse(null);
//    }
//
//    private String getAttrShortForm(List<Attribute<?>> attributes, Long attributeId) {
//        return attributes.stream()
//                .filter(a -> a.id().equals(attributeId))
//                .findFirst()
//                .map(Attribute::getShortForm)
//                .orElse(null);
//    }
}

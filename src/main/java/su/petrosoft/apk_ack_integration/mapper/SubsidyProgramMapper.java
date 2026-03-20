package su.petrosoft.apk_ack_integration.mapper;

import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPKR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KCSR;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.dictionaryIdByCode;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractAllData;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractData;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.COFIN_LVL_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.DOPKR_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.KCSR_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.LEVEL_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.NAME_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.PARENT_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.TEMPLATE_ID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.data.CofinancingLevelData;
import su.petrosoft.apk_ack_integration.model.data.DescriptedBudgetItemData;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.AttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LongAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.LinkedValue;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;
import su.petrosoft.apk_ack_integration.util.CropProductionUtil;

@Slf4j
@Component
public class SubsidyProgramMapper {

    public SubsidyProgram toEntity(InstanceDto dto) {
        List<AttributeDto<?>> attributeDtos = dto.attributeDtos();
        long level = extractData(attributeDtos, LEVEL_ATTR);
        return SubsidyProgram.builder()
            .id(dto.id())
            .version(dto.version())
            .title(extractData(attributeDtos, NAME_ATTR))
            .level(level)
            .parentId(extractData(attributeDtos, PARENT_ATTR))
            .kcsr(extractData(attributeDtos, KCSR_ATTR))
            .dopKr(level == 2 ?  extractData(attributeDtos, DOPKR_ATTR) : null)
            .cofinancingLevelIds(extractAllData(attributeDtos, COFIN_LVL_ATTR))
            .build();
    }

//    public SubsidyProgram toFirstLevelSP(DescriptedBudgetItemData dto) {
//        return SubsidyProgram.builder()
//                .level(1L)
//                .code(dto.code())
//                .title("Направление № " + dto.code())
//                .build();
//    }

    public SubsidyProgram toFirstLevelSP(DescriptedBudgetItemData dto, Map<Dictionary, Map<String, Long>> codesMap) {
        return SubsidyProgram.builder()
            .level(1L)
            .title(dto.kcsrTitle())
            .kcsr(dictionaryIdByCode(codesMap, KCSR, dto.kcsr()))
                .build();
    }

    public SubsidyProgram toSecondLevelSP(DescriptedBudgetItemData dto, Map<Dictionary, Map<String, Long>> codesMap) {
        return SubsidyProgram.builder()
                .level(2L)
                .title(dto.dopKrTitle())
                .kcsr(dictionaryIdByCode(codesMap, KCSR, dto.kcsr()))
                .dopKr(dictionaryIdByCode(codesMap, DOPKR, dto.dopKr()))
                .build();
    }

    public CreateInstanceRequestDto toCreateDto(SubsidyProgram sp) {
        List<AttributeDto<?>> attributeDtos = new ArrayList<>(List.of(
                new StringAttributeDto(NAME_ATTR, sp.getTitle()),
//                new StringAttribute(CODE_ATTR, sp.getCode()),
                new LongAttributeDto(LEVEL_ATTR, sp.getLevel()),
                new LinkedAttributeDto(PARENT_ATTR, sp.getParentId()),
                new LinkedAttributeDto(KCSR_ATTR,  sp.getKcsr())));
        if (sp.getLevel() == 2) {
            attributeDtos.add(new LinkedAttributeDto(DOPKR_ATTR,  sp.getDopKr()));
        }
        return new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(TEMPLATE_ID)
                        .attributes(attributeDtos)
                        .build()
        );
    }

    public SubsidyProgram toEntity(CofinancingLevelData row, Map<Dictionary, Map<String, Long>> codesMap) {
        return SubsidyProgram.builder()
                .level(2L)
                .kcsr(dictionaryIdByCode(codesMap, KCSR, row.kcsr()))
                .dopKr(dictionaryIdByCode(codesMap, DOPKR, row.dopKr()))
                .build();
    }


    public UpdateInstanceRequestDto toUpdatingDto(SubsidyProgram subsidyProgram) {
        return new UpdateInstanceRequestDto(
                InstanceDto.builder()
                        .id(subsidyProgram.getId())
                        .templateId(CropProductionUtil.TEMPLATE_ID)
                        .version(subsidyProgram.getVersion())
                        .attributes(List.of(
                                new LinkedAttributeDto(COFIN_LVL_ATTR, subsidyProgram.getCofinancingLevelIds())))
                        .build());
    }

    private List<LinkedValue> getLinkedValuesFromIds(List<Long> linkedIds) {
        return linkedIds.stream()
                .map(LinkedValue::new)
                .toList();
    }
}

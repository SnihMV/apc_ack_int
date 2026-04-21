package su.petrosoft.apk_ack_integration.mapper;

import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPKR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KCSR;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.dictionaryIdByCode;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractAllData;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractData;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.COFIN_LVL_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.DOPKR_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.FIN_SRC_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.KCSR_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.LEVEL_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.TITLE_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.PARENT_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.TEMPLATE_ID;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.DictionaryData;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.data.CofinancingLevelData;
import su.petrosoft.apk_ack_integration.model.data.DescriptedBudgetItemData;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LongAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

@Slf4j
@Component
public class SubsidyProgramMapper {

    public SubsidyProgram toEntity(InstanceDto dto) {
        List<Attribute<?>> attributes = dto.attributes();
        long level = extractData(attributes, LEVEL_ATTR);
        return SubsidyProgram.builder()
                .id(dto.id())
                .version(dto.version())
                .title(extractData(attributes, TITLE_ATTR))
                .level(level)
                .parentId(extractData(attributes, PARENT_ATTR))
                .kcsr(extractData(attributes, KCSR_ATTR))
                .dopKr(level == 2 ? extractData(attributes, DOPKR_ATTR) : null)
                .financingSourceIds(new HashSet<>(extractAllData(attributes, FIN_SRC_ATTR)))
                .cofinLevelIds(new HashSet<>(extractAllData(attributes, COFIN_LVL_ATTR)))
                .build();
    }

    public SubsidyProgram toFirstLevelSP(DescriptedBudgetItemData dto, Map<Dictionary, Map<DictionaryData, Long>> codesMap) {
        return SubsidyProgram.builder()
                .level(1L)
                .title(dto.kcsrTitle())
                .kcsr(dictionaryIdByCode(codesMap, KCSR, dto.kcsr()))
                .build();
    }

    public SubsidyProgram toSecondLevelSP(DescriptedBudgetItemData dto, Map<Dictionary, Map<DictionaryData, Long>> codesMap) {
        return SubsidyProgram.builder()
                .level(2L)
                .title(dto.dopKrTitle())
                .kcsr(dictionaryIdByCode(codesMap, KCSR, dto.kcsr()))
                .dopKr(dictionaryIdByCode(codesMap, DOPKR, dto.dopKr()))
                .build();
    }

    public CreateInstanceRequestDto toCreateDto(SubsidyProgram sp) {
        List<Attribute<?>> attributes = new ArrayList<>(List.of(
                new StringAttribute(TITLE_ATTR, sp.getTitle()),
                new LongAttribute(LEVEL_ATTR, sp.getLevel()),
                new LinkedAttribute(PARENT_ATTR, sp.getParentId()),
                new LinkedAttribute(KCSR_ATTR, sp.getKcsr())));
        if (sp.getLevel() == 2) {
            attributes.add(new LinkedAttribute(DOPKR_ATTR, sp.getDopKr()));
        }
        return new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(TEMPLATE_ID)
                        .attributes(attributes)
                        .build()
        );
    }

    public SubsidyProgram toEntity(CofinancingLevelData row, Map<Dictionary, Map<DictionaryData, Long>> codesMap) {
        return SubsidyProgram.builder()
                .level(2L)
                .kcsr(dictionaryIdByCode(codesMap, KCSR, row.kcsr()))
                .dopKr(dictionaryIdByCode(codesMap, DOPKR, row.dopKr()))
                .build();
    }

}

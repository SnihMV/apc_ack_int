package su.petrosoft.apk_ack_integration.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.dto.request.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.LongAttribute;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.StringAttribute;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.UniBudgetCodedExcelRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static su.petrosoft.apk_ack_integration.model.enums.CodeType.*;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.getCodeId;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.CODE_ATTR;
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
                .title((String) getAttrData(attributes, NAME_ATTR))
                .code((String) getAttrData(attributes, CODE_ATTR))
                .level((Long) getAttrData(attributes, LEVEL_ATTR))
                .parentId((Long) getAttrData(attributes, PARENT_ATTR))
                .kcsr(getAttrShortForm(attributes, KCSR_ATTR))
                .dopKr(getAttrShortForm(attributes, DOPKR_ATTR))
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

    private Object getAttrData(List<Attribute<?>> attributes, Long attributeId) {
        return attributes.stream()
                .filter(a -> a.id().equals(attributeId))
                .findFirst()
                .map(Attribute::getData)
                .orElse(null);
    }

    private String getAttrShortForm(List<Attribute<?>> attributes, Long attributeId) {
        return attributes.stream()
                .filter(a -> a.id().equals(attributeId))
                .findFirst()
                .map(Attribute::getShortForm)
                .orElse(null);
    }
}

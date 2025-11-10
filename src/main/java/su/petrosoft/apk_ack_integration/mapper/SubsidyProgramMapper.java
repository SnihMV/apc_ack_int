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

import java.util.List;

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

    public SubsidyProgram toSp(InstanceDto dto) {
        List<Attribute> attributes = dto.attributes();
        return SubsidyProgram.builder()
                .id(dto.id())
                .version(dto.version())
                .title((String) getAttrValue(attributes, NAME_ATTR))
                .code((String) getAttrValue(attributes, CODE_ATTR))
                .level((Long) getAttrValue(attributes, LEVEL_ATTR))
                .parentId((Long) getAttrValue(attributes, PARENT_ATTR))
                .kcsr((Long) getAttrValue(attributes, KCSR_ATTR))
                .dopKr((Long) getAttrValue(attributes, DOPKR_ATTR))
                .build();
    }

    public CreateInstanceRequestDto toCreateDto(SubsidyProgram sp) {
        return new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(TEMPLATE_ID)
                        .attributes(getAttributes(sp))
                        .build()
        );
    }

    private List<Attribute> getAttributes(SubsidyProgram sp) {
        return List.of(
                new StringAttribute(NAME_ATTR, sp.getTitle()),
                new StringAttribute(CODE_ATTR, sp.getCode()),
                new LongAttribute(LEVEL_ATTR, sp.getLevel()),
                new LinkedAttribute(PARENT_ATTR, sp.getParentId()),
                new LinkedAttribute(KCSR_ATTR, sp.getKcsr()),
                new LinkedAttribute(DOPKR_ATTR, sp.getDopKr())
        );
    }

    private Object getAttrValue(List<Attribute> attributes, Long attributeId) {

        return attributes.stream()
                .filter(a -> a.id().equals(attributeId))
                .peek(System.out::println)
                .findFirst()
                .map(Attribute::getData)
                .orElse(null);
    }
}

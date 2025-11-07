package su.petrosoft.apk_ack_integration.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.dto.response.GetAttributesListResponseDto;
import su.petrosoft.apk_ack_integration.model.excel.SubsidyProgramExcelRowDto;

import java.util.List;
import java.util.function.Function;

import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.CODE_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.DOPKR_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.KCSR_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.LEVEL_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.NAME_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.PARENT_ATTR;

@Slf4j
@Component
public class SubsidyProgramMapper {

    public SubsidyProgram toSp(GetAttributesListResponseDto dto) {
        List<GetAttributesListResponseDto.Attribute> attributes = dto.attributes();
        return new SubsidyProgram(
                dto.id(),
                dto.version(),
                getAttrValue(attributes, PARENT_ATTR, Long::valueOf),
                getAttrValue(attributes, LEVEL_ATTR, Long::valueOf),
                getAttrValue(attributes, CODE_ATTR, Function.identity()),
                getAttrValue(attributes, KCSR_ATTR, Function.identity()),
                getAttrValue(attributes, DOPKR_ATTR, Function.identity()),
                getAttrValue(attributes, NAME_ATTR, Function.identity())
        );
    }


    private <R> R getAttrValue(List<GetAttributesListResponseDto.Attribute> attributes, Long attributeId, Function<String, R> function) {
        return attributes.stream()
                .filter(a -> a.id().equals(attributeId))
                .findFirst()
                .map(a -> a.value())
                .map(v -> v.get(0).data())
                .map(function)
                .orElse(null);
    }
}

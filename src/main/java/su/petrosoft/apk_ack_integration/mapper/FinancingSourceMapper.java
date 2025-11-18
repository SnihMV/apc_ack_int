package su.petrosoft.apk_ack_integration.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.FinancingSource;
import su.petrosoft.apk_ack_integration.model.dto.request.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.LongAttribute;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.UniBudgetExcelRowDto;
import su.petrosoft.apk_ack_integration.util.FinancingSourceUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static su.petrosoft.apk_ack_integration.model.enums.CodeType.*;
import static su.petrosoft.apk_ack_integration.util.DictionaryUtil.getCodeId;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.DIRECTION_ATTR;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.KCSR_ATTR;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.KFSR_ATTR;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.KOSGU_ATTR;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.KVR_ATTR;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.KVSR_ATTR;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.TEMPLATE_ID;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.YEAR_ATTR;

@Slf4j
@Component
public class FinancingSourceMapper {

    public FinancingSource toEntity(InstanceDto dto) {
        List<Attribute> attributes = dto.attributes();
        return FinancingSource.builder()
                .id(dto.id())
                .version(dto.version())
                .year((Long) getAttrData(attributes, YEAR_ATTR))
                .kvsr(getAttrShortForm(attributes, KVSR_ATTR))
                .kfsr(getAttrShortForm(attributes, KFSR_ATTR))
                .kcsr(getAttrShortForm(attributes, KCSR_ATTR))
                .kvr(getAttrShortForm(attributes, KVR_ATTR))
                .kosgu(getAttrShortForm(attributes, 3453L))
                .dopEk(getAttrShortForm(attributes, 3455L))
                .dopFk(getAttrShortForm(attributes, 3454L))
                .dopKr(getAttrShortForm(attributes, 3456L))
                .purpose(getAttrShortForm(attributes, 3457L))
                .subsidyProgramId((Long) getAttrData(attributes, DIRECTION_ATTR))
                .build();
    }

    public FinancingSource fromUniBudgetDto(UniBudgetExcelRowDto dto) {
        return FinancingSource.builder()
                .year((long) LocalDate.now().getYear())
                .kvsr(dto.kvsr())
                .kfsr(dto.section()+dto.subsection())
                .kcsr(dto.kcsr())
                .kvr(dto.kvr())
                .kosgu(dto.kosgu())
                .dopKr(dto.dopKr())
                .dopEk(dto.dopEk())
                .dopFk(dto.dopFk())
                .purpose(dto.purposeCode())
                .build();
    }

    public CreateInstanceRequestDto toCreateDto(FinancingSource fs, Map<CodeType, Map<Long, String>> codesMap) {
        return new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(TEMPLATE_ID)
                        .attributes(List.of(
                                new LongAttribute(YEAR_ATTR, fs.getYear()),
                                new LinkedAttribute(KVSR_ATTR, getCodeId(codesMap, KADMR, fs.getKvsr())),
                                new LinkedAttribute(KFSR_ATTR, getCodeId(codesMap, KFSR, fs.getKfsr())),
                                new LinkedAttribute(KCSR_ATTR, getCodeId(codesMap, KCSR, fs.getKcsr())),
                                new LinkedAttribute(KVR_ATTR, getCodeId(codesMap, KVR, fs.getKvr())),
                                new LinkedAttribute(KOSGU_ATTR, getCodeId(codesMap, KESR, fs.getKosgu())),
                                new LinkedAttribute(3455L, getCodeId(codesMap, KDE, fs.getDopEk())),
                                new LinkedAttribute(3454L, getCodeId(codesMap, KDF, fs.getDopFk())),
                                new LinkedAttribute(3456L, getCodeId(codesMap, KDR, fs.getDopKr())),
                                new LinkedAttribute(3457L, getCodeId(codesMap, PURPOSEFULGRANT, fs.getPurpose())),
                                new LinkedAttribute(DIRECTION_ATTR, fs.getSubsidyProgramId())
                        ))
                        .build()
        );
    }
    private Object getAttrData(List<Attribute> attributes, Long attributeId) {
        return attributes.stream()
                .filter(a -> a.id().equals(attributeId))
                .findFirst()
                .map(Attribute::getData)
                .orElse(null);
    }

    private String getAttrShortForm(List<Attribute> attributes, Long attributeId) {
        return attributes.stream()
                .filter(a -> a.id().equals(attributeId))
                .findFirst()
                .map(Attribute::getShortForm)
                .orElse(null);
    }
}
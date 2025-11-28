package su.petrosoft.apk_ack_integration.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.FinancingSource;
import su.petrosoft.apk_ack_integration.model.dto.request.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.LongAttribute;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.StringAttribute;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.UniBudgetCodedExcelRow;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static su.petrosoft.apk_ack_integration.model.enums.CodeType.*;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.*;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.getCodeId;

@Slf4j
@Component
public class FinancingSourceMapper {

    public FinancingSource toEntity(InstanceDto dto) {
        List<Attribute<?>> attributes = dto.attributes();
        return FinancingSource.builder()
                .id(dto.id())
                .version(dto.version())
                .year((Long) getAttrData(attributes, YEAR_ATTR))
                .kvsr(getAttrShortForm(attributes, KVSR_ATTR))
                .kfsr(getAttrShortForm(attributes, KFSR_ATTR))
                .kcsr(getAttrShortForm(attributes, KCSR_ATTR))
                .kvr(getAttrShortForm(attributes, KVR_ATTR))
                .kosgu(getAttrShortForm(attributes, KOSGU_ATTR))
                .dopFk(getAttrShortForm(attributes, DOPFK_ATTR))
                .dopEk(getAttrShortForm(attributes, DOPEK_ATTR))
                .dopKr(getAttrShortForm(attributes, DOPKR_ATTR))
                .purpose(getAttrShortForm(attributes, PURPOSE_ATTR))
                .ownershipForm(getAttrShortForm(attributes, OWNERSHIP_FORM_ATTR))
                .subsidyProgramId((Long) getAttrData(attributes, SUBSIDY_PROGRAM_ATTR))
                .cashPlanLimitId((Long) getAttrData(attributes, CASH_PLAN_LIMIT_ATTR))
                .concatenatedKBK((String) getAttrData(attributes, CONCAT_KBK_ATTR))
                .build();
    }

    public FinancingSource toEntity(UniBudgetCodedExcelRow row) {
        return FinancingSource.builder()
                .year((long) LocalDate.now().getYear())
                .kvsr(row.kvsr())
                .kfsr(row.section() + row.subsection())
                .kcsr(row.kcsr())
                .kvr(row.kvr())
                .kosgu(row.kosgu())
                .dopFk(row.dopFk())
                .dopEk(row.dopEk())
                .dopKr(row.dopKr())
                .purpose(row.purpose())
                .ownershipForm(defineOwnershipForm(row))
                .concatenatedKBK(concatKBK(row))
                .build();
    }

    public CreateInstanceRequestDto toCreateDto(FinancingSource fs, Map<CodeType, Map<Long, String>> codesMap) {
        return new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(TEMPLATE_ID)
                        .attributes(List.of(
                                new LongAttribute(YEAR_ATTR, fs.getYear()),
                                new LinkedAttribute(KVSR_ATTR, getCodeId(codesMap, KVSR, fs.getKvsr())),
                                new LinkedAttribute(KFSR_ATTR, getCodeId(codesMap, KFSR, fs.getKfsr())),
                                new LinkedAttribute(KCSR_ATTR, getCodeId(codesMap, KCSR, fs.getKcsr())),
                                new LinkedAttribute(KVR_ATTR, getCodeId(codesMap, KVR, fs.getKvr())),
                                new LinkedAttribute(KOSGU_ATTR, getCodeId(codesMap, KOSGU, fs.getKosgu())),
                                new LinkedAttribute(DOPEK_ATTR, getCodeId(codesMap, DOPEK, fs.getDopEk())),
                                new LinkedAttribute(DOPFK_ATTR, getCodeId(codesMap, DOPFK, fs.getDopFk())),
                                new LinkedAttribute(DOPKR_ATTR, getCodeId(codesMap, DOPKR, fs.getDopKr())),
                                new LinkedAttribute(PURPOSE_ATTR, getCodeId(codesMap, PURPOSE, fs.getPurpose())),
                                new LinkedAttribute(OWNERSHIP_FORM_ATTR, getCodeId(codesMap, OWNERSHIP_FORM, fs.getOwnershipForm())),
                                new LinkedAttribute(SUBSIDY_PROGRAM_ATTR, fs.getSubsidyProgramId()),
                                new LinkedAttribute(CASH_PLAN_LIMIT_ATTR, fs.getCashPlanLimitId()),
                                new StringAttribute(CONCAT_KBK_ATTR, fs.getConcatenatedKBK())
                        ))
                        .build()
        );
    }

    private String defineOwnershipForm(UniBudgetCodedExcelRow row) {
        return switch (row.kosgu()) {
            case "244" -> "гос";
            case "245" -> "негос";
            case "246" -> "ИП";
            default -> "все";
        };
    }

    private String concatKBK(UniBudgetCodedExcelRow row) {
        StringBuilder sb = new StringBuilder();
        sb.append(LocalDateTime.now().getYear());
        sb.append("-");
        sb.append(row.kvsr());
        sb.append(row.section());
        sb.append(row.subsection());
        sb.append(row.kcsr());
        sb.append(row.kvr());
        sb.append("-");
        sb.append(row.dopKr());
        return sb.toString();
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
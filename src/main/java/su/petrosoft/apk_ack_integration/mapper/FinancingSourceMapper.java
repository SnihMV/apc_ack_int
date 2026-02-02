package su.petrosoft.apk_ack_integration.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.FinancingSource;
import su.petrosoft.apk_ack_integration.model.data.BudgetItemData;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LongAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttribute;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;
import su.petrosoft.apk_ack_integration.model.enums.OwnershipForm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.*;
import static su.petrosoft.apk_ack_integration.model.enums.OwnershipForm.*;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.*;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractData;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractShortForm;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.dictionaryIdByCode;

@Slf4j
@Component
public class FinancingSourceMapper {

    public FinancingSource toEntity(InstanceDto dto) {
        List<Attribute<?>> attributes = dto.attributes();
        return FinancingSource.builder()
                .id(dto.id())
                .version(dto.version())
                .year(extractData(attributes, YEAR_ATTR))
                .kvsr(extractShortForm(attributes, KVSR_ATTR))
                .kfsr(extractShortForm(attributes, KFSR_ATTR))
                .kcsr(extractShortForm(attributes, KCSR_ATTR))
                .kvr(extractShortForm(attributes, KVR_ATTR))
                .kosgu(extractShortForm(attributes, KOSGU_ATTR))
                .dopFk(extractShortForm(attributes, DOPFK_ATTR))
                .dopEk(extractShortForm(attributes, DOPEK_ATTR))
                .dopKr(extractShortForm(attributes, DOPKR_ATTR))
                .purpose(extractShortForm(attributes, PURPOSE_ATTR))
                .ownershipForm(extractShortForm(attributes, OWNERSHIP_FORM_ATTR))
                .subsidyProgramId(extractData(attributes, SUBSIDY_PROGRAM_ATTR))
                .cashPlanLimitId(extractData(attributes, CASH_PLAN_LIMIT_ATTR))
                .concatenatedKBK(extractData(attributes, CONCAT_KBK_ATTR))
                .build();
    }

    public FinancingSource toEntity(BudgetItemData row) {
        return FinancingSource.builder()
                .year((long) LocalDate.now().getYear())
                .kvsr(row.kvsr())
                .kfsr(row.kfsr())
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

    public CreateInstanceRequestDto toCreatingDto(FinancingSource fs, Map<Dictionary, Map<String, Long>> codesMap) {
        return new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(TEMPLATE_ID)
                        .attributes(List.of(
                                new LongAttribute(YEAR_ATTR, fs.getYear()),
                                new LinkedAttribute(KVSR_ATTR, dictionaryIdByCode(codesMap, KVSR, fs.getKvsr())),
                                new LinkedAttribute(KFSR_ATTR, dictionaryIdByCode(codesMap, KFSR, fs.getKfsr())),
                                new LinkedAttribute(KCSR_ATTR, dictionaryIdByCode(codesMap, KCSR, fs.getKcsr())),
                                new LinkedAttribute(KVR_ATTR, dictionaryIdByCode(codesMap, KVR, fs.getKvr())),
                                new LinkedAttribute(KOSGU_ATTR, dictionaryIdByCode(codesMap, KOSGU, fs.getKosgu())),
                                new LinkedAttribute(DOPEK_ATTR, dictionaryIdByCode(codesMap, DOPEK, fs.getDopEk())),
                                new LinkedAttribute(DOPFK_ATTR, dictionaryIdByCode(codesMap, DOPFK, fs.getDopFk())),
                                new LinkedAttribute(DOPKR_ATTR, dictionaryIdByCode(codesMap, DOPKR, fs.getDopKr())),
                                new LinkedAttribute(PURPOSE_ATTR, dictionaryIdByCode(codesMap, PURPOSE, fs.getPurpose())),
                                new LinkedAttribute(OWNERSHIP_FORM_ATTR, dictionaryIdByCode(codesMap, OWNERSHIP_FORM, fs.getOwnershipForm())),
                                new LinkedAttribute(SUBSIDY_PROGRAM_ATTR, fs.getSubsidyProgramId()),
                                new LinkedAttribute(CASH_PLAN_LIMIT_ATTR, fs.getCashPlanLimitId()),
                                new StringAttribute(CONCAT_KBK_ATTR, fs.getConcatenatedKBK())
                        ))
                        .build()
        );
    }

    private String defineOwnershipForm(BudgetItemData row) {
        return Arrays.stream(OwnershipForm.values())
                .filter(form -> form.getKosgu().equals(row.kosgu()))
                .findFirst()
                .map(OwnershipForm::getCode)
                .orElse(ALL.getCode());
    }

    private String concatKBK(BudgetItemData row) {
        StringBuilder sb = new StringBuilder();
        sb.append(LocalDateTime.now().getYear());
        sb.append("-");
        sb.append(row.kvsr());
        sb.append(row.kfsr());
        sb.append(row.kcsr());
        sb.append(row.kvr());
        sb.append("-");
        sb.append(row.dopKr());
        return sb.toString();
    }
}
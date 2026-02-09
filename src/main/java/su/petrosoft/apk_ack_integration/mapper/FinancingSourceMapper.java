package su.petrosoft.apk_ack_integration.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.FinancingSource;
import su.petrosoft.apk_ack_integration.model.data.BudgetItemData;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LongAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;
import su.petrosoft.apk_ack_integration.model.enums.OwnershipForm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPEK;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPFK;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPKR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KCSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KFSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KOSGU;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KVR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KVSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.OWNERSHIP_FORM;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.PURPOSE;
import static su.petrosoft.apk_ack_integration.model.enums.OwnershipForm.ALL;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.CASH_PLAN_LIMITS_ATTR;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.CONCAT_KBK_ATTR;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.DOPEK_ATTR;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.DOPFK_ATTR;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.DOPKR_ATTR;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.KCSR_ATTR;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.KFSR_ATTR;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.KOSGU_ATTR;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.KVR_ATTR;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.KVSR_ATTR;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.OWNERSHIP_FORM_ATTR;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.PURPOSE_ATTR;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.SUBSIDY_PROGRAM_ATTR;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.TEMPLATE_ID;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.YEAR_ATTR;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.dictionaryCodeById;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.dictionaryIdByCode;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractAllData;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractData;

@Slf4j
@Component
public class FinancingSourceMapper {

    public FinancingSource toEntity(InstanceDto dto, Map<Dictionary, Map<Long, Map.Entry<String, String>>> codesMap) {
        List<Attribute<?>> attributes = dto.attributes();
        return FinancingSource.builder()
                .id(dto.id())
                .version(dto.version())
                .year(extractData(attributes, YEAR_ATTR))
                .kvsr(dictionaryCodeById(codesMap, KVSR, extractData(attributes, KVSR_ATTR)))
                .kfsr(dictionaryCodeById(codesMap, KFSR, extractData(attributes, KFSR_ATTR)))
                .kcsr(dictionaryCodeById(codesMap, KCSR, extractData(attributes, KCSR_ATTR)))
                .kvr(dictionaryCodeById(codesMap, KVR, extractData(attributes, KVR_ATTR)))
                .kosgu(dictionaryCodeById(codesMap, KOSGU, extractData(attributes, KOSGU_ATTR)))
                .dopFk(dictionaryCodeById(codesMap, DOPFK, extractData(attributes, DOPFK_ATTR)))
                .dopEk(dictionaryCodeById(codesMap, DOPEK, extractData(attributes, DOPEK_ATTR)))
                .dopKr(dictionaryCodeById(codesMap, DOPKR, extractData(attributes, DOPKR_ATTR)))
                .purpose(dictionaryCodeById(codesMap, PURPOSE, extractData(attributes, PURPOSE_ATTR)))
                .ownershipForm(dictionaryCodeById(codesMap, OWNERSHIP_FORM, extractData(attributes, OWNERSHIP_FORM_ATTR)))
                .subsidyProgramId(extractData(attributes, SUBSIDY_PROGRAM_ATTR))
                .cashPlanLimitIds(extractAllData(attributes, CASH_PLAN_LIMITS_ATTR))
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

    public CreateInstanceRequestDto toCreatingDto(FinancingSource fs,
                                                  Map<Dictionary, Map<Long, Map.Entry<String, String>>> codesMap) {
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
                                new LinkedAttribute(CASH_PLAN_LIMITS_ATTR, fs.getCashPlanLimitIds()),
                                new StringAttribute(CONCAT_KBK_ATTR, fs.getConcatenatedKBK())
                        ))
                        .build()
        );
    }

    public UpdateInstanceRequestDto toUpdateDto(FinancingSource fs) {
        return new UpdateInstanceRequestDto(
                InstanceDto.builder()
                        .id(fs.getId())
                        .templateId(TEMPLATE_ID)
                        .version(fs.getVersion())
                        .attributes(buildAttributeListToUpdateByCplListAndSp(fs))
                        .build());
    }

    private static List<Attribute<?>> buildAttributeListToUpdateByCplListAndSp(FinancingSource fs) {
        return List.of(
                new LinkedAttribute(CASH_PLAN_LIMITS_ATTR, fs.getCashPlanLimitIds()),
                new LinkedAttribute(SUBSIDY_PROGRAM_ATTR, fs.getSubsidyProgramId())
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
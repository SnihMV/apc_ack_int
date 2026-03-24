package su.petrosoft.apk_ack_integration.mapper;

import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPKR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KCSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KFSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KOSGU;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KVR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KVSR;
import static su.petrosoft.apk_ack_integration.util.DictionaryUtil.ownershipForm;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.FinancingSource;
import su.petrosoft.apk_ack_integration.model.data.BudgetItemData;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.AttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LongAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

@Slf4j
@Component
public class FinancingSourceMapper {

    public FinancingSource toEntity(InstanceDto dto) {
        List<AttributeDto<?>> attributeDtos = dto.attributeDtos();
        return FinancingSource.builder()
            .id(dto.id())
            .version(dto.version())
            .year(extractData(attributeDtos, YEAR_ATTR))
            .kvsr(extractData(attributeDtos, KVSR_ATTR))
            .kfsr(extractData(attributeDtos, KFSR_ATTR))
            .kcsr(extractData(attributeDtos, KCSR_ATTR))
            .kvr(extractData(attributeDtos, KVR_ATTR))
            .kosgu(extractData(attributeDtos, KOSGU_ATTR))
            .dopFk(extractData(attributeDtos, DOPFK_ATTR))
            .dopEk(extractData(attributeDtos, DOPEK_ATTR))
            .dopKr(extractData(attributeDtos, DOPKR_ATTR))
            .purpose(extractData(attributeDtos, PURPOSE_ATTR))
            .ownershipForm(extractData(attributeDtos, OWNERSHIP_FORM_ATTR))
            .subsidyProgramId(extractData(attributeDtos, SUBSIDY_PROGRAM_ATTR))
            .cashPlanLimitIds(new HashSet<>(extractAllData(attributeDtos, CASH_PLAN_LIMITS_ATTR)))
            .concatenatedKBK(extractData(attributeDtos, CONCAT_KBK_ATTR))
            .build();
    }

    public FinancingSource toEntity(BudgetItemData row, Map<Dictionary, Map<String, Long>> codesMap) {
        return FinancingSource.builder()
            .year((long) LocalDate.now().getYear())
            .kvsr(dictionaryIdByCode(codesMap, KVSR, row.kvsr()))
            .kfsr(dictionaryIdByCode(codesMap, KVSR, row.kfsr()))
            .kcsr(dictionaryIdByCode(codesMap, KVSR, row.kcsr()))
            .kvr(dictionaryIdByCode(codesMap, KVSR, row.kvr()))
            .kosgu(dictionaryIdByCode(codesMap, KVSR, row.kosgu()))
            .dopFk(dictionaryIdByCode(codesMap, KVSR, row.dopFk()))
            .dopEk(dictionaryIdByCode(codesMap, KVSR, row.dopEk()))
            .dopKr(dictionaryIdByCode(codesMap, KVSR, row.dopKr()))
            .purpose(dictionaryIdByCode(codesMap, KVSR, row.purpose()))
            .ownershipForm(ownershipForm(row.kosgu()))
            .concatenatedKBK(concatKBK(row))
            .build();
    }

    public CreateInstanceRequestDto toCreatingDto(FinancingSource fs,
        Map<Dictionary, Map<String, Long>> codesMap) {
        long ownershipFormId = ownershipForm(dictionaryCodeById(codesMap, KOSGU, fs.getKosgu()));
        return new CreateInstanceRequestDto(
            InstanceDto.builder()
                .templateId(TEMPLATE_ID)
                .attributeDtos(List.of(
                    new LongAttributeDto(YEAR_ATTR, fs.getYear()),
                    new LinkedAttributeDto(KVSR_ATTR, fs.getKvsr()),
                    new LinkedAttributeDto(KFSR_ATTR, fs.getKfsr()),
                    new LinkedAttributeDto(KCSR_ATTR, fs.getKcsr()),
                    new LinkedAttributeDto(KVR_ATTR, fs.getKvr()),
                    new LinkedAttributeDto(KOSGU_ATTR, fs.getKosgu()),
                    new LinkedAttributeDto(DOPEK_ATTR, fs.getDopEk()),
                    new LinkedAttributeDto(DOPFK_ATTR, fs.getDopFk()),
                    new LinkedAttributeDto(DOPKR_ATTR, fs.getDopKr()),
                    new LinkedAttributeDto(PURPOSE_ATTR, fs.getPurpose()),
                    new LinkedAttributeDto(SUBSIDY_PROGRAM_ATTR, fs.getSubsidyProgramId()),
                    new LinkedAttributeDto(CASH_PLAN_LIMITS_ATTR, fs.getCashPlanLimitIds()),
                    new LinkedAttributeDto(OWNERSHIP_FORM_ATTR, ownershipFormId),
                    new StringAttributeDto(CONCAT_KBK_ATTR, concatKBK(fs, codesMap))
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
                .attributeDtos(buildAttributeListToUpdateByCplListAndSp(fs))
                .build());
    }

    private static List<AttributeDto<?>> buildAttributeListToUpdateByCplListAndSp(FinancingSource fs) {
        return List.of(
            new LinkedAttributeDto(CASH_PLAN_LIMITS_ATTR, fs.getCashPlanLimitIds()),
            new LinkedAttributeDto(SUBSIDY_PROGRAM_ATTR, fs.getSubsidyProgramId())
        );
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

    private String concatKBK(FinancingSource fs, Map<Dictionary, Map<String, Long>> codesMap) {
        return LocalDateTime.now().getYear() +
               "-" +
                dictionaryCodeById(codesMap, KVSR, fs.getKvsr()) +
                dictionaryCodeById(codesMap, KFSR, fs.getKfsr()) +
                dictionaryCodeById(codesMap, KCSR, fs.getKcsr()) +
                dictionaryCodeById(codesMap, KVR, fs.getKvr()) +
                "-" +
                dictionaryCodeById(codesMap, DOPKR, fs.getDopKr());
    }
}
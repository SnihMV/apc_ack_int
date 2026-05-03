package su.petrosoft.apk_ack_integration.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.DictionaryData;
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

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.OWNERSHIP_FORM;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.*;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.*;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.*;

@Slf4j
@Component
public class FinancingSourceMapper {

    public FinancingSource toEntity(InstanceDto dto) {
        List<Attribute<?>> attributes = dto.attributes();
        return FinancingSource.builder()
            .id(dto.id())
            .version(dto.version())
            .year(extractData(attributes, YEAR_ATTR))
            .kvsr(extractData(attributes, KVSR_ATTR))
            .kfsr(extractData(attributes, KFSR_ATTR))
            .kcsr(extractData(attributes, KCSR_ATTR))
            .kvr(extractData(attributes, KVR_ATTR))
            .kosgu(extractData(attributes, KOSGU_ATTR))
            .dopFk(extractData(attributes, DOPFK_ATTR))
            .dopEk(extractData(attributes, DOPEK_ATTR))
            .dopKr(extractData(attributes, DOPKR_ATTR))
            .purpose(extractData(attributes, PURPOSE_ATTR))
            .ownershipForm(extractData(attributes, OWNERSHIP_FORM_ATTR))
            .subsidyProgramId(extractData(attributes, SUBSIDY_PROGRAM_ATTR))
            .cashPlanLimitIds(new HashSet<>(extractAllData(attributes, CASH_PLAN_LIMITS_ATTR)))
            .concatenatedKBK(extractData(attributes, CONCAT_KBK_ATTR))
            .build();
    }

    public FinancingSource toEntity(BudgetItemData row, Map<Dictionary, Map<DictionaryData, Long>> codesMap) {
        Long kosguId = dictionaryIdByCode(codesMap, KVSR, row.kosgu());
        return FinancingSource.builder()
            .year((long) LocalDate.now().getYear())
            .kvsr(dictionaryIdByCode(codesMap, KVSR, row.kvsr()))
            .kfsr(dictionaryIdByCode(codesMap, KVSR, row.kfsr()))
            .kcsr(dictionaryIdByCode(codesMap, KVSR, row.kcsr()))
            .kvr(dictionaryIdByCode(codesMap, KVSR, row.kvr()))
            .kosgu(kosguId)
            .dopFk(dictionaryIdByCode(codesMap, KVSR, row.dopFk()))
            .dopEk(dictionaryIdByCode(codesMap, KVSR, row.dopEk()))
            .dopKr(dictionaryIdByCode(codesMap, KVSR, row.dopKr()))
            .purpose(dictionaryIdByCode(codesMap, KVSR, row.purpose()))
            .ownershipForm(dictionaryIdByCode(codesMap, OWNERSHIP_FORM, dictionaryDataById(codesMap, KOSGU, kosguId).getCode()))
            .concatenatedKBK(concatKBK(row))
            .build();
    }

    public CreateInstanceRequestDto toCreatingDto(FinancingSource fs) {
        return new CreateInstanceRequestDto(
            InstanceDto.builder()
                .templateId(TEMPLATE_ID)
                .attributes(List.of(
                    new LongAttribute(YEAR_ATTR, fs.getYear()),
                    new LinkedAttribute(KVSR_ATTR, fs.getKvsr()),
                    new LinkedAttribute(KFSR_ATTR, fs.getKfsr()),
                    new LinkedAttribute(KCSR_ATTR, fs.getKcsr()),
                    new LinkedAttribute(KVR_ATTR, fs.getKvr()),
                    new LinkedAttribute(KOSGU_ATTR, fs.getKosgu()),
                    new LinkedAttribute(DOPEK_ATTR, fs.getDopEk()),
                    new LinkedAttribute(DOPFK_ATTR, fs.getDopFk()),
                    new LinkedAttribute(DOPKR_ATTR, fs.getDopKr()),
                    new LinkedAttribute(PURPOSE_ATTR, fs.getPurpose()),
                    new LinkedAttribute(CASH_PLAN_LIMITS_ATTR, fs.getCashPlanLimitIds()),
                    new LinkedAttribute(OWNERSHIP_FORM_ATTR, fs.getOwnershipForm()),
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
}
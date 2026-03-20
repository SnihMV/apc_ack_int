package su.petrosoft.apk_ack_integration.mapper;

import static java.util.stream.Collectors.toMap;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.*;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.*;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.dictionaryIdByCode;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.data.CashPlanLimitData;
import su.petrosoft.apk_ack_integration.model.data.xml.CreateCashPlanLimitsXml.Line;
import su.petrosoft.apk_ack_integration.model.data.xml.UpdateCashPlanLimitXml;
import su.petrosoft.apk_ack_integration.model.data.xml.rpl.PlDirectionLine;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.AttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DoubleAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LongAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;
import su.petrosoft.apk_ack_integration.util.AttrInfo;

@Component
@RequiredArgsConstructor
public class CashPlanLimitMapper {

    public CashPlanLimit toEntity(Line line, Map<Dictionary, Map<String, Long>> codesMap) {

        PlDirectionLine pl = getPlDirectionLine(line);

        return CashPlanLimit.builder()
            .year((long) LocalDate.now().getYear())
            .kfsr(dictionaryIdByCode(codesMap, KFSR, line.kfsrCode()))
            .kcsr(dictionaryIdByCode(codesMap, KCSR, line.kcsrCode()))
            .kvr(dictionaryIdByCode(codesMap, KVR, line.kvrCode()))
            .kosgu(dictionaryIdByCode(codesMap, KOSGU, line.kesrCode()))
            .kvsr(dictionaryIdByCode(codesMap, KVSR, line.kadmrCode()))
            .dopFk(dictionaryIdByCode(codesMap, DOPFK, line.kdfCode()))
            .dopEk(dictionaryIdByCode(codesMap, DOPEK, line.kdeCode()))
            .dopKr(dictionaryIdByCode(codesMap, DOPKR, line.kdrCode()))
            .purpose(dictionaryIdByCode(codesMap, PURPOSE, line.purposeFulGrantCode()))
            .totalLimit(getTotalLimit(pl))
            .federalBudget(getTotalFederal(pl))
            .regionalBudget(getTotalRegional(pl))
            .janLimit(line.m01Amt())
            .febLimit(line.m02Amt())
            .marLimit(line.m03Amt())
            .aprLimit(line.m04Amt())
            .mayLimit(line.m05Amt())
            .junLimit(line.m06Amt())
            .julLimit(line.m07Amt())
            .augLimit(line.m08Amt())
            .sepLimit(line.m09Amt())
            .octLimit(line.m10Amt())
            .novLimit(line.m11Amt())
            .decLimit(line.m12Amt())
            .build();
    }

    public CashPlanLimit toEntity(UpdateCashPlanLimitXml updateDto,
        Map<Dictionary, Map<String, Long>> codesMap) {

        PlDirectionLine pl = updateDto.plDirectionLineWrapper().plDirectionLine();

        return CashPlanLimit.builder()
            .year((long) LocalDate.now().getYear())
            .kvsr(dictionaryIdByCode(codesMap, KVSR, updateDto.kadmrCode()))
            .kfsr(dictionaryIdByCode(codesMap, KFSR, updateDto.kfsrCode()))
            .kcsr(dictionaryIdByCode(codesMap, KCSR, updateDto.kcsrCode()))
            .kvr(dictionaryIdByCode(codesMap, KVR, updateDto.kvrCode()))
            .kosgu(dictionaryIdByCode(codesMap, KOSGU, updateDto.kesrCode()))
            .dopEk(dictionaryIdByCode(codesMap, DOPEK, updateDto.kdeCode()))
            .dopKr(dictionaryIdByCode(codesMap, DOPKR, updateDto.kdrCode()))
            .purpose(dictionaryIdByCode(codesMap, PURPOSE, updateDto.purposeFulGrantCode()))
            .dopFk(dictionaryIdByCode(codesMap, DOPFK, updateDto.kdfCode()))
            .totalLimit(getTotalLimit(pl))
            .federalBudget(getTotalFederal(pl))
            .regionalBudget(getTotalRegional(pl))
            .janLimit(updateDto.m01Amt())
            .febLimit(updateDto.m02Amt())
            .marLimit(updateDto.m03Amt())
            .aprLimit(updateDto.m04Amt())
            .mayLimit(updateDto.m05Amt())
            .junLimit(updateDto.m06Amt())
            .julLimit(updateDto.m07Amt())
            .augLimit(updateDto.m08Amt())
            .sepLimit(updateDto.m09Amt())
            .octLimit(updateDto.m10Amt())
            .novLimit(updateDto.m11Amt())
            .decLimit(updateDto.m12Amt())
            .build();
    }

    public CashPlanLimit toEntity(InstanceDto dto) {

        List<AttributeDto<?>> attributeDtos = dto.attributeDtos();

        Map<String, Object> collect = attrInfoList.stream()
                .map(attrInfo -> {
                    Object data = extractData(attributeDtos, attrInfo.id());
                    return data != null ? Map.entry(attrInfo.name(), data) : null;
                })
                .filter(Objects::nonNull)
                .collect(toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));


        return CashPlanLimit.builder()
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
            .recipientName(extractData(attributeDtos, RECIPIENT_NAME))
            .recipientInn(extractData(attributeDtos, RECIPIENT_INN))
            .recipientKpp(extractData(attributeDtos, RECIPIENT_KPP))
            .totalLimit(extractData(attributeDtos, TOTAL_LIMIT_ATTR))
            .federalBudget(extractData(attributeDtos, FEDERAL_BUDGET_ATTR))
            .regionalBudget(extractData(attributeDtos, REGIONAL_BUDGET_ATTR))
            .janLimit(extractData(attributeDtos, JAN_LIMIT_ATTR))
            .febLimit(extractData(attributeDtos, FEB_LIMIT_ATTR))
            .marLimit(extractData(attributeDtos, MAR_LIMIT_ATTR))
            .aprLimit(extractData(attributeDtos, APR_LIMIT_ATTR))
            .mayLimit(extractData(attributeDtos, MAY_LIMIT_ATTR))
            .junLimit(extractData(attributeDtos, JUN_LIMIT_ATTR))
            .julLimit(extractData(attributeDtos, JUL_LIMIT_ATTR))
            .augLimit(extractData(attributeDtos, AUG_LIMIT_ATTR))
            .sepLimit(extractData(attributeDtos, SEP_LIMIT_ATTR))
            .octLimit(extractData(attributeDtos, OCT_LIMIT_ATTR))
            .novLimit(extractData(attributeDtos, NOV_LIMIT_ATTR))
            .decLimit(extractData(attributeDtos, DEC_LIMIT_ATTR))
            .janExpense(extractData(attributeDtos, JAN_EXPENSE_ATTR))
            .febExpense(extractData(attributeDtos, FEB_EXPENSE_ATTR))
            .marExpense(extractData(attributeDtos, MAR_EXPENSE_ATTR))
            .aprExpense(extractData(attributeDtos, APR_EXPENSE_ATTR))
            .mayExpense(extractData(attributeDtos, MAY_EXPENSE_ATTR))
            .junExpense(extractData(attributeDtos, JUN_EXPENSE_ATTR))
            .julExpense(extractData(attributeDtos, JUL_EXPENSE_ATTR))
            .augExpense(extractData(attributeDtos, AUG_EXPENSE_ATTR))
            .sepExpense(extractData(attributeDtos, SEP_EXPENSE_ATTR))
            .octExpense(extractData(attributeDtos, OCT_EXPENSE_ATTR))
            .novExpense(extractData(attributeDtos, NOV_EXPENSE_ATTR))
            .decExpense(extractData(attributeDtos, DEC_EXPENSE_ATTR))
            .janBalance(extractData(attributeDtos, JAN_BALANCE_ATTR))
            .febBalance(extractData(attributeDtos, FEB_BALANCE_ATTR))
            .marBalance(extractData(attributeDtos, MAR_BALANCE_ATTR))
            .aprBalance(extractData(attributeDtos, APR_BALANCE_ATTR))
            .mayBalance(extractData(attributeDtos, MAY_BALANCE_ATTR))
            .junBalance(extractData(attributeDtos, JUN_BALANCE_ATTR))
            .julBalance(extractData(attributeDtos, JUL_BALANCE_ATTR))
            .augBalance(extractData(attributeDtos, AUG_BALANCE_ATTR))
            .sepBalance(extractData(attributeDtos, SEP_BALANCE_ATTR))
            .octBalance(extractData(attributeDtos, OCT_BALANCE_ATTR))
            .novBalance(extractData(attributeDtos, NOV_BALANCE_ATTR))
            .decBalance(extractData(attributeDtos, DEC_BALANCE_ATTR))
            .fstQuarterExpense(extractData(attributeDtos, Q_1_EXPENSE_ATTR))
            .scdQuarterExpense(extractData(attributeDtos, Q_2_EXPENSE_ATTR))
            .trdQuarterExpense(extractData(attributeDtos, Q_3_EXPENSE_ATTR))
            .frtQuarterExpense(extractData(attributeDtos, Q_4_EXPENSE_ATTR))
            .fstQuarterBalance(extractData(attributeDtos, Q_1_BALANCE_ATTR))
            .scdQuarterBalance(extractData(attributeDtos, Q_2_BALANCE_ATTR))
            .trdQuarterBalance(extractData(attributeDtos, Q_3_BALANCE_ATTR))
            .frtQuarterBalance(extractData(attributeDtos, Q_4_BALANCE_ATTR))
            .build();
    }

    public CashPlanLimit toEntity(
        CashPlanLimitData dto,
        Map<Dictionary, Map<String, Long>> codesMap
    ) {

        return CashPlanLimit.builder()
            .year((long) LocalDateTime.now().getYear())
            .kfsr(dictionaryIdByCode(codesMap, KFSR, dto.kfsr()))
            .kvsr(dictionaryIdByCode(codesMap, KVSR, dto.kvsr()))
            .kcsr(dictionaryIdByCode(codesMap, KCSR, dto.kcsr()))
            .kvr(dictionaryIdByCode(codesMap, KVR, dto.kvr()))
            .kosgu(dictionaryIdByCode(codesMap, KOSGU, dto.kosgu()))
            .dopEk(dictionaryIdByCode(codesMap, DOPEK, dto.dopEk()))
            .dopKr(dictionaryIdByCode(codesMap, DOPKR, dto.dopKr()))
            .purpose(dictionaryIdByCode(codesMap, PURPOSE, dto.purpose()))
            .dopFk(dictionaryIdByCode(codesMap, DOPFK, dto.dopFk()))
            .recipientName(dto.recipientName())
            .recipientInn(dto.recipientInn())
            .recipientKpp(dto.recipientKpp())
            .totalLimit(BigDecimal.valueOf(dto.assignTotal()))
            .totalExpense(BigDecimal.ZERO)
            .totalBalance(BigDecimal.valueOf(dto.assignTotal()))
            .federalBudget(BigDecimal.valueOf(dto.assignFederal()))
            .regionalBudget(BigDecimal.valueOf(dto.assignRegional()))
            .janLimit(BigDecimal.valueOf(dto.janLimit()))
            .febLimit(BigDecimal.valueOf(dto.febLimit()))
            .marLimit(BigDecimal.valueOf(dto.marLimit()))
            .aprLimit(BigDecimal.valueOf(dto.aprLimit()))
            .mayLimit(BigDecimal.valueOf(dto.mayLimit()))
            .junLimit(BigDecimal.valueOf(dto.junLimit()))
            .julLimit(BigDecimal.valueOf(dto.julLimit()))
            .augLimit(BigDecimal.valueOf(dto.augLimit()))
            .sepLimit(BigDecimal.valueOf(dto.sepLimit()))
            .octLimit(BigDecimal.valueOf(dto.octLimit()))
            .novLimit(BigDecimal.valueOf(dto.novLimit()))
            .decLimit(BigDecimal.valueOf(dto.decLimit()))
            .janExpense(BigDecimal.ZERO)
            .febExpense(BigDecimal.ZERO)
            .marExpense(BigDecimal.ZERO)
            .aprExpense(BigDecimal.ZERO)
            .mayExpense(BigDecimal.ZERO)
            .junExpense(BigDecimal.ZERO)
            .julExpense(BigDecimal.ZERO)
            .augExpense(BigDecimal.ZERO)
            .sepExpense(BigDecimal.ZERO)
            .octExpense(BigDecimal.ZERO)
            .novExpense(BigDecimal.ZERO)
            .decExpense(BigDecimal.ZERO)
            .janBalance(BigDecimal.valueOf(dto.janLimit()))
            .febBalance(BigDecimal.valueOf(dto.febLimit()))
            .marBalance(BigDecimal.valueOf(dto.marLimit()))
            .aprBalance(BigDecimal.valueOf(dto.aprLimit()))
            .mayBalance(BigDecimal.valueOf(dto.mayLimit()))
            .junBalance(BigDecimal.valueOf(dto.junLimit()))
            .julBalance(BigDecimal.valueOf(dto.julLimit()))
            .augBalance(BigDecimal.valueOf(dto.augLimit()))
            .sepBalance(BigDecimal.valueOf(dto.sepLimit()))
            .octBalance(BigDecimal.valueOf(dto.octLimit()))
            .novBalance(BigDecimal.valueOf(dto.novLimit()))
            .decBalance(BigDecimal.valueOf(dto.decLimit()))
            .fstQuarterExpense(BigDecimal.ZERO)
            .scdQuarterExpense(BigDecimal.ZERO)
            .trdQuarterExpense(BigDecimal.ZERO)
            .frtQuarterExpense(BigDecimal.ZERO)
            .fstQuarterBalance(sumOf(dto.janLimit(), dto.febLimit(), dto.marLimit()))
            .scdQuarterBalance(sumOf(dto.aprLimit(), dto.mayLimit(), dto.junLimit()))
            .trdQuarterBalance(sumOf(dto.julLimit(), dto.augLimit(), dto.sepLimit()))
            .frtQuarterBalance(sumOf(dto.octLimit(), dto.novLimit(), dto.decLimit()))
            .build();
    }

    public CreateInstanceRequestDto toCreateDto(CashPlanLimit cpl) {
        List<AttributeDto<?>> resultList = new ArrayList<>(identifyingAttributeList(cpl));
        resultList.addAll(businessAttributeList(cpl));
        return new CreateInstanceRequestDto(
            InstanceDto.builder()
                .templateId(TEMPLATE_ID)
                .attributeDtos(resultList)
                .build());
    }

    public UpdateInstanceRequestDto toUpdateDto(CashPlanLimit cpl) {
        return new UpdateInstanceRequestDto(
            InstanceDto.builder()
                .id(cpl.getId())
                .templateId(TEMPLATE_ID)
                .version(cpl.getVersion())
                .attributeDtos(businessAttributeList(cpl))
                .build());
    }

    private List<AttributeDto<?>> identifyingAttributeList(CashPlanLimit cpl) {
        return List.of(
                new LongAttributeDto(YEAR_ATTR, cpl.getYear()),
                new LinkedAttributeDto(KVSR_ATTR, cpl.getKvsr()),
                new LinkedAttributeDto(KFSR_ATTR, cpl.getKfsr()),
                new LinkedAttributeDto(KCSR_ATTR, cpl.getKcsr()),
                new LinkedAttributeDto(KVR_ATTR, cpl.getKvr()),
                new LinkedAttributeDto(KOSGU_ATTR, cpl.getKosgu()),
                new LinkedAttributeDto(DOPFK_ATTR, cpl.getDopFk()),
                new LinkedAttributeDto(DOPEK_ATTR, cpl.getDopEk()),
                new LinkedAttributeDto(DOPKR_ATTR, cpl.getDopKr()),
                new LinkedAttributeDto(PURPOSE_ATTR, cpl.getPurpose()),
                new StringAttributeDto(RECIPIENT_NAME, cpl.getRecipientName()),
                new StringAttributeDto(RECIPIENT_INN, cpl.getRecipientInn()),
                new StringAttributeDto(RECIPIENT_KPP, cpl.getRecipientKpp())
        );
    }

    private List<AttributeDto<?>> businessAttributeList(CashPlanLimit cpl) {
        return List.of(
            new DoubleAttributeDto(TOTAL_LIMIT_ATTR, cpl.getTotalLimit()),
            new DoubleAttributeDto(TOTAL_EXPENSE_ATTR, cpl.getTotalExpense()),
            new DoubleAttributeDto(TOTAL_BALANCE_ATTR, cpl.getTotalBalance()),
            new DoubleAttributeDto(FEDERAL_BUDGET_ATTR, cpl. getFederalBudget()),
            new DoubleAttributeDto(REGIONAL_BUDGET_ATTR, cpl.getRegionalBudget()),
            new DoubleAttributeDto(JAN_LIMIT_ATTR, cpl.getJanLimit()),
            new DoubleAttributeDto(FEB_LIMIT_ATTR, cpl.getFebLimit()),
            new DoubleAttributeDto(MAR_LIMIT_ATTR, cpl.getMarLimit()),
            new DoubleAttributeDto(APR_LIMIT_ATTR, cpl.getAprLimit()),
            new DoubleAttributeDto(MAY_LIMIT_ATTR, cpl.getMayLimit()),
            new DoubleAttributeDto(JUN_LIMIT_ATTR, cpl.getJunLimit()),
            new DoubleAttributeDto(JUL_LIMIT_ATTR, cpl.getJulLimit()),
            new DoubleAttributeDto(AUG_LIMIT_ATTR, cpl.getAugLimit()),
            new DoubleAttributeDto(SEP_LIMIT_ATTR, cpl.getSepLimit()),
            new DoubleAttributeDto(OCT_LIMIT_ATTR, cpl.getOctLimit()),
            new DoubleAttributeDto(NOV_LIMIT_ATTR, cpl.getNovLimit()),
            new DoubleAttributeDto(DEC_LIMIT_ATTR, cpl.getDecLimit()),
            new DoubleAttributeDto(JAN_EXPENSE_ATTR, cpl.getJanExpense()),
            new DoubleAttributeDto(FEB_EXPENSE_ATTR, cpl.getFebExpense()),
            new DoubleAttributeDto(MAR_EXPENSE_ATTR, cpl.getMarExpense()),
            new DoubleAttributeDto(APR_EXPENSE_ATTR, cpl.getAprExpense()),
            new DoubleAttributeDto(MAY_EXPENSE_ATTR, cpl.getMayExpense()),
            new DoubleAttributeDto(JUN_EXPENSE_ATTR, cpl.getJunExpense()),
            new DoubleAttributeDto(JUL_EXPENSE_ATTR, cpl.getJulExpense()),
            new DoubleAttributeDto(AUG_EXPENSE_ATTR, cpl.getAugExpense()),
            new DoubleAttributeDto(SEP_EXPENSE_ATTR, cpl.getSepExpense()),
            new DoubleAttributeDto(OCT_EXPENSE_ATTR, cpl.getOctExpense()),
            new DoubleAttributeDto(NOV_EXPENSE_ATTR, cpl.getNovExpense()),
            new DoubleAttributeDto(DEC_EXPENSE_ATTR, cpl.getDecExpense()),
            new DoubleAttributeDto(JAN_BALANCE_ATTR, cpl.getJanBalance()),
            new DoubleAttributeDto(FEB_BALANCE_ATTR, cpl.getFebBalance()),
            new DoubleAttributeDto(MAR_BALANCE_ATTR, cpl.getMarBalance()),
            new DoubleAttributeDto(APR_BALANCE_ATTR, cpl.getAprBalance()),
            new DoubleAttributeDto(MAY_BALANCE_ATTR, cpl.getMayBalance()),
            new DoubleAttributeDto(JUN_BALANCE_ATTR, cpl.getJunBalance()),
            new DoubleAttributeDto(JUL_BALANCE_ATTR, cpl.getJulBalance()),
            new DoubleAttributeDto(AUG_BALANCE_ATTR, cpl.getAugBalance()),
            new DoubleAttributeDto(SEP_BALANCE_ATTR, cpl.getSepBalance()),
            new DoubleAttributeDto(OCT_BALANCE_ATTR, cpl.getOctBalance()),
            new DoubleAttributeDto(NOV_BALANCE_ATTR, cpl.getNovBalance()),
            new DoubleAttributeDto(DEC_BALANCE_ATTR, cpl.getDecBalance()),
            new DoubleAttributeDto(Q_1_EXPENSE_ATTR, cpl.getFstQuarterExpense()),
            new DoubleAttributeDto(Q_2_EXPENSE_ATTR, cpl.getScdQuarterExpense()),
            new DoubleAttributeDto(Q_3_EXPENSE_ATTR, cpl.getTrdQuarterExpense()),
            new DoubleAttributeDto(Q_4_EXPENSE_ATTR, cpl.getFrtQuarterExpense()),
            new DoubleAttributeDto(Q_1_BALANCE_ATTR, cpl.getFstQuarterBalance()),
            new DoubleAttributeDto(Q_2_BALANCE_ATTR, cpl.getScdQuarterBalance()),
            new DoubleAttributeDto(Q_3_BALANCE_ATTR, cpl.getTrdQuarterBalance()),
            new DoubleAttributeDto(Q_4_BALANCE_ATTR, cpl.getFrtQuarterBalance())
        );
    }

    private PlDirectionLine getPlDirectionLine(Line line) {
        if (line.plDirectionLineWrapper() != null &&
            line.plDirectionLineWrapper().plDirectionLine() != null) {
            return line.plDirectionLineWrapper().plDirectionLine();
        }
        return null;
    }

    public CashPlanLimit toEntity(List<AttributeDto<?>> attributeDtos) {
        Map<? extends AttrInfo<?>, Object> collect = attrInfoList.stream()
                .map(attrInfo -> {
                    Object data = extractData(attributeDtos, attrInfo.id());
                    return data != null ? Map.entry(attrInfo, data) : null;
                })
                .filter(Objects::nonNull)
                .collect(toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));

        CashPlanLimit build = CashPlanLimit.builder()
                .id(extractData(attributeDtos, ID_ATTR))
                .version(extractData(attributeDtos, VERSION_ATTR))
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
                .recipientName(extractData(attributeDtos, RECIPIENT_NAME))
                .recipientInn(extractData(attributeDtos, RECIPIENT_INN))
                .recipientKpp(extractData(attributeDtos, RECIPIENT_KPP))
                .build();
        return build;
    }
}

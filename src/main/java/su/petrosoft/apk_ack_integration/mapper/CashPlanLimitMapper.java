package su.petrosoft.apk_ack_integration.mapper;

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

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.DictionaryData;
import su.petrosoft.apk_ack_integration.model.data.CashPlanLimitData;
import su.petrosoft.apk_ack_integration.model.data.xml.CreateCashPlanLimitsXml.Line;
import su.petrosoft.apk_ack_integration.model.data.xml.UpdateCashPlanLimitXml;
import su.petrosoft.apk_ack_integration.model.data.xml.rpl.PlDirectionLine;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DoubleAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LongAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

@Component
@RequiredArgsConstructor
public class CashPlanLimitMapper {

    public CashPlanLimit toEntity(Line line, Map<Dictionary, Map<DictionaryData, Long>> codesMap) {

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

    public CashPlanLimit toEntity(UpdateCashPlanLimitXml updateDto, Map<Dictionary, Map<DictionaryData, Long>> codesMap) {

        PlDirectionLine pl = updateDto.plDirectionLineWrapper().plDirectionLine();

        return CashPlanLimit.builder()
                .year((long) LocalDate.now().getYear())
                .kvsr(dictionaryIdByCode(codesMap, KVSR, updateDto.kvsr()))
                .kfsr(dictionaryIdByCode(codesMap, KFSR, updateDto.kfsr()))
                .kcsr(dictionaryIdByCode(codesMap, KCSR, updateDto.kcsr()))
                .kvr(dictionaryIdByCode(codesMap, KVR, updateDto.kvr()))
                .kosgu(dictionaryIdByCode(codesMap, KOSGU, updateDto.kosgu()))
                .dopEk(dictionaryIdByCode(codesMap, DOPEK, updateDto.dopEk()))
                .dopKr(dictionaryIdByCode(codesMap, DOPKR, updateDto.dopKr()))
                .dopFk(dictionaryIdByCode(codesMap, DOPFK, updateDto.dopKf()))
                .purpose(dictionaryIdByCode(codesMap, PURPOSE, updateDto.purpose()))
                .recipientInn(updateDto.recipientInn())
                .recipientKpp(updateDto.recipientKpp())
                .totalLimit(getTotalLimit(pl))
                .federalBudget(getTotalFederal(pl))
                .regionalBudget(getTotalRegional(pl))
                .janLimit(pl.janLimit())
                .febLimit(pl.febLimit())
                .marLimit(pl.marLimit())
                .aprLimit(pl.aprLimit())
                .mayLimit(pl.mayLimit())
                .junLimit(pl.junLimit())
                .julLimit(pl.julLimit())
                .augLimit(pl.augLimit())
                .sepLimit(pl.sepLimit())
                .octLimit(pl.octLimit())
                .novLimit(pl.novLimit())
                .decLimit(pl.decLimit())
                .build();
    }

    public CashPlanLimit toEntity(InstanceDto dto) {

        List<Attribute<?>> attributes = dto.attributes();

        return CashPlanLimit.builder()
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
                .recipientName(extractData(attributes, RECIPIENT_NAME))
                .recipientInn(extractData(attributes, RECIPIENT_INN))
                .recipientKpp(extractData(attributes, RECIPIENT_KPP))
                .totalLimit(extractData(attributes, TOTAL_LIMIT_ATTR))
                .totalExpense(extractData(attributes, TOTAL_EXPENSE_ATTR))
                .totalBalance(extractData(attributes, TOTAL_BALANCE_ATTR))
                .federalBudget(extractData(attributes, FEDERAL_BUDGET_ATTR))
                .regionalBudget(extractData(attributes, REGIONAL_BUDGET_ATTR))
                .janLimit(extractData(attributes, JAN_LIMIT_ATTR))
                .febLimit(extractData(attributes, FEB_LIMIT_ATTR))
                .marLimit(extractData(attributes, MAR_LIMIT_ATTR))
                .aprLimit(extractData(attributes, APR_LIMIT_ATTR))
                .mayLimit(extractData(attributes, MAY_LIMIT_ATTR))
                .junLimit(extractData(attributes, JUN_LIMIT_ATTR))
                .julLimit(extractData(attributes, JUL_LIMIT_ATTR))
                .augLimit(extractData(attributes, AUG_LIMIT_ATTR))
                .sepLimit(extractData(attributes, SEP_LIMIT_ATTR))
                .octLimit(extractData(attributes, OCT_LIMIT_ATTR))
                .novLimit(extractData(attributes, NOV_LIMIT_ATTR))
                .decLimit(extractData(attributes, DEC_LIMIT_ATTR))
                .janExpense(extractData(attributes, JAN_EXPENSE_ATTR))
                .febExpense(extractData(attributes, FEB_EXPENSE_ATTR))
                .marExpense(extractData(attributes, MAR_EXPENSE_ATTR))
                .aprExpense(extractData(attributes, APR_EXPENSE_ATTR))
                .mayExpense(extractData(attributes, MAY_EXPENSE_ATTR))
                .junExpense(extractData(attributes, JUN_EXPENSE_ATTR))
                .julExpense(extractData(attributes, JUL_EXPENSE_ATTR))
                .augExpense(extractData(attributes, AUG_EXPENSE_ATTR))
                .sepExpense(extractData(attributes, SEP_EXPENSE_ATTR))
                .octExpense(extractData(attributes, OCT_EXPENSE_ATTR))
                .novExpense(extractData(attributes, NOV_EXPENSE_ATTR))
                .decExpense(extractData(attributes, DEC_EXPENSE_ATTR))
                .janBalance(extractData(attributes, JAN_BALANCE_ATTR))
                .febBalance(extractData(attributes, FEB_BALANCE_ATTR))
                .marBalance(extractData(attributes, MAR_BALANCE_ATTR))
                .aprBalance(extractData(attributes, APR_BALANCE_ATTR))
                .mayBalance(extractData(attributes, MAY_BALANCE_ATTR))
                .junBalance(extractData(attributes, JUN_BALANCE_ATTR))
                .julBalance(extractData(attributes, JUL_BALANCE_ATTR))
                .augBalance(extractData(attributes, AUG_BALANCE_ATTR))
                .sepBalance(extractData(attributes, SEP_BALANCE_ATTR))
                .octBalance(extractData(attributes, OCT_BALANCE_ATTR))
                .novBalance(extractData(attributes, NOV_BALANCE_ATTR))
                .decBalance(extractData(attributes, DEC_BALANCE_ATTR))
                .build();
    }

    public CashPlanLimit toEntity(
            CashPlanLimitData dto,
            Map<Dictionary, Map<DictionaryData, Long>> codesMap
    ) {

        return CashPlanLimit.builder()
                .year((long) LocalDateTime.now().getYear())
                .kfsr(codesMap.get(KFSR).get(new DictionaryData(dto.kfsr(), null)))
                .kvsr(codesMap.get(KVSR).get(new DictionaryData(dto.kvsr(), null)))
                .kcsr(codesMap.get(KCSR).get(new DictionaryData(dto.kcsr(), null)))
                .kvr(codesMap.get(KVR).get(new DictionaryData(dto.kvr(), null)))
                .kosgu(codesMap.get(KOSGU).get(new DictionaryData(dto.kosgu(), null)))
                .dopEk(codesMap.get(DOPEK).get(new DictionaryData(dto.dopEk(), null)))
                .dopKr(codesMap.get(DOPKR).get(new DictionaryData(dto.dopKr(), null)))
                .purpose(codesMap.get(PURPOSE).get(new DictionaryData(dto.purpose(), null)))
                .dopFk(codesMap.get(DOPFK).get(new DictionaryData(dto.dopFk(), null)))
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
        return new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(TEMPLATE_ID)
                        .attributes(buildAttributeListToCreate(cpl))
                        .build());
    }

    public UpdateInstanceRequestDto toUpdateDto(CashPlanLimit cpl) {
        return new UpdateInstanceRequestDto(
                InstanceDto.builder()
                        .id(cpl.getId())
                        .templateId(TEMPLATE_ID)
                        .version(cpl.getVersion())
                        .attributes(buildAttributeListToUpdate(cpl))
                        .build());
    }

    private List<Attribute<?>> buildAttributeListToCreate(CashPlanLimit cpl) {
        List<Attribute<?>> equalsAttributes = List.of(
                new LongAttribute(YEAR_ATTR, cpl.getYear()),
                new LinkedAttribute(KVSR_ATTR, cpl.getKvsr()),
                new LinkedAttribute(KFSR_ATTR, cpl.getKfsr()),
                new LinkedAttribute(KCSR_ATTR, cpl.getKcsr()),
                new LinkedAttribute(KVR_ATTR, cpl.getKvr()),
                new LinkedAttribute(KOSGU_ATTR, cpl.getKosgu()),
                new LinkedAttribute(DOPFK_ATTR, cpl.getDopFk()),
                new LinkedAttribute(DOPEK_ATTR, cpl.getDopEk()),
                new LinkedAttribute(DOPKR_ATTR, cpl.getDopKr()),
                new LinkedAttribute(PURPOSE_ATTR, cpl.getPurpose()),
                new StringAttribute(RECIPIENT_NAME, cpl.getRecipientName()),
                new StringAttribute(RECIPIENT_INN, cpl.getRecipientInn()),
                new StringAttribute(RECIPIENT_KPP, cpl.getRecipientKpp())
        );
        List<Attribute<?>> resultList = new ArrayList<>(buildAttributeListToUpdate(cpl));
        resultList.addAll(equalsAttributes);
        return resultList;
    }

    private static List<Attribute<?>> buildAttributeListToUpdate(CashPlanLimit cpl) {
        return List.of(
                new DoubleAttribute(TOTAL_LIMIT_ATTR, cpl.getTotalLimit()),
                new DoubleAttribute(TOTAL_EXPENSE_ATTR, cpl.getTotalExpense()),
                new DoubleAttribute(TOTAL_BALANCE_ATTR, cpl.getTotalBalance()),
                new DoubleAttribute(FEDERAL_BUDGET_ATTR, cpl.getFederalBudget()),
                new DoubleAttribute(REGIONAL_BUDGET_ATTR, cpl.getRegionalBudget()),
                new DoubleAttribute(JAN_LIMIT_ATTR, cpl.getJanLimit()),
                new DoubleAttribute(FEB_LIMIT_ATTR, cpl.getFebLimit()),
                new DoubleAttribute(MAR_LIMIT_ATTR, cpl.getMarLimit()),
                new DoubleAttribute(APR_LIMIT_ATTR, cpl.getAprLimit()),
                new DoubleAttribute(MAY_LIMIT_ATTR, cpl.getMayLimit()),
                new DoubleAttribute(JUN_LIMIT_ATTR, cpl.getJunLimit()),
                new DoubleAttribute(JUL_LIMIT_ATTR, cpl.getJulLimit()),
                new DoubleAttribute(AUG_LIMIT_ATTR, cpl.getAugLimit()),
                new DoubleAttribute(SEP_LIMIT_ATTR, cpl.getSepLimit()),
                new DoubleAttribute(OCT_LIMIT_ATTR, cpl.getOctLimit()),
                new DoubleAttribute(NOV_LIMIT_ATTR, cpl.getNovLimit()),
                new DoubleAttribute(DEC_LIMIT_ATTR, cpl.getDecLimit()),
                new DoubleAttribute(JAN_EXPENSE_ATTR, cpl.getJanExpense()),
                new DoubleAttribute(FEB_EXPENSE_ATTR, cpl.getFebExpense()),
                new DoubleAttribute(MAR_EXPENSE_ATTR, cpl.getMarExpense()),
                new DoubleAttribute(APR_EXPENSE_ATTR, cpl.getAprExpense()),
                new DoubleAttribute(MAY_EXPENSE_ATTR, cpl.getMayExpense()),
                new DoubleAttribute(JUN_EXPENSE_ATTR, cpl.getJunExpense()),
                new DoubleAttribute(JUL_EXPENSE_ATTR, cpl.getJulExpense()),
                new DoubleAttribute(AUG_EXPENSE_ATTR, cpl.getAugExpense()),
                new DoubleAttribute(SEP_EXPENSE_ATTR, cpl.getSepExpense()),
                new DoubleAttribute(OCT_EXPENSE_ATTR, cpl.getOctExpense()),
                new DoubleAttribute(NOV_EXPENSE_ATTR, cpl.getNovExpense()),
                new DoubleAttribute(DEC_EXPENSE_ATTR, cpl.getDecExpense()),
                new DoubleAttribute(JAN_BALANCE_ATTR, cpl.getJanBalance()),
                new DoubleAttribute(FEB_BALANCE_ATTR, cpl.getFebBalance()),
                new DoubleAttribute(MAR_BALANCE_ATTR, cpl.getMarBalance()),
                new DoubleAttribute(APR_BALANCE_ATTR, cpl.getAprBalance()),
                new DoubleAttribute(MAY_BALANCE_ATTR, cpl.getMayBalance()),
                new DoubleAttribute(JUN_BALANCE_ATTR, cpl.getJunBalance()),
                new DoubleAttribute(JUL_BALANCE_ATTR, cpl.getJulBalance()),
                new DoubleAttribute(AUG_BALANCE_ATTR, cpl.getAugBalance()),
                new DoubleAttribute(SEP_BALANCE_ATTR, cpl.getSepBalance()),
                new DoubleAttribute(OCT_BALANCE_ATTR, cpl.getOctBalance()),
                new DoubleAttribute(NOV_BALANCE_ATTR, cpl.getNovBalance()),
                new DoubleAttribute(DEC_BALANCE_ATTR, cpl.getDecBalance()),
                new DoubleAttribute(Q_1_EXPENSE_ATTR, cpl.getFstQuarterExpense()),
                new DoubleAttribute(Q_2_EXPENSE_ATTR, cpl.getScdQuarterExpense()),
                new DoubleAttribute(Q_3_EXPENSE_ATTR, cpl.getTrdQuarterExpense()),
                new DoubleAttribute(Q_4_EXPENSE_ATTR, cpl.getFrtQuarterExpense()),
                new DoubleAttribute(Q_1_BALANCE_ATTR, cpl.getFstQuarterBalance()),
                new DoubleAttribute(Q_2_BALANCE_ATTR, cpl.getScdQuarterBalance()),
                new DoubleAttribute(Q_3_BALANCE_ATTR, cpl.getTrdQuarterBalance()),
                new DoubleAttribute(Q_4_BALANCE_ATTR, cpl.getFrtQuarterBalance())
        );
    }

    private BigDecimal getBigDecimalValue(Object data) {
        if (data == null) {
            return null;
        }
        if (data instanceof BigDecimal bd) {
            return bd;
        }
        if (data instanceof Number num) {
            return BigDecimal.valueOf(num.doubleValue());
        }

        return null;
    }

    private String getAttrShortForm(List<Attribute<?>> attributes, Long attributeId) {
        return attributes.stream()
                .filter(a -> a.id().equals(attributeId))
                .findFirst()
                .map(Attribute::getShortForm)
                .orElse(null);
    }

    private PlDirectionLine getPlDirectionLine(Line line) {
        if (line.plDirectionLineWrapper() != null &&
                line.plDirectionLineWrapper().plDirectionLine() != null) {
            return line.plDirectionLineWrapper().plDirectionLine();
        }
        return null;
    }
}

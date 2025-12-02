package su.petrosoft.apk_ack_integration.mapper;

import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.getCodeId;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.*;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DoubleAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LongAttribute;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.CashPlanLimitExcelRow;
import su.petrosoft.apk_ack_integration.model.excel.RosterKbkExcelRow;
import su.petrosoft.apk_ack_integration.model.xml.CreateCashPlanLimitsXml.Line;
import su.petrosoft.apk_ack_integration.model.xml.PlDirectionLine;
import su.petrosoft.apk_ack_integration.model.xml.UpdateCashPlanLimitXml;

@Component
@RequiredArgsConstructor
public class CashPlanLimitMapper {

    public CashPlanLimit toCpl(Line line) {

        PlDirectionLine pl = getPlDirectionLine(line);

        return CashPlanLimit.builder()
                .year(Long.valueOf(LocalDate.now().getYear()))
                .kfsr(line.kfsrCode())
                .kcsr(line.kcsrCode())
                .kvr(line.kvrCode())
                .kosgu(line.kesrCode())
                .kvsr(line.kadmrCode())
                .dopFk(line.kdfCode())
                .dopEk(line.kdeCode())
                .dopKr(line.kdrCode())
                .purpose(line.purposeFulGrantCode())
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

    public CashPlanLimit toCpl(UpdateCashPlanLimitXml updateDto) {

        PlDirectionLine pl = updateDto.plDirectionLineWrapper().plDirectionLine();

        return CashPlanLimit.builder()
                .year(Long.valueOf(LocalDate.now().getYear()))
                .kvsr(updateDto.kadmrCode())
                .kfsr(updateDto.kfsrCode())
                .kcsr(updateDto.kcsrCode())
                .kvr(updateDto.kvrCode())
                .kosgu(updateDto.kesrCode())
                .dopEk(updateDto.kdeCode())
                .dopKr(updateDto.kdrCode())
                .purpose(updateDto.purposeFulGrantCode())
                .dopFk(updateDto.kdfCode())
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

    public CashPlanLimit toCpl(InstanceDto dto) {

        List<Attribute<?>> attributes = dto.attributes();

        return CashPlanLimit.builder()
                .id(dto.id())
                .version(dto.version())
                .year((Long) getAttrData(attributes, YEAR_ATTR))
                .kvsr(getAttrShortForm(attributes, KVSR_ATTR))
                .kfsr(getAttrShortForm(attributes, KFSR_ATTR))
                .kcsr(getAttrShortForm(attributes, KCSR_ATTR))
                .kvr(getAttrShortForm(attributes, KVR_ATTR))
                .kosgu(getAttrShortForm(attributes, KOSGU_ATTR))
                .dopEk(getAttrShortForm(attributes, DOPEK_ATTR))
                .dopKr(getAttrShortForm(attributes, DOPKR_ATTR))
                .purpose(getAttrShortForm(attributes, PURPOSE_ATTR))
                .dopFk(getAttrShortForm(attributes, DOPFK_ATTR))
                .totalLimit(getBigDecimalValue(getAttrData(attributes, TOTAL_LIMIT_ATTR)))
                .federalBudget(getBigDecimalValue(getAttrData(attributes, FEDERAL_BUDGET_ATTR)))
                .regionalBudget(getBigDecimalValue(getAttrData(attributes, REGIONAL_BUDGET_ATTR)))
                .janLimit(getBigDecimalValue(getAttrData(attributes, JAN_LIMIT_ATTR)))
                .febLimit(getBigDecimalValue(getAttrData(attributes, FEB_LIMIT_ATTR)))
                .marLimit(getBigDecimalValue(getAttrData(attributes, MAR_LIMIT_ATTR)))
                .aprLimit(getBigDecimalValue(getAttrData(attributes, APR_LIMIT_ATTR)))
                .mayLimit(getBigDecimalValue(getAttrData(attributes, MAY_LIMIT_ATTR)))
                .junLimit(getBigDecimalValue(getAttrData(attributes, JUN_LIMIT_ATTR)))
                .julLimit(getBigDecimalValue(getAttrData(attributes, JUL_LIMIT_ATTR)))
                .augLimit(getBigDecimalValue(getAttrData(attributes, AUG_LIMIT_ATTR)))
                .sepLimit(getBigDecimalValue(getAttrData(attributes, SEP_LIMIT_ATTR)))
                .octLimit(getBigDecimalValue(getAttrData(attributes, OCT_LIMIT_ATTR)))
                .novLimit(getBigDecimalValue(getAttrData(attributes, NOV_LIMIT_ATTR)))
                .decLimit(getBigDecimalValue(getAttrData(attributes, DEC_LIMIT_ATTR)))
                .janBalance(getBigDecimalValue(getAttrData(attributes, JAN_BALANCE_ATTR)))
                .febBalance(getBigDecimalValue(getAttrData(attributes, FEB_BALANCE_ATTR)))
                .marBalance(getBigDecimalValue(getAttrData(attributes, MAR_BALANCE_ATTR)))
                .aprBalance(getBigDecimalValue(getAttrData(attributes, APR_BALANCE_ATTR)))
                .mayBalance(getBigDecimalValue(getAttrData(attributes, MAY_BALANCE_ATTR)))
                .junBalance(getBigDecimalValue(getAttrData(attributes, JUN_BALANCE_ATTR)))
                .julBalance(getBigDecimalValue(getAttrData(attributes, JUL_BALANCE_ATTR)))
                .augBalance(getBigDecimalValue(getAttrData(attributes, AUG_BALANCE_ATTR)))
                .sepBalance(getBigDecimalValue(getAttrData(attributes, SEP_BALANCE_ATTR)))
                .octBalance(getBigDecimalValue(getAttrData(attributes, OCT_BALANCE_ATTR)))
                .novBalance(getBigDecimalValue(getAttrData(attributes, NOV_BALANCE_ATTR)))
                .decBalance(getBigDecimalValue(getAttrData(attributes, DEC_BALANCE_ATTR)))
                .fstQuarterBalance(getBigDecimalValue(getAttrData(attributes, QUARTER_1_BAL_ATTR)))
                .scdQuarterBalance(getBigDecimalValue(getAttrData(attributes, QUARTER_2_BAL_ATTR)))
                .trdQuarterBalance(getBigDecimalValue(getAttrData(attributes, QUARTER_3_BAL_ATTR)))
                .frtQuarterBalance(getBigDecimalValue(getAttrData(attributes, QUARTER_4_BAL_ATTR)))
                .build();
    }


    public CashPlanLimit toCpl(RosterKbkExcelRow cplExcel) {

        return CashPlanLimit.builder()
                .year((long) LocalDateTime.now().getYear())
                .kfsr(cplExcel.section() + cplExcel.subsection())
                .kvsr(cplExcel.kvsr())
                .kcsr(cplExcel.kcsr())
                .kvr(cplExcel.kvr())
                .kosgu(cplExcel.kosgu())
                .dopEk(cplExcel.dopEk())
                .dopKr(cplExcel.dopKr())
                .purpose(cplExcel.purpose())
                .dopFk(cplExcel.dopFk())
                .totalLimit(BigDecimal.valueOf(cplExcel.assignTotal()))
                .totalBalance(BigDecimal.valueOf(cplExcel.assignTotal()))
                .federalBudget(BigDecimal.valueOf(cplExcel.assignFederal()))
                .regionalBudget(BigDecimal.valueOf(cplExcel.assignRegional()))
                .totalBalance(BigDecimal.valueOf(cplExcel.assignTotal()))
                .janLimit(BigDecimal.valueOf(cplExcel.m01Amt()))
                .febLimit(BigDecimal.valueOf(cplExcel.m02Amt()))
                .marLimit(BigDecimal.valueOf(cplExcel.m03Amt()))
                .aprLimit(BigDecimal.valueOf(cplExcel.m04Amt()))
                .mayLimit(BigDecimal.valueOf(cplExcel.m05Amt()))
                .junLimit(BigDecimal.valueOf(cplExcel.m06Amt()))
                .julLimit(BigDecimal.valueOf(cplExcel.m07Amt()))
                .augLimit(BigDecimal.valueOf(cplExcel.m08Amt()))
                .sepLimit(BigDecimal.valueOf(cplExcel.m09Amt()))
                .octLimit(BigDecimal.valueOf(cplExcel.m10Amt()))
                .novLimit(BigDecimal.valueOf(cplExcel.m11Amt()))
                .decLimit(BigDecimal.valueOf(cplExcel.m12Amt()))
                .janBalance(BigDecimal.valueOf(cplExcel.m01Amt()))
                .janBalance(BigDecimal.valueOf(cplExcel.m02Amt()))
                .janBalance(BigDecimal.valueOf(cplExcel.m03Amt()))
                .janBalance(BigDecimal.valueOf(cplExcel.m04Amt()))
                .janBalance(BigDecimal.valueOf(cplExcel.m05Amt()))
                .janBalance(BigDecimal.valueOf(cplExcel.m06Amt()))
                .janBalance(BigDecimal.valueOf(cplExcel.m07Amt()))
                .janBalance(BigDecimal.valueOf(cplExcel.m08Amt()))
                .janBalance(BigDecimal.valueOf(cplExcel.m09Amt()))
                .janBalance(BigDecimal.valueOf(cplExcel.m10Amt()))
                .janBalance(BigDecimal.valueOf(cplExcel.m11Amt()))
                .janBalance(BigDecimal.valueOf(cplExcel.m12Amt()))
                .fstQuarterBalance(sumOf(BigDecimal.valueOf(cplExcel.m01Amt()), BigDecimal.valueOf(cplExcel.m02Amt()), BigDecimal.valueOf(cplExcel.m03Amt())))
                .scdQuarterBalance(sumOf(BigDecimal.valueOf(cplExcel.m04Amt()), BigDecimal.valueOf(cplExcel.m05Amt()), BigDecimal.valueOf(cplExcel.m06Amt())))
                .trdQuarterBalance(sumOf(BigDecimal.valueOf(cplExcel.m07Amt()), BigDecimal.valueOf(cplExcel.m08Amt()), BigDecimal.valueOf(cplExcel.m09Amt())))
                .frtQuarterBalance(sumOf(BigDecimal.valueOf(cplExcel.m10Amt()), BigDecimal.valueOf(cplExcel.m11Amt()), BigDecimal.valueOf(cplExcel.m12Amt())))
                .build();
    }

    public CashPlanLimit toCpl(CashPlanLimitExcelRow rowDto) {

        return CashPlanLimit.builder()
                .year((long) LocalDateTime.now().getYear())
                .kfsr(rowDto.getKfsr())
                .kvsr(rowDto.kvsr())
                .kcsr(rowDto.kcsr())
                .kvr(rowDto.kvr())
                .kosgu(rowDto.kosgu())
                .dopEk(rowDto.dopEk())
                .dopKr(rowDto.dopKr())
                .purpose(rowDto.purpose())
                .dopFk(rowDto.dopFk())
                .totalLimit(BigDecimal.valueOf(rowDto.assignTotal()))
                .totalBalance(BigDecimal.valueOf(rowDto.assignTotal()))
                .federalBudget(BigDecimal.valueOf(rowDto.assignFederal()))
                .regionalBudget(BigDecimal.valueOf(rowDto.assignRegional()))
                .totalBalance(BigDecimal.valueOf(rowDto.assignTotal()))
                .janLimit(BigDecimal.valueOf(rowDto.m01Amt()))
                .febLimit(BigDecimal.valueOf(rowDto.m02Amt()))
                .marLimit(BigDecimal.valueOf(rowDto.m03Amt()))
                .aprLimit(BigDecimal.valueOf(rowDto.m04Amt()))
                .mayLimit(BigDecimal.valueOf(rowDto.m05Amt()))
                .junLimit(BigDecimal.valueOf(rowDto.m06Amt()))
                .julLimit(BigDecimal.valueOf(rowDto.m07Amt()))
                .augLimit(BigDecimal.valueOf(rowDto.m08Amt()))
                .sepLimit(BigDecimal.valueOf(rowDto.m09Amt()))
                .octLimit(BigDecimal.valueOf(rowDto.m10Amt()))
                .novLimit(BigDecimal.valueOf(rowDto.m11Amt()))
                .decLimit(BigDecimal.valueOf(rowDto.m12Amt()))
                .janBalance(BigDecimal.valueOf(rowDto.m01Amt()))
                .febBalance(BigDecimal.valueOf(rowDto.m02Amt()))
                .marBalance(BigDecimal.valueOf(rowDto.m03Amt()))
                .aprBalance(BigDecimal.valueOf(rowDto.m04Amt()))
                .mayBalance(BigDecimal.valueOf(rowDto.m05Amt()))
                .junBalance(BigDecimal.valueOf(rowDto.m06Amt()))
                .julBalance(BigDecimal.valueOf(rowDto.m07Amt()))
                .augBalance(BigDecimal.valueOf(rowDto.m08Amt()))
                .sepBalance(BigDecimal.valueOf(rowDto.m09Amt()))
                .octBalance(BigDecimal.valueOf(rowDto.m10Amt()))
                .novBalance(BigDecimal.valueOf(rowDto.m11Amt()))
                .decBalance(BigDecimal.valueOf(rowDto.m12Amt()))
                .fstQuarterBalance(sumOf(BigDecimal.valueOf(rowDto.m01Amt()), BigDecimal.valueOf(rowDto.m02Amt()), BigDecimal.valueOf(rowDto.m03Amt())))
                .scdQuarterBalance(sumOf(BigDecimal.valueOf(rowDto.m04Amt()), BigDecimal.valueOf(rowDto.m05Amt()), BigDecimal.valueOf(rowDto.m06Amt())))
                .trdQuarterBalance(sumOf(BigDecimal.valueOf(rowDto.m07Amt()), BigDecimal.valueOf(rowDto.m08Amt()), BigDecimal.valueOf(rowDto.m09Amt())))
                .frtQuarterBalance(sumOf(BigDecimal.valueOf(rowDto.m10Amt()), BigDecimal.valueOf(rowDto.m11Amt()), BigDecimal.valueOf(rowDto.m12Amt())))
                .build();
    }

    public CreateInstanceRequestDto toCreateDto(CashPlanLimit cpl, Map<CodeType, Map<Long, String>> codesMap) {
        CreateInstanceRequestDto dto = new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(TEMPLATE_ID)
                        .attributes(buildAttributeListToCreate(cpl, codesMap))
                        .build());
        return dto;
    }

    public UpdateInstanceRequestDto toUpdateDto(CashPlanLimit cpl) {
        UpdateInstanceRequestDto dto = new UpdateInstanceRequestDto(
                InstanceDto.builder()
                        .id(cpl.getId())
                        .templateId(TEMPLATE_ID)
                        .version(cpl.getVersion())
                        .attributes(buildAttributeListToUpdate(cpl))
                        .build());
        return dto;
    }

    private List<Attribute<?>> buildAttributeListToCreate(CashPlanLimit cpl, Map<CodeType, Map<Long, String>> codesMap) {
        return List.of(
                new LongAttribute(YEAR_ATTR, cpl.getYear()),
                new DoubleAttribute(TOTAL_LIMIT_ATTR, cpl.getTotalLimit()),
                new DoubleAttribute(TOTAL_BALANCE_ATTR, cpl.getTotalBalance()),
                new DoubleAttribute(FEDERAL_BUDGET_ATTR, cpl.getFederalBudget()),
                new DoubleAttribute(REGIONAL_BUDGET_ATTR, cpl.getRegionalBudget()),
                new LinkedAttribute(KVSR_ATTR, getCodeId(codesMap, KVSR, cpl.getKvsr())),
                new LinkedAttribute(KFSR_ATTR, getCodeId(codesMap, KFSR, cpl.getKfsr())),
                new LinkedAttribute(KCSR_ATTR, getCodeId(codesMap, KCSR, cpl.getKcsr())),
                new LinkedAttribute(KVR_ATTR, getCodeId(codesMap, KVR, cpl.getKvr())),
                new LinkedAttribute(KOSGU_ATTR, getCodeId(codesMap, KOSGU, cpl.getKosgu())),
                new LinkedAttribute(DOPFK_ATTR, getCodeId(codesMap, DOPFK, cpl.getDopFk())),
                new LinkedAttribute(DOPEK_ATTR, getCodeId(codesMap, DOPEK, cpl.getDopEk())),
                new LinkedAttribute(DOPKR_ATTR, getCodeId(codesMap, DOPKR, cpl.getDopKr())),
                new LinkedAttribute(PURPOSE_ATTR, getCodeId(codesMap, PURPOSE, cpl.getPurpose())),
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
                new DoubleAttribute(QUARTER_1_BAL_ATTR, cpl.getFstQuarterBalance()),
                new DoubleAttribute(QUARTER_2_BAL_ATTR, cpl.getScdQuarterBalance()),
                new DoubleAttribute(QUARTER_3_BAL_ATTR, cpl.getTrdQuarterBalance()),
                new DoubleAttribute(QUARTER_4_BAL_ATTR, cpl.getFrtQuarterBalance())
        );
    }

    private static List<Attribute<?>> buildAttributeListToUpdate(CashPlanLimit cpl) {
        return List.of(
                new DoubleAttribute(TOTAL_LIMIT_ATTR, cpl.getTotalLimit()),
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
                new DoubleAttribute(QUARTER_1_BAL_ATTR, cpl.getFstQuarterBalance()),
                new DoubleAttribute(QUARTER_2_BAL_ATTR, cpl.getScdQuarterBalance()),
                new DoubleAttribute(QUARTER_3_BAL_ATTR, cpl.getTrdQuarterBalance()),
                new DoubleAttribute(QUARTER_4_BAL_ATTR, cpl.getFrtQuarterBalance())
        );
    }

    private BigDecimal getBigDecimalValue(Object data) {
        if (data == null) return null;
        if (data instanceof BigDecimal bd) return bd;
        if (data instanceof Number num) return BigDecimal.valueOf(num.doubleValue());

        return null;
    }

    private Object getAttrData(List<Attribute<?>> attributes, long attributeId) {
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

    private PlDirectionLine getPlDirectionLine(Line line) {
        if (line.plDirectionLineWrapper() != null &&
                line.plDirectionLineWrapper().plDirectionLine() != null) {
            return line.plDirectionLineWrapper().plDirectionLine();
        }
        return null;
    }
}

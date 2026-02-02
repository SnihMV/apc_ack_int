package su.petrosoft.apk_ack_integration.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
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
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.PURPOSE;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.APR_BALANCE_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.APR_LIMIT_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.AUG_BALANCE_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.AUG_LIMIT_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.DEC_BALANCE_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.DEC_LIMIT_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.DOPEK_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.DOPFK_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.DOPKR_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.FEB_BALANCE_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.FEB_LIMIT_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.FEDERAL_BUDGET_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.JAN_BALANCE_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.JAN_LIMIT_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.JUL_BALANCE_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.JUL_LIMIT_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.JUN_BALANCE_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.JUN_LIMIT_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.KCSR_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.KFSR_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.KOSGU_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.KVR_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.KVSR_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.MAR_BALANCE_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.MAR_LIMIT_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.MAY_BALANCE_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.MAY_LIMIT_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.NOV_BALANCE_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.NOV_LIMIT_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.OCT_BALANCE_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.OCT_LIMIT_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.PURPOSE_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.QUARTER_1_BAL_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.QUARTER_2_BAL_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.QUARTER_3_BAL_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.QUARTER_4_BAL_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.REGIONAL_BUDGET_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.SEP_BALANCE_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.SEP_LIMIT_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.TEMPLATE_ID;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.TOTAL_BALANCE_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.TOTAL_LIMIT_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.YEAR_ATTR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.getTotalFederal;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.getTotalLimit;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.getTotalRegional;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.sumOf;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.dictionaryIdByCode;

@Component
@RequiredArgsConstructor
public class CashPlanLimitMapper {

    public CashPlanLimit toEntity(Line line) {

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

    public CashPlanLimit toEntity(UpdateCashPlanLimitXml updateDto) {

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

    public CashPlanLimit toEntity(InstanceDto dto) {

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

    public CashPlanLimit toEntity(CashPlanLimitData valueObject) {

        return CashPlanLimit.builder()
                .year((long) LocalDateTime.now().getYear())
                .kfsr(valueObject.kfsr())
                .kvsr(valueObject.kvsr())
                .kcsr(valueObject.kcsr())
                .kvr(valueObject.kvr())
                .kosgu(valueObject.kosgu())
                .dopEk(valueObject.dopEk())
                .dopKr(valueObject.dopKr())
                .purpose(valueObject.purpose())
                .dopFk(valueObject.dopFk())
                .totalLimit(BigDecimal.valueOf(valueObject.assignTotal()))
                .totalBalance(BigDecimal.valueOf(valueObject.assignTotal()))
                .federalBudget(BigDecimal.valueOf(valueObject.assignFederal()))
                .regionalBudget(BigDecimal.valueOf(valueObject.assignRegional()))
                .totalBalance(BigDecimal.valueOf(valueObject.assignTotal()))
                .janLimit(BigDecimal.valueOf(valueObject.janLimit()))
                .febLimit(BigDecimal.valueOf(valueObject.febLimit()))
                .marLimit(BigDecimal.valueOf(valueObject.marLimit()))
                .aprLimit(BigDecimal.valueOf(valueObject.aprLimit()))
                .mayLimit(BigDecimal.valueOf(valueObject.mayLimit()))
                .junLimit(BigDecimal.valueOf(valueObject.junLimit()))
                .julLimit(BigDecimal.valueOf(valueObject.julLimit()))
                .augLimit(BigDecimal.valueOf(valueObject.augLimit()))
                .sepLimit(BigDecimal.valueOf(valueObject.sepLimit()))
                .octLimit(BigDecimal.valueOf(valueObject.octLimit()))
                .novLimit(BigDecimal.valueOf(valueObject.novLimit()))
                .decLimit(BigDecimal.valueOf(valueObject.decLimit()))
                .janBalance(BigDecimal.valueOf(valueObject.janLimit()))
                .febBalance(BigDecimal.valueOf(valueObject.febLimit()))
                .marBalance(BigDecimal.valueOf(valueObject.marLimit()))
                .aprBalance(BigDecimal.valueOf(valueObject.aprLimit()))
                .mayBalance(BigDecimal.valueOf(valueObject.mayLimit()))
                .junBalance(BigDecimal.valueOf(valueObject.junLimit()))
                .julBalance(BigDecimal.valueOf(valueObject.julLimit()))
                .augBalance(BigDecimal.valueOf(valueObject.augLimit()))
                .sepBalance(BigDecimal.valueOf(valueObject.sepLimit()))
                .octBalance(BigDecimal.valueOf(valueObject.octLimit()))
                .novBalance(BigDecimal.valueOf(valueObject.novLimit()))
                .decBalance(BigDecimal.valueOf(valueObject.decLimit()))
                .fstQuarterBalance(sumOf(BigDecimal.valueOf(valueObject.janLimit()), BigDecimal.valueOf(valueObject.febLimit()), BigDecimal.valueOf(valueObject.marLimit())))
                .scdQuarterBalance(sumOf(BigDecimal.valueOf(valueObject.aprLimit()), BigDecimal.valueOf(valueObject.mayLimit()), BigDecimal.valueOf(valueObject.junLimit())))
                .trdQuarterBalance(sumOf(BigDecimal.valueOf(valueObject.julLimit()), BigDecimal.valueOf(valueObject.augLimit()), BigDecimal.valueOf(valueObject.sepLimit())))
                .frtQuarterBalance(sumOf(BigDecimal.valueOf(valueObject.octLimit()), BigDecimal.valueOf(valueObject.novLimit()), BigDecimal.valueOf(valueObject.decLimit())))
                .build();
    }

    public CreateInstanceRequestDto toCreateDto(CashPlanLimit cpl, Map<Dictionary, Map<String, Long>> codesMap) {
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

    private List<Attribute<?>> buildAttributeListToCreate(CashPlanLimit cpl, Map<Dictionary, Map<String, Long>> codesMap) {
        return List.of(
                new LongAttribute(YEAR_ATTR, cpl.getYear()),
                new DoubleAttribute(TOTAL_LIMIT_ATTR, cpl.getTotalLimit()),
                new DoubleAttribute(TOTAL_BALANCE_ATTR, cpl.getTotalBalance()),
                new DoubleAttribute(FEDERAL_BUDGET_ATTR, cpl.getFederalBudget()),
                new DoubleAttribute(REGIONAL_BUDGET_ATTR, cpl.getRegionalBudget()),
                new LinkedAttribute(KVSR_ATTR, dictionaryIdByCode(codesMap, KVSR, cpl.getKvsr())),
                new LinkedAttribute(KFSR_ATTR, dictionaryIdByCode(codesMap, KFSR, cpl.getKfsr())),
                new LinkedAttribute(KCSR_ATTR, dictionaryIdByCode(codesMap, KCSR, cpl.getKcsr())),
                new LinkedAttribute(KVR_ATTR, dictionaryIdByCode(codesMap, KVR, cpl.getKvr())),
                new LinkedAttribute(KOSGU_ATTR, dictionaryIdByCode(codesMap, KOSGU, cpl.getKosgu())),
                new LinkedAttribute(DOPFK_ATTR, dictionaryIdByCode(codesMap, DOPFK, cpl.getDopFk())),
                new LinkedAttribute(DOPEK_ATTR, dictionaryIdByCode(codesMap, DOPEK, cpl.getDopEk())),
                new LinkedAttribute(DOPKR_ATTR, dictionaryIdByCode(codesMap, DOPKR, cpl.getDopKr())),
                new LinkedAttribute(PURPOSE_ATTR, dictionaryIdByCode(codesMap, PURPOSE, cpl.getPurpose())),
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

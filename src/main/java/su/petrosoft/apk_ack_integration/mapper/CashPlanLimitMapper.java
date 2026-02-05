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
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttribute;
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
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.RECIPIENT_INN;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.RECIPIENT_KPP;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.RECIPIENT_NAME;
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
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.dictionaryCodeById;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.dictionaryIdByCode;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractData;

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

    public CashPlanLimit toEntity(InstanceDto dto, Map<Dictionary, Map<Long, Map.Entry<String, String>>> codesMap) {

        List<Attribute<?>> attributes = dto.attributes();

        return CashPlanLimit.builder()
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
                .recipientName(extractData(attributes, RECIPIENT_NAME))
                .recipientInn(extractData(attributes, RECIPIENT_INN))
                .recipientKpp(extractData(attributes, RECIPIENT_KPP))
                .totalLimit(getBigDecimalValue(extractData(attributes, TOTAL_LIMIT_ATTR)))
                .federalBudget(getBigDecimalValue(extractData(attributes, FEDERAL_BUDGET_ATTR)))
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
                .fstQuarterBalance(extractData(attributes, QUARTER_1_BAL_ATTR))
                .scdQuarterBalance(extractData(attributes, QUARTER_2_BAL_ATTR))
                .trdQuarterBalance(extractData(attributes, QUARTER_3_BAL_ATTR))
                .frtQuarterBalance(extractData(attributes, QUARTER_4_BAL_ATTR))
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
                .recipientName(valueObject.recipientName())
                .recipientInn(valueObject.recipientInn())
                .recipientKpp(valueObject.recipientKpp())
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
                .fstQuarterBalance(sumOf(BigDecimal.valueOf(valueObject.janLimit()),
                        BigDecimal.valueOf(valueObject.febLimit()),
                        BigDecimal.valueOf(valueObject.marLimit())))
                .scdQuarterBalance(sumOf(BigDecimal.valueOf(valueObject.aprLimit()),
                        BigDecimal.valueOf(valueObject.mayLimit()),
                        BigDecimal.valueOf(valueObject.junLimit())))
                .trdQuarterBalance(sumOf(BigDecimal.valueOf(valueObject.julLimit()),
                        BigDecimal.valueOf(valueObject.augLimit()),
                        BigDecimal.valueOf(valueObject.sepLimit())))
                .frtQuarterBalance(sumOf(BigDecimal.valueOf(valueObject.octLimit()),
                        BigDecimal.valueOf(valueObject.novLimit()),
                        BigDecimal.valueOf(valueObject.decLimit())))
                .build();
    }

    public CreateInstanceRequestDto toCreateDto(CashPlanLimit cpl,
                                                Map<Dictionary, Map<Long, Map.Entry<String, String>>> codesMap) {
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

    private List<Attribute<?>> buildAttributeListToCreate(CashPlanLimit cpl,
                                                          Map<Dictionary, Map<Long, Map.Entry<String, String>>> codesMap) {
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
                new StringAttribute(RECIPIENT_NAME, cpl.getRecipientName()),
                new StringAttribute(RECIPIENT_INN, cpl.getRecipientInn()),
                new StringAttribute(RECIPIENT_KPP, cpl.getRecipientKpp()),
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
//
//    private Object getAttrData(List<Attribute<?>> attributes, long attributeId) {
//        return attributes.stream()
//                .filter(a -> a.id().equals(attributeId))
//                .findFirst()
//                .map(Attribute::getData)
//                .orElse(null);
//    }

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

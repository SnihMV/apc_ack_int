package su.petrosoft.apk_ack_integration.mapper;

import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KADMR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KCSR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KDE;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KDF;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KDR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KESR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KFSR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KVR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.PURPOSEFULGRANT;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.CASH_PLAN_LIMIT_TEMPLATE_ID;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.getTotalFederal;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.getTotalLimit;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.getTotalRegional;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.sumOf;
import static su.petrosoft.apk_ack_integration.util.DictionaryUtil.getCodeId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.dto.request.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.UpsertInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.DoubleAttribute;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.LongAttribute;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.CashPlanLimitExcelRow;
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
                .limitTotalAmt(getTotalLimit(pl))
                .limitFederalAmt(getTotalFederal(pl))
                .limitRegionalAmt(getTotalRegional(pl))
                .m01Amt(line.m01Amt())
                .m02Amt(line.m02Amt())
                .m03Amt(line.m03Amt())
                .m04Amt(line.m04Amt())
                .m05Amt(line.m05Amt())
                .m06Amt(line.m06Amt())
                .m07Amt(line.m07Amt())
                .m08Amt(line.m08Amt())
                .m09Amt(line.m09Amt())
                .m10Amt(line.m10Amt())
                .m11Amt(line.m11Amt())
                .m12Amt(line.m12Amt())
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
                .limitTotalAmt(getTotalLimit(pl))
                .limitFederalAmt(getTotalFederal(pl))
                .limitRegionalAmt(getTotalRegional(pl))
                .m01Amt(updateDto.m01Amt())
                .m02Amt(updateDto.m02Amt())
                .m03Amt(updateDto.m03Amt())
                .m04Amt(updateDto.m04Amt())
                .m05Amt(updateDto.m05Amt())
                .m06Amt(updateDto.m06Amt())
                .m07Amt(updateDto.m07Amt())
                .m08Amt(updateDto.m08Amt())
                .m09Amt(updateDto.m09Amt())
                .m10Amt(updateDto.m10Amt())
                .m11Amt(updateDto.m11Amt())
                .m12Amt(updateDto.m12Amt())
                .build();
    }

    public CashPlanLimit toCpl(InstanceDto dto) {

        List<Attribute> attributes = dto.attributes();

        return CashPlanLimit.builder()
                .id(dto.id())
                .version(dto.version())
                .year((Long) getAttrData(attributes, 3303))
                .kvsr(getAttrShortForm(attributes, KADMR.getAttributeId()))
                .kfsr(getAttrShortForm(attributes, KFSR.getAttributeId()))
                .kcsr(getAttrShortForm(attributes, KCSR.getAttributeId()))
                .kvr(getAttrShortForm(attributes, KVR.getAttributeId()))
                .kosgu(getAttrShortForm(attributes, KESR.getAttributeId()))
                .dopEk(getAttrShortForm(attributes, KDE.getAttributeId()))
                .dopKr(getAttrShortForm(attributes, KDR.getAttributeId()))
                .purpose(getAttrShortForm(attributes, PURPOSEFULGRANT.getAttributeId()))
                .dopFk(getAttrShortForm(attributes, KDF.getAttributeId()))
                .limitTotalAmt(getBigDecimalValue(getAttrData(attributes, 1609)))
                .limitFederalAmt(getBigDecimalValue(getAttrData(attributes, 1828)))
                .limitRegionalAmt(getBigDecimalValue(getAttrData(attributes, 1829)))
                .m01Amt(getBigDecimalValue(getAttrData(attributes, 1612)))
                .m02Amt(getBigDecimalValue(getAttrData(attributes, 1613)))
                .m03Amt(getBigDecimalValue(getAttrData(attributes, 1614)))
                .m04Amt(getBigDecimalValue(getAttrData(attributes, 1617)))
                .m05Amt(getBigDecimalValue(getAttrData(attributes, 1618)))
                .m06Amt(getBigDecimalValue(getAttrData(attributes, 1619)))
                .m07Amt(getBigDecimalValue(getAttrData(attributes, 1622)))
                .m08Amt(getBigDecimalValue(getAttrData(attributes, 1623)))
                .m09Amt(getBigDecimalValue(getAttrData(attributes, 1624)))
                .m10Amt(getBigDecimalValue(getAttrData(attributes, 1627)))
                .m11Amt(getBigDecimalValue(getAttrData(attributes, 1628)))
                .m12Amt(getBigDecimalValue(getAttrData(attributes, 1629)))
                .r01Amt(getBigDecimalValue(getAttrData(attributes, 3276)))
                .r02Amt(getBigDecimalValue(getAttrData(attributes, 3278)))
                .r03Amt(getBigDecimalValue(getAttrData(attributes, 3280)))
                .r04Amt(getBigDecimalValue(getAttrData(attributes, 3282)))
                .r05Amt(getBigDecimalValue(getAttrData(attributes, 3284)))
                .r06Amt(getBigDecimalValue(getAttrData(attributes, 3286)))
                .r07Amt(getBigDecimalValue(getAttrData(attributes, 3288)))
                .r08Amt(getBigDecimalValue(getAttrData(attributes, 3290)))
                .r09Amt(getBigDecimalValue(getAttrData(attributes, 3292)))
                .r10Amt(getBigDecimalValue(getAttrData(attributes, 3294)))
                .r11Amt(getBigDecimalValue(getAttrData(attributes, 3296)))
                .r12Amt(getBigDecimalValue(getAttrData(attributes, 3298)))
                .rKv1Amt(getBigDecimalValue(getAttrData(attributes, 1616)))
                .rKv2Amt(getBigDecimalValue(getAttrData(attributes, 1621)))
                .rKv3Amt(getBigDecimalValue(getAttrData(attributes, 1626)))
                .rKv4Amt(getBigDecimalValue(getAttrData(attributes, 1631)))
                .build();
    }


    public CashPlanLimit toCpl(CashPlanLimitExcelRow cplExcel) {

        return CashPlanLimit.builder()
                .year((long) LocalDateTime.now().getYear())
                .kfsr(cplExcel.section() + cplExcel.subsection())
                .kvsr(cplExcel.kvsr())
                .kcsr(cplExcel.kcsr())
                .kvr(cplExcel.kvr())
                .kosgu(cplExcel.kosgu())
                .dopEk(cplExcel.dopEk())
                .dopKr(cplExcel.dopKr())
                .purpose(cplExcel.purposeCode())
                .dopFk(cplExcel.dopFk())
                .limitTotalAmt(BigDecimal.valueOf(cplExcel.assignTotal()))
                .remainTotal(BigDecimal.valueOf(cplExcel.assignTotal()))
                .limitFederalAmt(BigDecimal.valueOf(cplExcel.assignFederal()))
                .limitRegionalAmt(BigDecimal.valueOf(cplExcel.assignRegional()))
                .remainTotal(BigDecimal.valueOf(cplExcel.assignTotal()))
                .m01Amt(BigDecimal.valueOf(cplExcel.m01Amt()))
                .m02Amt(BigDecimal.valueOf(cplExcel.m02Amt()))
                .m03Amt(BigDecimal.valueOf(cplExcel.m03Amt()))
                .m04Amt(BigDecimal.valueOf(cplExcel.m04Amt()))
                .m05Amt(BigDecimal.valueOf(cplExcel.m05Amt()))
                .m06Amt(BigDecimal.valueOf(cplExcel.m06Amt()))
                .m07Amt(BigDecimal.valueOf(cplExcel.m07Amt()))
                .m08Amt(BigDecimal.valueOf(cplExcel.m08Amt()))
                .m09Amt(BigDecimal.valueOf(cplExcel.m09Amt()))
                .m10Amt(BigDecimal.valueOf(cplExcel.m10Amt()))
                .m11Amt(BigDecimal.valueOf(cplExcel.m11Amt()))
                .m12Amt(BigDecimal.valueOf(cplExcel.m12Amt()))
                .r01Amt(BigDecimal.valueOf(cplExcel.m01Amt()))
                .r02Amt(BigDecimal.valueOf(cplExcel.m02Amt()))
                .r03Amt(BigDecimal.valueOf(cplExcel.m03Amt()))
                .r04Amt(BigDecimal.valueOf(cplExcel.m04Amt()))
                .r05Amt(BigDecimal.valueOf(cplExcel.m05Amt()))
                .r06Amt(BigDecimal.valueOf(cplExcel.m06Amt()))
                .r07Amt(BigDecimal.valueOf(cplExcel.m07Amt()))
                .r08Amt(BigDecimal.valueOf(cplExcel.m08Amt()))
                .r09Amt(BigDecimal.valueOf(cplExcel.m09Amt()))
                .r10Amt(BigDecimal.valueOf(cplExcel.m10Amt()))
                .r11Amt(BigDecimal.valueOf(cplExcel.m11Amt()))
                .r12Amt(BigDecimal.valueOf(cplExcel.m12Amt()))
                .rKv1Amt(sumOf(BigDecimal.valueOf(cplExcel.m01Amt()), BigDecimal.valueOf(cplExcel.m02Amt()), BigDecimal.valueOf(cplExcel.m03Amt())))
                .rKv2Amt(sumOf(BigDecimal.valueOf(cplExcel.m04Amt()), BigDecimal.valueOf(cplExcel.m05Amt()), BigDecimal.valueOf(cplExcel.m06Amt())))
                .rKv3Amt(sumOf(BigDecimal.valueOf(cplExcel.m07Amt()), BigDecimal.valueOf(cplExcel.m08Amt()), BigDecimal.valueOf(cplExcel.m09Amt())))
                .rKv4Amt(sumOf(BigDecimal.valueOf(cplExcel.m10Amt()), BigDecimal.valueOf(cplExcel.m11Amt()), BigDecimal.valueOf(cplExcel.m12Amt())))
                .build();
    }

    public CreateInstanceRequestDto toCreateDto(CashPlanLimit cpl, Map<CodeType, Map<Long, String>> codesMap) {
        CreateInstanceRequestDto dto = new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(CASH_PLAN_LIMIT_TEMPLATE_ID)
                        .attributes(buildAttributeList(cpl, codesMap))
                        .build());
        return dto;
    }

    public UpsertInstanceRequestDto toUpdateDto(CashPlanLimit cpl, Map<CodeType, Map<Long, String>> codesMap) {
        UpsertInstanceRequestDto dto = new UpsertInstanceRequestDto(
                InstanceDto.builder()
                        .id(cpl.getId())
                        .templateId(CASH_PLAN_LIMIT_TEMPLATE_ID)
                        .version(cpl.getVersion())
                        .attributes(buildAttributeList(cpl, codesMap))
                        .build());
        return dto;
    }

    private static List<Attribute> buildAttributeList(CashPlanLimit cpl, Map<CodeType, Map<Long, String>> codesMap) {
        return List.of(
                new LongAttribute(3303, cpl.getYear()),
                new DoubleAttribute(1609, cpl.getLimitTotalAmt()),
                new DoubleAttribute(1611, cpl.getRemainTotal()),
                new DoubleAttribute(1828, cpl.getLimitFederalAmt()),
                new DoubleAttribute(1829, cpl.getLimitRegionalAmt()),
                new LinkedAttribute(1733, getCodeId(codesMap, KADMR, cpl.getKvsr())),
                new LinkedAttribute(1734, getCodeId(codesMap, KFSR, cpl.getKfsr())),
                new LinkedAttribute(1735, getCodeId(codesMap, KCSR, cpl.getKcsr())),
                new LinkedAttribute(1736, getCodeId(codesMap, KVR, cpl.getKvr())),
                new LinkedAttribute(1737, getCodeId(codesMap, KESR, cpl.getKosgu())),
                new LinkedAttribute(1739, getCodeId(codesMap, KDE, cpl.getDopEk())),
                new LinkedAttribute(1740, getCodeId(codesMap, KDR, cpl.getDopKr())),
                new LinkedAttribute(1751, getCodeId(codesMap, PURPOSEFULGRANT, cpl.getPurpose())),
                new LinkedAttribute(3448, getCodeId(codesMap, KDF, cpl.getDopFk())),
                new DoubleAttribute(1612, cpl.getM01Amt()),
                new DoubleAttribute(1613, cpl.getM02Amt()),
                new DoubleAttribute(1614, cpl.getM03Amt()),
                new DoubleAttribute(1617, cpl.getM04Amt()),
                new DoubleAttribute(1618, cpl.getM05Amt()),
                new DoubleAttribute(1619, cpl.getM06Amt()),
                new DoubleAttribute(1622, cpl.getM07Amt()),
                new DoubleAttribute(1623, cpl.getM08Amt()),
                new DoubleAttribute(1624, cpl.getM09Amt()),
                new DoubleAttribute(1627, cpl.getM10Amt()),
                new DoubleAttribute(1628, cpl.getM11Amt()),
                new DoubleAttribute(1629, cpl.getM12Amt()),
                new DoubleAttribute(3276, cpl.getR01Amt()),
                new DoubleAttribute(3278, cpl.getR02Amt()),
                new DoubleAttribute(3280, cpl.getR03Amt()),
                new DoubleAttribute(3282, cpl.getR04Amt()),
                new DoubleAttribute(3284, cpl.getR05Amt()),
                new DoubleAttribute(3286, cpl.getR06Amt()),
                new DoubleAttribute(3288, cpl.getR07Amt()),
                new DoubleAttribute(3290, cpl.getR08Amt()),
                new DoubleAttribute(3292, cpl.getR09Amt()),
                new DoubleAttribute(3294, cpl.getR10Amt()),
                new DoubleAttribute(3296, cpl.getR11Amt()),
                new DoubleAttribute(3298, cpl.getR12Amt()),
                new DoubleAttribute(1616, cpl.getRKv1Amt()),
                new DoubleAttribute(1621, cpl.getRKv2Amt()),
                new DoubleAttribute(1626, cpl.getRKv3Amt()),
                new DoubleAttribute(1631, cpl.getRKv4Amt())
        );
    }

    private BigDecimal getBigDecimalValue(Object data) {
        if (data == null) return null;
        if (data instanceof BigDecimal bd) return bd;
        if (data instanceof Number num) return BigDecimal.valueOf(num.doubleValue());

        return null;
    }

    private Object getAttrData(List<Attribute> attributes, long attributeId) {
        return attributes.stream()
                .filter(a -> a.id().equals(attributeId))
                .findFirst()
                .map(Attribute::getData)
                .orElse(null);
    }

    private String getAttrShortForm(List<Attribute> attributes, Long attributeId) {
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

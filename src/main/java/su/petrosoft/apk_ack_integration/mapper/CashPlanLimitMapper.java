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
import su.petrosoft.apk_ack_integration.model.dto.response.GetAttributesListResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.CreateCashPlanLimitExcel;
import su.petrosoft.apk_ack_integration.model.xml.CreateCashPlanLimitsXml.Line;
import su.petrosoft.apk_ack_integration.model.xml.PlDirectionLine;
import su.petrosoft.apk_ack_integration.model.xml.UpsertCashPlanLimitXml;

@Component
@RequiredArgsConstructor
public class CashPlanLimitMapper {

    public CashPlanLimit toCpl(Line line) {

        PlDirectionLine pl = getPlDirectionLine(line);

        return CashPlanLimit.builder()
                .year(Long.valueOf(LocalDate.now().getYear()))
                .kfsrCode(Long.valueOf(line.kfsrCode()))
                .kcsrCode(Long.valueOf(line.kcsrCode()))
                .kvrCode(Long.valueOf(line.kvrCode()))
                .kesrCode(Long.valueOf(line.kesrCode()))
                .kadmrCode(Long.valueOf(line.kadmrCode()))
                .kdfCode(Long.valueOf(line.kdfCode()))
                .kdeCode(Long.valueOf(line.kdeCode()))
                .kdrCode(Long.valueOf(line.kdrCode()))
                .purposeCode(Long.valueOf(line.purposeFulGrantCode()))
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

    public CashPlanLimit toCpl(UpsertCashPlanLimitXml upsertPojo, Map<CodeType, Map<Long, String>> allCodes) {

        PlDirectionLine pl = upsertPojo.plDirectionLineWrapper().plDirectionLine();

        return CashPlanLimit.builder()
                .year(Long.valueOf(LocalDate.now().getYear()))
                .kadmrCode(getCodeId(allCodes, KADMR, upsertPojo.kadmrCode()))
                .kfsrCode(getCodeId(allCodes, KFSR, upsertPojo.kfsrCode()))
                .kcsrCode(getCodeId(allCodes, KCSR, upsertPojo.kcsrCode()))
                .kvrCode(getCodeId(allCodes, KVR, upsertPojo.kvrCode()))
                .kesrCode(getCodeId(allCodes, KESR, upsertPojo.kesrCode()))
                .kdeCode(getCodeId(allCodes, KDE, upsertPojo.kdeCode()))
                .kdrCode(getCodeId(allCodes, KDR, upsertPojo.kdrCode()))
                .purposeCode(getCodeId(allCodes, PURPOSEFULGRANT, upsertPojo.purposeFulGrantCode()))
                .kdfCode(getCodeId(allCodes, KDF, upsertPojo.kdfCode()))
                .limitTotalAmt(getTotalLimit(pl))
                .limitFederalAmt(getTotalFederal(pl))
                .limitRegionalAmt(getTotalRegional(pl))
                .m01Amt(upsertPojo.m01Amt())
                .m02Amt(upsertPojo.m02Amt())
                .m03Amt(upsertPojo.m03Amt())
                .m04Amt(upsertPojo.m04Amt())
                .m05Amt(upsertPojo.m05Amt())
                .m06Amt(upsertPojo.m06Amt())
                .m07Amt(upsertPojo.m07Amt())
                .m08Amt(upsertPojo.m08Amt())
                .m09Amt(upsertPojo.m09Amt())
                .m10Amt(upsertPojo.m10Amt())
                .m11Amt(upsertPojo.m11Amt())
                .m12Amt(upsertPojo.m12Amt())
                .build();
    }

    public CashPlanLimit toCpl(GetAttributesListResponseDto dto) {

        List<GetAttributesListResponseDto.Attribute> attributes = dto.attributes();

        return CashPlanLimit.builder()
                .id(dto.id())
                .version(dto.version())
                .year(getLongValue(getValue(attributes, 3303)))
                .kadmrCode(getLongValue(getValue(attributes, KADMR.getAttributeId())))
                .kfsrCode(getLongValue(getValue(attributes, KFSR.getAttributeId())))
                .kcsrCode(getLongValue(getValue(attributes, KCSR.getAttributeId())))
                .kvrCode(getLongValue(getValue(attributes, KVR.getAttributeId())))
                .kesrCode(getLongValue(getValue(attributes, KESR.getAttributeId())))
                .kdeCode(getLongValue(getValue(attributes, KDE.getAttributeId())))
                .kdrCode(getLongValue(getValue(attributes, KDR.getAttributeId())))
                .purposeCode(getLongValue(getValue(attributes, PURPOSEFULGRANT.getAttributeId())))
                .kdfCode(getLongValue(getValue(attributes, KDF.getAttributeId())))
                .limitTotalAmt(getBigDecimalValue(getValue(attributes, 1609)))
                .limitFederalAmt(getBigDecimalValue(getValue(attributes, 1828)))
                .limitRegionalAmt(getBigDecimalValue(getValue(attributes, 1829)))
                .m01Amt(getBigDecimalValue(getValue(attributes, 3275)))
                .m02Amt(getBigDecimalValue(getValue(attributes, 3277)))
                .m03Amt(getBigDecimalValue(getValue(attributes, 3279)))
                .m04Amt(getBigDecimalValue(getValue(attributes, 3281)))
                .m05Amt(getBigDecimalValue(getValue(attributes, 3283)))
                .m06Amt(getBigDecimalValue(getValue(attributes, 3285)))
                .m07Amt(getBigDecimalValue(getValue(attributes, 3287)))
                .m08Amt(getBigDecimalValue(getValue(attributes, 3289)))
                .m09Amt(getBigDecimalValue(getValue(attributes, 3291)))
                .m10Amt(getBigDecimalValue(getValue(attributes, 3293)))
                .m11Amt(getBigDecimalValue(getValue(attributes, 3295)))
                .m12Amt(getBigDecimalValue(getValue(attributes, 3297)))
                .build();
    }


    public CashPlanLimit toCpl(CreateCashPlanLimitExcel cplExcel, Map<CodeType, Map<Long, String>> allCodes) {

        return CashPlanLimit.builder()
                .year((long) LocalDateTime.now().getYear())
                .kfsrCode(getCodeId(allCodes, KFSR, cplExcel.section() + cplExcel.subsection()))
                .kadmrCode(getCodeId(allCodes, KADMR, cplExcel.kvsr()))
                .kcsrCode(getCodeId(allCodes, KCSR, cplExcel.kcsr()))
                .kvrCode(getCodeId(allCodes, KVR, cplExcel.kvr()))
                .kesrCode(getCodeId(allCodes, KESR, cplExcel.kosgu()))
                .kdeCode(getCodeId(allCodes, KDE, cplExcel.additionalEk()))
                .kdrCode(getCodeId(allCodes, KDR, cplExcel.additionalKr()))
                .purposeCode(getCodeId(allCodes, PURPOSEFULGRANT, cplExcel.purposeCode()))
                .kdfCode(getCodeId(allCodes, KDF, cplExcel.additionalFk()))
                .limitTotalAmt(BigDecimal.valueOf(cplExcel.assignTotal()))
                .limitFederalAmt(BigDecimal.valueOf(cplExcel.assignFederal()))
                .limitRegionalAmt(BigDecimal.valueOf(cplExcel.assignRegional()))
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
                .build();
    }

    public CreateInstanceRequestDto toCreateDto(CashPlanLimit cpl) {
        CreateInstanceRequestDto dto = new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(CASH_PLAN_LIMIT_TEMPLATE_ID)
                        .attributes(getAttributes(cpl))
                        .build());
        return dto;
    }

    public UpsertInstanceRequestDto toUpdateDto(CashPlanLimit cpl) {
        UpsertInstanceRequestDto dto = new UpsertInstanceRequestDto(
                InstanceDto.builder()
                        .id(cpl.getId())
                        .templateId(CASH_PLAN_LIMIT_TEMPLATE_ID)
                        .version(cpl.getVersion())
                        .attributes(getAttributes(cpl))
                        .build());
        return dto;
    }

    private static List<Attribute> getAttributes(CashPlanLimit cpl) {
        return List.of(
                new LongAttribute(3303, cpl.getYear()),
                new DoubleAttribute(1609, cpl.getLimitTotalAmt()),
                new DoubleAttribute(1828, cpl.getLimitFederalAmt()),
                new DoubleAttribute(1829, cpl.getLimitRegionalAmt()),
                new LinkedAttribute(1733, cpl.getKadmrCode()),
                new LinkedAttribute(1734, cpl.getKfsrCode()),
                new LinkedAttribute(1735, cpl.getKcsrCode()),
                new LinkedAttribute(1736, cpl.getKvrCode()),
                new LinkedAttribute(1737, cpl.getKesrCode()),
                new LinkedAttribute(1739, cpl.getKdeCode()),
                new LinkedAttribute(1740, cpl.getKdrCode()),
                new LinkedAttribute(1751, cpl.getPurposeCode()),
                new LinkedAttribute(3448, cpl.getKdfCode()),
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
                new DoubleAttribute(1629, cpl.getM12Amt())
        );
    }

    private BigDecimal getBigDecimalValue(String data) {
        return data != null ? new BigDecimal(data) : null;
    }

    private Long getLongValue(String data) {
        return data != null ? Long.valueOf(data) : null;
    }

    private String getValue(List<GetAttributesListResponseDto.Attribute> attributes, long attributeId) {
        var attribute = attributes.stream()
                .filter(a -> a.id() == attributeId)
                .findFirst()
                .orElse(null);
        return attribute != null && attribute.value() != null ? attribute.value().get(0).data() : null;
    }

    private PlDirectionLine getPlDirectionLine(Line line) {
        if (line.plDirectionLineWrapper() != null &&
                line.plDirectionLineWrapper().plDirectionLine() != null) {
            return line.plDirectionLineWrapper().plDirectionLine();
        }
        return null;
    }
}

package su.petrosoft.apk_ack_integration.model.data.xml.rpl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.math.BigDecimal;

public record ExpCashPlanBoLinesItem(
        @JacksonXmlProperty(isAttribute = true, localName = "ESTIMATE_ID") Long estimateId,
        @JacksonXmlProperty(isAttribute = true, localName = "LAWACT_ID") Long lawactId,
        @JacksonXmlProperty(isAttribute = true, localName = "RECIPIENT_ID") Long recipientId,

        @JacksonXmlProperty(isAttribute = true, localName = "KFSR_CODE") Long kfsrCode,
        @JacksonXmlProperty(isAttribute = true, localName = "KCSR_CODE") Long kcsrCode,
        @JacksonXmlProperty(isAttribute = true, localName = "KVR_CODE") Long kvrCode,
        @JacksonXmlProperty(isAttribute = true, localName = "KESR_CODE") Long kesrCode,
        @JacksonXmlProperty(isAttribute = true, localName = "KADMR_CODE") Long kadmrCode,
        @JacksonXmlProperty(isAttribute = true, localName = "KDF_CODE") Long kdfCode,
        @JacksonXmlProperty(isAttribute = true, localName = "KDE_CODE") Long kdeCode,
        @JacksonXmlProperty(isAttribute = true, localName = "KDR_CODE") Long kdrCode,

        @JacksonXmlProperty(isAttribute = true, localName = "M_01_AMT") BigDecimal m01Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_02_AMT") BigDecimal m02Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_03_AMT") BigDecimal m03Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_04_AMT") BigDecimal m04Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_05_AMT") BigDecimal m05Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_06_AMT") BigDecimal m06Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_07_AMT") BigDecimal m07Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_08_AMT") BigDecimal m08Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_09_AMT") BigDecimal m09Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_10_AMT") BigDecimal m10Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_11_AMT") BigDecimal m11Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_12_AMT") BigDecimal m12Amt,

        @JacksonXmlProperty(isAttribute = true, localName = "DESCRIPTION_ID") Long descriptionId,
        @JacksonXmlProperty(isAttribute = true, localName = "BUDGORDER_ID") Long budgorderId,
        @JacksonXmlProperty(isAttribute = true, localName = "BO_DATA") String boData,
        @JacksonXmlProperty(isAttribute = true, localName = "BO_STAGEBUDGET_DATA") String boStagebudgetData
) {}

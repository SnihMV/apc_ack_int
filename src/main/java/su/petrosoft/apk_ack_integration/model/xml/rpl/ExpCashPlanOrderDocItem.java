package su.petrosoft.apk_ack_integration.model.xml.rpl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpCashPlanOrderDocItem(
        @JacksonXmlProperty(isAttribute = true, localName = "DOC_ID") Long docId,
        @JacksonXmlProperty(isAttribute = true, localName = "DOC_NUMBER") String docNumber,
        @JacksonXmlProperty(isAttribute = true, localName = "DOC_DATE") LocalDateTime docDate,
        @JacksonXmlProperty(isAttribute = true, localName = "AMOUNT") BigDecimal amount,
        @JacksonXmlProperty(isAttribute = true, localName = "DESCRIPTION") String description,
        @JacksonXmlProperty(isAttribute = true, localName = "OPERTYPE_ID") Long opertypeId,
        @JacksonXmlProperty(isAttribute = true, localName = "ORG_ID") Long orgId,
        @JacksonXmlProperty(isAttribute = true, localName = "PAY_NAME") String payName,

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

        @JacksonXmlProperty(isAttribute = true, localName = "KFSR_CODE") Long kfsrCode,
        @JacksonXmlProperty(isAttribute = true, localName = "KCSR_CODE") Long kcsrCode,
        @JacksonXmlProperty(isAttribute = true, localName = "KVR_CODE") Long kvrCode,
        @JacksonXmlProperty(isAttribute = true, localName = "KESR_CODE") Long kesrCode,
        @JacksonXmlProperty(isAttribute = true, localName = "INDUSTRYCODE_ID") Long industryCodeId,
        @JacksonXmlProperty(isAttribute = true, localName = "KSDA_CODE") String ksdaCode,
        @JacksonXmlProperty(isAttribute = true, localName = "GRANTINVESTMENT_ID") Long grantInvestmentId,
        @JacksonXmlProperty(isAttribute = true, localName = "FSR_ID") Long fsrId
) {}

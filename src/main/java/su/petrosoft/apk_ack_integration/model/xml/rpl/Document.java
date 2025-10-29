package su.petrosoft.apk_ack_integration.model.xml.rpl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Document(
        @JacksonXmlProperty(isAttribute = true, localName = "ID") Long id,
        @JacksonXmlProperty(isAttribute = true, localName = "DOCUMENT_ID") Long documentId,

        @JacksonXmlProperty(isAttribute = true, localName = "DOC_NUMBER") String docNumber,
        @JacksonXmlProperty(isAttribute = true, localName = "DOC_DATE") LocalDateTime docDate,
        @JacksonXmlProperty(isAttribute = true, localName = "CARRY_DATE") LocalDateTime carryDate,
        @JacksonXmlProperty(isAttribute = true, localName = "BEGIN_AGREEMENT_DATE") LocalDateTime beginAgreementDate,

        @JacksonXmlProperty(isAttribute = true, localName = "BUDGET_ID") Long budgetId,
        @JacksonXmlProperty(isAttribute = true, localName = "BUDGET_CAPTION") String budgetCaption,
        @JacksonXmlProperty(isAttribute = true, localName = "BUDGET_CODE") Integer budgetCode,
        @JacksonXmlProperty(isAttribute = true, localName = "BUDGET_YEAR") Integer budgetYear,

        @JacksonXmlProperty(isAttribute = true, localName = "OPERTYPE_ID") Long opertypeId,
        @JacksonXmlProperty(isAttribute = true, localName = "AMOUNT") BigDecimal amount,
        @JacksonXmlProperty(isAttribute = true, localName = "DESCRIPTION") String description,
        @JacksonXmlProperty(isAttribute = true, localName = "REMARK") String remark,

        @JacksonXmlProperty(localName = "LINES") Lines lines,
        @JacksonXmlProperty(localName = "EXPCASHPLANDOC") ExpCashPlanDoc expCashPlanDoc,
        @JacksonXmlProperty(localName = "EXPCASHPLANBOLINES") ExpCashPlanBoLines expCashPlanBoLines,
        @JacksonXmlProperty(localName = "EXPCASHPLANORDERDOC") ExpCashPlanOrderDoc expCashPlanOrderDoc,
        @JacksonXmlProperty(localName = "ATTACH") Attach attach
) {}

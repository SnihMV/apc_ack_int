package su.petrosoft.apk_ack_integration.model.data.xml.rpl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record Header(
        @JacksonXmlProperty(isAttribute = true, localName = "ORG_TAXCODE") String orgTaxcode,
        @JacksonXmlProperty(isAttribute = true, localName = "ORG_KPP") String orgKpp,
        @JacksonXmlProperty(isAttribute = true, localName = "ORG_CODE_FK") String orgCodeFk,
        @JacksonXmlProperty(isAttribute = true, localName = "DOCUMENT_ID") Long documentId,
        @JacksonXmlProperty(isAttribute = true, localName = "class") Long documentClass,
        @JacksonXmlProperty(isAttribute = true, localName = "status") Long status,
        @JacksonXmlProperty(isAttribute = true, localName = "RecordTimeStamp") String recordTimeStamp,
        @JacksonXmlProperty(isAttribute = true, localName = "budget_name") String budgetName,
        @JacksonXmlProperty(isAttribute = true, localName = "budget_year") Integer budgetYear,
        @JacksonXmlProperty(isAttribute = true, localName = "system_code") String systemCode
) {}

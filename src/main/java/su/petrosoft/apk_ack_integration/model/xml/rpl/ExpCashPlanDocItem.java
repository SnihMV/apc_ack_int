package su.petrosoft.apk_ack_integration.model.xml.rpl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.time.LocalDateTime;

public record ExpCashPlanDocItem(
        @JacksonXmlProperty(isAttribute = true, localName = "DOC_ID") Long docId,
        @JacksonXmlProperty(isAttribute = true, localName = "DOC_NUMBER") String docNumber,
        @JacksonXmlProperty(isAttribute = true, localName = "DOC_DATE") LocalDateTime docDate
) {}

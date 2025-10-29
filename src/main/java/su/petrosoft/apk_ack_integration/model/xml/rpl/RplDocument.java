package su.petrosoft.apk_ack_integration.model.xml.rpl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "RPL")
public record RplDocument(
        @JacksonXmlProperty(isAttribute = true, localName = "CLIENT_ID") Long clientId,
        @JacksonXmlProperty(isAttribute = true, localName = "MASTER_ID") Long masterId,
        @JacksonXmlProperty(isAttribute = true, localName = "action") String action,

        @JacksonXmlProperty(localName = "HEADER") Header header,
        @JacksonXmlProperty(localName = "DOCUMENT") Document document
) {}
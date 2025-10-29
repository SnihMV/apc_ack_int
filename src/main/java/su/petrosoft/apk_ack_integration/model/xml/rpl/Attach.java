package su.petrosoft.apk_ack_integration.model.xml.rpl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public record Attach(
        @JacksonXmlProperty(localName = "ATTACH")
        List<AttachItem> items
) {}

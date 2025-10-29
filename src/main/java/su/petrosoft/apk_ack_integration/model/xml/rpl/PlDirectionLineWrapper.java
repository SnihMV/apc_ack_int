package su.petrosoft.apk_ack_integration.model.xml.rpl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record PlDirectionLineWrapper(
        @JacksonXmlProperty(localName = "PL_DIRECTIONLINE")
        PlDirectionLine plDirectionLine
) {
}

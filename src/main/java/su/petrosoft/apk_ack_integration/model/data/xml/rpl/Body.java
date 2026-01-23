package su.petrosoft.apk_ack_integration.model.data.xml.rpl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public record Body(
        @JacksonXmlText String value
) {
}

package su.petrosoft.apk_ack_integration.model.data.xml.rpl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public record ExpCashPlanBoLines(
        @JacksonXmlProperty(localName = "EXPCASHPLANBOLINES")
        List<ExpCashPlanBoLinesItem> items
) {}

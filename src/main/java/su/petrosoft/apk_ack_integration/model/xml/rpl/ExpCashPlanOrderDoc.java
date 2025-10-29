package su.petrosoft.apk_ack_integration.model.xml.rpl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public record ExpCashPlanOrderDoc(
        @JacksonXmlProperty(localName = "EXPCASHPLANORDERDOC")
        List<ExpCashPlanOrderDocItem> items
) {}

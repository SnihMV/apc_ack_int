package su.petrosoft.apk_ack_integration.model.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.math.BigDecimal;
import java.util.List;

@JacksonXmlRootElement(localName = "Objects")
public record CreateSubsidiesEstimatedAmountsXml(
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "Object")
        List<SubsidyEstimatedAmountXml> objects
) {
    public record SubsidyEstimatedAmountXml(
            @JacksonXmlProperty(localName = "Year")
            Long year,

            @JacksonXmlProperty(localName = "ApplicantINN")
            String recipientINN,

            @JacksonXmlProperty(localName = "SubsidyAmountNextYear")
            BigDecimal sob,

            @JacksonXmlProperty(localName = "SubsidyForObligations")
            BigDecimal sst,

            @JacksonXmlProperty(localName = "SubsidyForCreditAgreements")
            BigDecimal sn,

            @JacksonXmlProperty(localName = "Direction")
            String kcsr,

            @JacksonXmlProperty(localName = "DopKR")
            String dopKR
    ) {
    }
}
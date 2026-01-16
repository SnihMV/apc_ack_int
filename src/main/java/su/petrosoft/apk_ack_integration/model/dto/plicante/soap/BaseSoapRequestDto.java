package su.petrosoft.apk_ack_integration.model.dto.plicante.soap;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "soapenv:Envelope")
public abstract class BaseSoapRequestDto {

    @JacksonXmlProperty(localName = "xmlns:soapenv", isAttribute = true)
    private final String soapenv = "http://schemas.xmlsoap.org/soap/envelope/";

    @JacksonXmlProperty(localName = "xmlns:reg", isAttribute = true)
    private final String reg = "http://ru.petrosoft/register";

    @JacksonXmlProperty(localName = "soapenv:Header")
    private final String header = "";

    @JacksonXmlProperty(localName = "soapenv:Body")
    protected abstract Object getBody();
}

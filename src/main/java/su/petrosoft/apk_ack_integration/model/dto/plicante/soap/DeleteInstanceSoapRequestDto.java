package su.petrosoft.apk_ack_integration.model.dto.plicante.soap;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "soapenv:Envelope")
public class DeleteInstanceSoapRequestDto {

    @JacksonXmlProperty(localName = "xmlns:soapenv", isAttribute = true)
    private final String soapenv = "http://schemas.xmlsoap.org/soap/envelope/";

    @JacksonXmlProperty(localName = "xmlns:reg", isAttribute = true)
    private final String reg = "http://ru.petrosoft/register";

    @JacksonXmlProperty(localName = "soapenv:Header")
    private final String header = null;

    @JacksonXmlProperty(localName = "soapenv:Body")
    private final Body body;

    public DeleteInstanceSoapRequestDto(long instanceId) {
        this.body = new Body(new DeleteInstance(instanceId));
    }

    @JacksonXmlRootElement(localName = "soapenv:Body")
    private record Body(
            @JacksonXmlProperty(localName = "reg:deleteInstance")
            DeleteInstance deleteInstance
    ) {
        private Body(DeleteInstance deleteInstance) {
            this.deleteInstance = deleteInstance;
        }
    }

    private record DeleteInstance(
            @JacksonXmlProperty(localName = "instanceId")
            long instanceId
    ) {
        private DeleteInstance(long instanceId) {
            this.instanceId = instanceId;
        }
    }
}

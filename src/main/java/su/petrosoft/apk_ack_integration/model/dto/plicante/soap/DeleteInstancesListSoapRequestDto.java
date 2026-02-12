package su.petrosoft.apk_ack_integration.model.dto.plicante.soap;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Collection;
import java.util.List;

@JacksonXmlRootElement(localName = "soapenv:Envelope")
public class DeleteInstancesListSoapRequestDto {

    @JacksonXmlProperty(localName = "xmlns:soapenv", isAttribute = true)
    private final String soapenv = "http://schemas.xmlsoap.org/soap/envelope/";

    @JacksonXmlProperty(localName = "xmlns:reg", isAttribute = true)
    private final String reg = "http://ru.petrosoft/register";

    @JacksonXmlProperty(localName = "soapenv:Header")
    private final String header = null;

    @JacksonXmlProperty(localName = "soapenv:Body")
    private final Body body;

    public DeleteInstancesListSoapRequestDto(Collection<Long> instanceIds) {
        this.body = new Body(new DeleteInstances(instanceIds));
    }

    @JacksonXmlRootElement(localName = "soapenv:Body")
    private record Body(
            @JacksonXmlProperty(localName = "reg:deleteInstances")
            DeleteInstances deleteInstances
    ) {
        private Body(DeleteInstances deleteInstances) {
            this.deleteInstances = deleteInstances;
        }
    }

    private record DeleteInstances(
            @JacksonXmlElementWrapper(useWrapping = false)
            @JacksonXmlProperty(localName = "instancesIds")
            Collection<Long> instancesIds
    ) {
        private DeleteInstances(Collection<Long> instancesIds) {
            this.instancesIds = instancesIds;
        }
    }
}

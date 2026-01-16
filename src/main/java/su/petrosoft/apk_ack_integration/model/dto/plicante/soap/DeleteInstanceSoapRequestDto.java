package su.petrosoft.apk_ack_integration.model.dto.plicante.soap;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class DeleteInstanceSoapRequestDto extends BaseSoapRequestDto {

    @JacksonXmlProperty(localName = "reg:deleteInstance")
    private final DeleteInstanceTag deleteInstanceTag;

    public DeleteInstanceSoapRequestDto(long instanceId) {
        this.deleteInstanceTag = new DeleteInstanceTag(instanceId);
    }

    @Override
    protected Object getBody() {
        return deleteInstanceTag;
    }

    private record DeleteInstanceTag(
            @JacksonXmlProperty long instanceId
    ) {
    }
}

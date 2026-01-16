package su.petrosoft.apk_ack_integration.model.dto.plicante.soap;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class DeleteInstancesListSoapRequestDto extends BaseSoapRequestDto {

    @JacksonXmlProperty(localName = "reg:deleteInstances")
    private final DeleteInstancesTag deleteInstancesTag;

    public DeleteInstancesListSoapRequestDto(List<Long> instanceIds) {
        this.deleteInstancesTag = new DeleteInstancesTag(instanceIds);
    }

    @Override
    protected Object getBody() {
        return deleteInstancesTag;
    }

    private record DeleteInstancesTag(
            @JacksonXmlProperty List<Long> instancesIds
    ){

    }
}

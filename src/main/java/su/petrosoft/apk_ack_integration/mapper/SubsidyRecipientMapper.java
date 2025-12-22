package su.petrosoft.apk_ack_integration.mapper;

import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.SubsidyRecipient;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;

import java.util.List;

import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractData;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.*;

@Component
public class SubsidyRecipientMapper {
    public SubsidyRecipient toEntity(InstanceDto dto) {
        List<Attribute<?>> attributes = dto.attributes();
        return SubsidyRecipient.builder()
                .id(dto.id())
                .version(dto.version())
                .fullTitle(extractData(attributes, FULL_TITLE_ATTR))
                .shortTitle(extractData(attributes, SHORT_TITLE_ATTR))
                .inn(extractData(attributes, INN_ATTR))
                .kpp(extractData(attributes, KPP_ATTR))
                .ogrn(extractData(attributes, OGRN_ATTR))
                .build();

    }
}

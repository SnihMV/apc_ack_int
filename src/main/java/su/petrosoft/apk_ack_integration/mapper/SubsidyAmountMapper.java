package su.petrosoft.apk_ack_integration.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.SubsidyAmount;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DoubleAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LongAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;

import java.util.List;
import java.util.Map;

import static su.petrosoft.apk_ack_integration.model.data.xml.CreatingSubsidiesAmountsXml.SubsidyAmountXml;
import static su.petrosoft.apk_ack_integration.util.SubsidyAmountUtil.PROGRAM_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyAmountUtil.RECIPIENT_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyAmountUtil.SN_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyAmountUtil.SOB_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyAmountUtil.SST_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyAmountUtil.TEMPLATE_ID;
import static su.petrosoft.apk_ack_integration.util.SubsidyAmountUtil.YEAR_ATTR;

@Slf4j
@Component
public class SubsidyAmountMapper {

    public SubsidyAmount toEntity(
            SubsidyAmountXml xml,
            Long subsidyProgramId,
            Map<String, Long> innToIdMap
    ) {
        return SubsidyAmount.builder()
                .year(xml.year())
                .recipientId(innToIdMap.get(xml.recipientINN()))
                .subsidyProgramId(subsidyProgramId)
                .sob(xml.sob())
                .sst(xml.sst())
                .sn(xml.sn())
                .build();
    }

    public CreateInstanceRequestDto toCreatingDto(SubsidyAmount subsidyAmount) {
        return new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(TEMPLATE_ID)
                        .attributes(List.of(
                                new LongAttribute(YEAR_ATTR, subsidyAmount.getYear()),
                                new LinkedAttribute(RECIPIENT_ATTR, subsidyAmount.getRecipientId()),
                                new LinkedAttribute(PROGRAM_ATTR, subsidyAmount.getSubsidyProgramId()),
                                new DoubleAttribute(SOB_ATTR, subsidyAmount.getSob()),
                                new DoubleAttribute(SST_ATTR, subsidyAmount.getSst()),
                                new DoubleAttribute(SN_ATTR, subsidyAmount.getSn())
                        ))
                        .build()
        );

    }
}

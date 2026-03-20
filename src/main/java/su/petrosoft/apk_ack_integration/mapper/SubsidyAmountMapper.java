package su.petrosoft.apk_ack_integration.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.SubsidyAmount;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DoubleAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LongAttributeDto;
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
                                new LongAttributeDto(YEAR_ATTR, subsidyAmount.getYear()),
                                new LinkedAttributeDto(RECIPIENT_ATTR, subsidyAmount.getRecipientId()),
                                new LinkedAttributeDto(PROGRAM_ATTR, subsidyAmount.getSubsidyProgramId()),
                                new DoubleAttributeDto(SOB_ATTR, subsidyAmount.getSob()),
                                new DoubleAttributeDto(SST_ATTR, subsidyAmount.getSst()),
                                new DoubleAttributeDto(SN_ATTR, subsidyAmount.getSn())
                        ))
                        .build()
        );

    }
}

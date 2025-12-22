package su.petrosoft.apk_ack_integration.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.SubsidyEstimatedAmount;

import static su.petrosoft.apk_ack_integration.model.xml.CreatingSubsidiesAmountsXml.SubsidyAmountXml;

@Slf4j
@Component
public class SubsidyEstimatedAmountMapper {
    public SubsidyEstimatedAmount toEntity(SubsidyAmountXml xml) {
       return SubsidyEstimatedAmount.builder()
                .year(xml.year())
//                .applicantId(getApplicantIdByInn(xml.recipientINN()))
                .sob(xml.sob())
                .sst(xml.sst())
                .sn(xml.sn())
                .build();
    }

}

package su.petrosoft.apk_ack_integration.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.SubsidyEstimatedAmount;

import static su.petrosoft.apk_ack_integration.model.xml.CreateSubsidiesEstimatedAmountsXml.SubsidyEstimatedAmountXml;

@Slf4j
@Component
public class SubsidyEstimatedAmountMapper {
    public SubsidyEstimatedAmount toEntity(SubsidyEstimatedAmountXml xml) {
       return SubsidyEstimatedAmount.builder()
                .year(xml.year())
//                .applicantId(getApplicantIdByInn(xml.recipientINN()))
                .sob(xml.subsidyAmountNextYear())
                .sst(xml.subsidyForObligations())
                .sn(xml.subsidyForCreditAgreements())
                .build();
    }

}

package su.petrosoft.apk_ack_integration.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryParkUtil.TEMPLATE_ID;

@Getter
@Setter
@SuperBuilder
public class AgriculturalMachineryPark extends PlicanteInstance {
    private Long recipientId;
    private Long districtId;
    private Long indicator;
    private Long machineryAndEquip;
    private String brandModel;
    private String serialNumber;
    private Long count;
    private BigDecimal power;
    private BigDecimal cost;
    private Long productionCountry;
    private Long productionYear;
    private Boolean stateSupport;
    private Long techState;

    @Override
    public long getTemplateId() {
        return TEMPLATE_ID;
    }
}

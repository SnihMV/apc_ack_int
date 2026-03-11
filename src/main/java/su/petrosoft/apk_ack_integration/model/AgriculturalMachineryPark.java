package su.petrosoft.apk_ack_integration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

}

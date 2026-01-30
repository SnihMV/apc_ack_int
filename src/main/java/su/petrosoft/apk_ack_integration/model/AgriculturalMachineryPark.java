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
    private String indicator;
    private String machineryAndEquip;
    private String brandModel;
    private Long count;
    private BigDecimal power;
    private BigDecimal cost;
    private String productionCountry;
    private Long productionYear;
    private Boolean stateSupport;
    private String techState;

}

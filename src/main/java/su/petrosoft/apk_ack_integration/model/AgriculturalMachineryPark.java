package su.petrosoft.apk_ack_integration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgriculturalMachineryPark {
    private Long id;
    private Long version;
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

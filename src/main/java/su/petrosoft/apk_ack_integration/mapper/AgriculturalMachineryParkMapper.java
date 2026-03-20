package su.petrosoft.apk_ack_integration.mapper;

import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryParkUtil.BRAND_MODEL_ATTR;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryParkUtil.COST_ATTR;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryParkUtil.COUNT_ATTR;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryParkUtil.DISTRICT_ATTR;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryParkUtil.INDICATOR_ATTR;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryParkUtil.MACH_EQUIP_ATTR;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryParkUtil.POWER_ATTR;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryParkUtil.PROD_COUNTRY_ATTR;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryParkUtil.PROD_YEAR_ATTR;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryParkUtil.RECIPIENT_ATTR;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryParkUtil.SERIAL_NUMBER_ATTR;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryParkUtil.STATE_SUPPORT_ATTR;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryParkUtil.TECH_STATE_ATTR;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryParkUtil.TEMPLATE_ID;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.AgriculturalMachineryPark;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.BooleanAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DoubleAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LongAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;


@Slf4j
@Component
public class AgriculturalMachineryParkMapper {

    public AgriculturalMachineryPark toEntity(InstanceDto dto) {
        return AgriculturalMachineryPark.builder()
                .id(dto.id())
                .version(dto.version())
                .build();
    }

    public CreateInstanceRequestDto toCreationDto(AgriculturalMachineryPark park) {
        return new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(TEMPLATE_ID)
                        .attributeDtos(List.of(
                                new LinkedAttributeDto(RECIPIENT_ATTR, park.getRecipientId()),
                                new LinkedAttributeDto(DISTRICT_ATTR, park.getDistrictId()),
                                new LinkedAttributeDto(INDICATOR_ATTR, park.getIndicator()),
                                new LinkedAttributeDto(MACH_EQUIP_ATTR, park.getMachineryAndEquip()),
                                new StringAttributeDto(BRAND_MODEL_ATTR, park.getBrandModel()),
                                new StringAttributeDto(SERIAL_NUMBER_ATTR, park.getSerialNumber()),
                                new LongAttributeDto(COUNT_ATTR, park.getCount()),
                                new DoubleAttributeDto(POWER_ATTR, park.getPower()),
                                new DoubleAttributeDto(COST_ATTR, park.getCost()),
                                new LinkedAttributeDto(PROD_COUNTRY_ATTR, park.getProductionCountry()),
                                new LongAttributeDto(PROD_YEAR_ATTR, park.getProductionYear()),
                                new BooleanAttributeDto(STATE_SUPPORT_ATTR, park.getStateSupport()),
                                new LinkedAttributeDto(TECH_STATE_ATTR, park.getTechState())
                        ))
                        .build());
    }


}

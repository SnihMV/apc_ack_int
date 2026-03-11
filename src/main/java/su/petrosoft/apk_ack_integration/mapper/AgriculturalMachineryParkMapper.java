package su.petrosoft.apk_ack_integration.mapper;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Long.parseLong;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.PROD_COUNTRY;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.TECH_STATE;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.values;
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
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.dictionaryIdByCode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.AgriculturalMachineryPark;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.BooleanAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DoubleAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LongAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;


@Slf4j
@Component
public class AgriculturalMachineryParkMapper {

    public AgriculturalMachineryPark toEntity(InstanceDto dto) {
        return AgriculturalMachineryPark.builder()
                .id(dto.id())
                .version(dto.version())
                .build();
    }

    public CreateInstanceRequestDto toCreationDto(AgriculturalMachineryPark park, Map<Dictionary, Map<String, Long>> codesMap) {
        return new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(TEMPLATE_ID)
                        .attributes(List.of(
                                new LinkedAttribute(RECIPIENT_ATTR, park.getRecipientId()),
                                new LinkedAttribute(DISTRICT_ATTR, park.getDistrictId()),
                                new LinkedAttribute(INDICATOR_ATTR, park.getIndicator()),
                                new LinkedAttribute(MACH_EQUIP_ATTR, park.getMachineryAndEquip()),
                                new StringAttribute(BRAND_MODEL_ATTR, park.getBrandModel()),
                                new StringAttribute(SERIAL_NUMBER_ATTR, park.getSerialNumber()),
                                new LongAttribute(COUNT_ATTR, park.getCount()),
                                new DoubleAttribute(POWER_ATTR, park.getPower()),
                                new DoubleAttribute(COST_ATTR, park.getCost()),
                                new LinkedAttribute(PROD_COUNTRY_ATTR, park.getProductionCountry()),
                                new LongAttribute(PROD_YEAR_ATTR, park.getProductionYear()),
                                new BooleanAttribute(STATE_SUPPORT_ATTR, park.getStateSupport()),
                                new LinkedAttribute(TECH_STATE_ATTR, park.getTechState())
                        ))
                        .build());
    }


}

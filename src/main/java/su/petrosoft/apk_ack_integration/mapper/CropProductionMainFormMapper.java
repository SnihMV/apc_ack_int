package su.petrosoft.apk_ack_integration.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.CropProductionMainForm;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.AttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DoubleAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;

import java.util.List;

import static su.petrosoft.apk_ack_integration.util.CropProductionUtil.*;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractData;

@Slf4j
@Component
public class CropProductionMainFormMapper {

    public UpdateInstanceRequestDto toUpdateDto(CropProductionMainForm mainForm) {
        return new UpdateInstanceRequestDto(
                InstanceDto.builder()
                        .id(mainForm.getId())
                        .templateId(TEMPLATE_ID)
                        .version(mainForm.getVersion())
                        .attributes(buildAttributeListToUpdate(mainForm))
                        .build()
        );
    }

    private List<AttributeDto<?>> buildAttributeListToUpdate(CropProductionMainForm mainForm) {
        return List.of(
                new DoubleAttributeDto(B1_L1_A1, mainForm.getB1_l1_a1()),
                new DoubleAttributeDto(B1_L1_A2, mainForm.getB1_l1_a2()),
                new DoubleAttributeDto(B1_L1_A3, mainForm.getB1_l1_a3()),
                new DoubleAttributeDto(B1_L1_A4, mainForm.getB1_l1_a4()),
                new DoubleAttributeDto(B1_L1_A5, mainForm.getB1_l1_a5()),
                new DoubleAttributeDto(B1_L2_A1, mainForm.getB1_l2_a1()),
                new DoubleAttributeDto(B1_L2_A2, mainForm.getB1_l2_a2()),
                new DoubleAttributeDto(B1_L2_A3, mainForm.getB1_l2_a3()),
                new DoubleAttributeDto(B1_L2_A4, mainForm.getB1_l2_a4()),
                new DoubleAttributeDto(B1_L2_A5, mainForm.getB1_l2_a5()),
                new DoubleAttributeDto(B1_L3_A1, mainForm.getB1_l3_a1()),
                new DoubleAttributeDto(B1_L3_A2, mainForm.getB1_l3_a2()),
                new DoubleAttributeDto(B1_L3_A3, mainForm.getB1_l3_a3()),
                new DoubleAttributeDto(B1_L3_A4, mainForm.getB1_l3_a4()),
                new DoubleAttributeDto(B1_L3_A5, mainForm.getB1_l3_a5()),
                new DoubleAttributeDto(B1_L4_A1, mainForm.getB1_l4_a1()),
                new DoubleAttributeDto(B1_L4_A2, mainForm.getB1_l4_a2()),
                new DoubleAttributeDto(B1_L4_A3, mainForm.getB1_l4_a3()),
                new DoubleAttributeDto(B1_L4_A4, mainForm.getB1_l4_a4()),
                new DoubleAttributeDto(B1_L4_A5, mainForm.getB1_l4_a5()),
                new DoubleAttributeDto(B1_L4_A6, mainForm.getB1_l4_a6()),
                new DoubleAttributeDto(B1_L5_A1, mainForm.getB1_l5_a1()),
                new DoubleAttributeDto(B1_L5_A2, mainForm.getB1_l5_a2()),
                new DoubleAttributeDto(B1_L5_A3, mainForm.getB1_l5_a3()),
                new DoubleAttributeDto(B1_L5_A4, mainForm.getB1_l5_a4()),
                new DoubleAttributeDto(B1_L5_A5, mainForm.getB1_l5_a5()),
                new DoubleAttributeDto(B1_L6_A1, mainForm.getB1_l6_a1()),
                new DoubleAttributeDto(B1_L6_A2, mainForm.getB1_l6_a2()),
                new DoubleAttributeDto(B1_L6_A3, mainForm.getB1_l6_a3()),
                new DoubleAttributeDto(B1_L6_A4, mainForm.getB1_l6_a4()),
                new DoubleAttributeDto(B1_L6_A5, mainForm.getB1_l6_a5()),
                new DoubleAttributeDto(B1_L7_A1, mainForm.getB1_l7_a1()),
                new DoubleAttributeDto(B1_L7_A2, mainForm.getB1_l7_a2()),
                new DoubleAttributeDto(B1_L7_A3, mainForm.getB1_l7_a3()),
                new DoubleAttributeDto(B1_L7_A4, mainForm.getB1_l7_a4()),
                new DoubleAttributeDto(B1_L7_A5, mainForm.getB1_l7_a5()),
                new DoubleAttributeDto(B1_L8_A1, mainForm.getB1_l8_a1()),
                new DoubleAttributeDto(B1_L8_A2, mainForm.getB1_l8_a2()),
                new DoubleAttributeDto(B1_L8_A3, mainForm.getB1_l8_a3()),
                new DoubleAttributeDto(B1_L8_A4, mainForm.getB1_l8_a4()),
                new DoubleAttributeDto(B1_L8_A5, mainForm.getB1_l8_a5()),
                new DoubleAttributeDto(B1_L9_A1, mainForm.getB1_l9_a1()),
                new DoubleAttributeDto(B1_L9_A2, mainForm.getB1_l9_a2()),
                new DoubleAttributeDto(B1_L10_A1, mainForm.getB1_l10_a1()),
                new DoubleAttributeDto(B1_L10_A2, mainForm.getB1_l10_a2()),
                new DoubleAttributeDto(B1_L11_A1, mainForm.getB1_l11_a1()),
                new DoubleAttributeDto(B1_L11_A2, mainForm.getB1_l11_a2()),
                new DoubleAttributeDto(B1_L12_A1, mainForm.getB1_l12_a1()),
                new DoubleAttributeDto(B1_L12_A2, mainForm.getB1_l12_a2()),
                new DoubleAttributeDto(B1_L13_A1, mainForm.getB1_l13_a1()),
                new DoubleAttributeDto(B1_L13_A2, mainForm.getB1_l13_a2()),
                new DoubleAttributeDto(B1_L14_A1, mainForm.getB1_l14_a1()),
                new DoubleAttributeDto(B1_L14_A2, mainForm.getB1_l14_a2()),
                new DoubleAttributeDto(B1_L15_A1, mainForm.getB1_l15_a1()),
                new DoubleAttributeDto(B1_L15_A2, mainForm.getB1_l15_a2()),
                new DoubleAttributeDto(B1_L16_A1, mainForm.getB1_l16_a1()),
                new DoubleAttributeDto(B1_L16_A2, mainForm.getB1_l16_a2()),
                new DoubleAttributeDto(B1_L16_A3, mainForm.getB1_l16_a3()),
                new DoubleAttributeDto(B1_L16_A4, mainForm.getB1_l16_a4()),
                new DoubleAttributeDto(B1_L16_A5, mainForm.getB1_l16_a5()),
                new DoubleAttributeDto(B2_L1_A1, mainForm.getB2_l1_a1()),
                new DoubleAttributeDto(B2_L1_A2, mainForm.getB2_l1_a2()),
                new DoubleAttributeDto(B2_L1_A3, mainForm.getB2_l1_a3()),
                new DoubleAttributeDto(B2_L1_A4, mainForm.getB2_l1_a4()),
                new DoubleAttributeDto(B2_L1_A5, mainForm.getB2_l1_a5()),
                new DoubleAttributeDto(B2_L2_A1, mainForm.getB2_l2_a1()),
                new DoubleAttributeDto(B2_L2_A2, mainForm.getB2_l2_a2()),
                new DoubleAttributeDto(B2_L3_A1, mainForm.getB2_l3_a1()),
                new DoubleAttributeDto(B2_L3_A2, mainForm.getB2_l3_a2()),
                new DoubleAttributeDto(B2_L4_A1, mainForm.getB2_l4_a1()),
                new DoubleAttributeDto(B2_L4_A2, mainForm.getB2_l4_a2()),
                new DoubleAttributeDto(B2_L4_A3, mainForm.getB2_l4_a3()),
                new DoubleAttributeDto(B2_L4_A4, mainForm.getB2_l4_a4()),
                new DoubleAttributeDto(B2_L4_A5, mainForm.getB2_l4_a5()),
                new DoubleAttributeDto(B2_L5_A1, mainForm.getB2_l5_a1()),
                new DoubleAttributeDto(B2_L5_A2, mainForm.getB2_l5_a2()),
                new DoubleAttributeDto(B2_L5_A3, mainForm.getB2_l5_a3()),
                new DoubleAttributeDto(B2_L5_A4, mainForm.getB2_l5_a4()),
                new DoubleAttributeDto(B2_L5_A5, mainForm.getB2_l5_a5()),
                new DoubleAttributeDto(B2_L6_A1, mainForm.getB2_l6_a1()),
                new DoubleAttributeDto(B2_L6_A2, mainForm.getB2_l6_a2()),
                new DoubleAttributeDto(B2_L6_A3, mainForm.getB2_l6_a3()),
                new DoubleAttributeDto(B2_L6_A4, mainForm.getB2_l6_a4()),
                new DoubleAttributeDto(B2_L6_A5, mainForm.getB2_l6_a5()),
                new DoubleAttributeDto(B2_L7_A1, mainForm.getB2_l7_a1()),
                new DoubleAttributeDto(B2_L7_A2, mainForm.getB2_l7_a2()),
                new DoubleAttributeDto(B2_L7_A3, mainForm.getB2_l7_a3()),
                new DoubleAttributeDto(B2_L7_A4, mainForm.getB2_l7_a4()),
                new DoubleAttributeDto(B2_L7_A5, mainForm.getB2_l7_a5()),
                new DoubleAttributeDto(B2_L8_A1, mainForm.getB2_l8_a1()),
                new DoubleAttributeDto(B2_L8_A2, mainForm.getB2_l8_a2()),
                new DoubleAttributeDto(B2_L8_A3, mainForm.getB2_l8_a3()),
                new DoubleAttributeDto(B2_L8_A4, mainForm.getB2_l8_a4()),
                new DoubleAttributeDto(B2_L8_A5, mainForm.getB2_l8_a5()),
                new DoubleAttributeDto(B2_L9_A1, mainForm.getB2_l9_a1()),
                new DoubleAttributeDto(B2_L9_A2, mainForm.getB2_l9_a2()),
                new DoubleAttributeDto(B2_L9_A3, mainForm.getB2_l9_a3()),
                new DoubleAttributeDto(B2_L9_A4, mainForm.getB2_l9_a4()),
                new DoubleAttributeDto(B2_L9_A5, mainForm.getB2_l9_a5()),
                new DoubleAttributeDto(B2_L9_A6, mainForm.getB2_l9_a6()),
                new DoubleAttributeDto(B2_L9_A7, mainForm.getB2_l9_a7()),
                new DoubleAttributeDto(B3_L1_A1, mainForm.getB3_l1_a1()),
                new DoubleAttributeDto(B3_L1_A2, mainForm.getB3_l1_a2()),
                new DoubleAttributeDto(B3_L1_A3, mainForm.getB3_l1_a3()),
                new DoubleAttributeDto(B3_L1_A4, mainForm.getB3_l1_a4()),
                new DoubleAttributeDto(B3_L1_A5, mainForm.getB3_l1_a5()),
                new DoubleAttributeDto(B3_L2_A1, mainForm.getB3_l2_a1()),
                new DoubleAttributeDto(B3_L2_A2, mainForm.getB3_l2_a2()),
                new DoubleAttributeDto(B3_L2_A3, mainForm.getB3_l2_a3()),
                new DoubleAttributeDto(B3_L2_A4, mainForm.getB3_l2_a4()),
                new DoubleAttributeDto(B3_L3_A1, mainForm.getB3_l3_a1()),
                new DoubleAttributeDto(B3_L3_A2, mainForm.getB3_l3_a2()),
                new DoubleAttributeDto(B3_L3_A3, mainForm.getB3_l3_a3()),
                new DoubleAttributeDto(B3_L4_A1, mainForm.getB3_l4_a1()),
                new DoubleAttributeDto(B3_L4_A2, mainForm.getB3_l4_a2()),
                new DoubleAttributeDto(B3_L4_A3, mainForm.getB3_l4_a3()),
                new DoubleAttributeDto(B3_L4_A4, mainForm.getB3_l4_a4()),
                new DoubleAttributeDto(B3_L5_A1, mainForm.getB3_l5_a1()),
                new DoubleAttributeDto(B3_L5_A2, mainForm.getB3_l5_a2()),
                new DoubleAttributeDto(B3_L5_A3, mainForm.getB3_l5_a3()),
                new DoubleAttributeDto(B3_L5_A4, mainForm.getB3_l5_a4()),
                new DoubleAttributeDto(B3_L5_A5, mainForm.getB3_l5_a5()),
                new DoubleAttributeDto(B3_L5_A6, mainForm.getB3_l5_a6()),
                new DoubleAttributeDto(B3_L6_A1, mainForm.getB3_l6_a1()),
                new DoubleAttributeDto(B3_L6_A2, mainForm.getB3_l6_a2()),
                new DoubleAttributeDto(B3_L6_A3, mainForm.getB3_l6_a3()),
                new DoubleAttributeDto(B3_L6_A4, mainForm.getB3_l6_a4()),
                new DoubleAttributeDto(B3_L7_A1, mainForm.getB3_l7_a1()),
                new DoubleAttributeDto(B3_L7_A2, mainForm.getB3_l7_a2()),
                new DoubleAttributeDto(B3_L7_A3, mainForm.getB3_l7_a3()),
                new DoubleAttributeDto(B3_L7_A4, mainForm.getB3_l7_a4()),
                new DoubleAttributeDto(B3_L7_A5, mainForm.getB3_l7_a5()),
                new DoubleAttributeDto(B3_L7_A6, mainForm.getB3_l7_a6()),
                new DoubleAttributeDto(B3_L8_A1, mainForm.getB3_l8_a1()),
                new DoubleAttributeDto(B3_L8_A2, mainForm.getB3_l8_a2()),
                new DoubleAttributeDto(B3_L8_A3, mainForm.getB3_l8_a3()),
                new DoubleAttributeDto(B3_L8_A4, mainForm.getB3_l8_a4()),
                new DoubleAttributeDto(B3_L9_A1, mainForm.getB3_l9_a1()),
                new DoubleAttributeDto(B3_L9_A2, mainForm.getB3_l9_a2()),
                new DoubleAttributeDto(B3_L9_A3, mainForm.getB3_l9_a3()),
                new DoubleAttributeDto(B3_L9_A4, mainForm.getB3_l9_a4()),
                new DoubleAttributeDto(B3_L10_A1, mainForm.getB3_l10_a1()),
                new DoubleAttributeDto(B3_L10_A2, mainForm.getB3_l10_a2()),
                new DoubleAttributeDto(B3_L10_A3, mainForm.getB3_l10_a3()),
                new DoubleAttributeDto(B3_L11_A1, mainForm.getB3_l11_a1()),
                new DoubleAttributeDto(B3_L11_A2, mainForm.getB3_l11_a2()),
                new DoubleAttributeDto(B3_L11_A3, mainForm.getB3_l11_a3()),
                new DoubleAttributeDto(B3_L11_A4, mainForm.getB3_l11_a4()),
                new DoubleAttributeDto(B3_L11_A5, mainForm.getB3_l11_a5()),
                new DoubleAttributeDto(B3_L11_A6, mainForm.getB3_l11_a6()),
                new DoubleAttributeDto(B3_L12_A1, mainForm.getB3_l12_a1()),
                new DoubleAttributeDto(B3_L12_A2, mainForm.getB3_l12_a2()),
                new DoubleAttributeDto(B3_L12_A3, mainForm.getB3_l12_a3()),
                new DoubleAttributeDto(B3_L12_A4, mainForm.getB3_l12_a4()),
                new DoubleAttributeDto(B3_L13_A1, mainForm.getB3_l13_a1()),
                new DoubleAttributeDto(B3_L13_A2, mainForm.getB3_l13_a2()),
                new DoubleAttributeDto(B3_L13_A3, mainForm.getB3_l13_a3()),
                new DoubleAttributeDto(B3_L13_A4, mainForm.getB3_l13_a4()),
                new DoubleAttributeDto(B3_L14_A1, mainForm.getB3_l14_a1()),
                new DoubleAttributeDto(B3_L14_A2, mainForm.getB3_l14_a2()),
                new DoubleAttributeDto(B3_L15_A1, mainForm.getB3_l15_a1()),
                new DoubleAttributeDto(B3_L15_A2, mainForm.getB3_l15_a2()),
                new DoubleAttributeDto(B3_L15_A3, mainForm.getB3_l15_a3()),
                new DoubleAttributeDto(B3_L15_A4, mainForm.getB3_l15_a4()),
                new DoubleAttributeDto(B3_L16_A1, mainForm.getB3_l16_a1()),
                new DoubleAttributeDto(B3_L16_A2, mainForm.getB3_l16_a2()),
                new DoubleAttributeDto(B3_L16_A3, mainForm.getB3_l16_a3()),
                new DoubleAttributeDto(B3_L16_A4, mainForm.getB3_l16_a4()),
                new DoubleAttributeDto(B3_L16_A5, mainForm.getB3_l16_a5()),
                new DoubleAttributeDto(B3_L16_A6, mainForm.getB3_l16_a6())
        );
    }

    public CropProductionMainForm toEntity(InstanceDto dto) {
        List<AttributeDto<?>> attributeDtos = dto.attributeDtos();
        return CropProductionMainForm.builder()
                .id(dto.id())
                .version((dto.version()))
                .date(extractData(attributeDtos, DATE_ATTR))
                .b1_l1_a1(extractData(attributeDtos,B1_L1_A1))
                .b1_l1_a2(extractData(attributeDtos,B1_L1_A2))
                .b1_l1_a3(extractData(attributeDtos,B1_L1_A3))
                .b1_l1_a4(extractData(attributeDtos,B1_L1_A4))
                .b1_l1_a5(extractData(attributeDtos,B1_L1_A5))
                .b1_l2_a1(extractData(attributeDtos,B1_L2_A1))
                .b1_l2_a2(extractData(attributeDtos,B1_L2_A2))
                .b1_l2_a3(extractData(attributeDtos,B1_L2_A3))
                .b1_l2_a4(extractData(attributeDtos,B1_L2_A4))
                .b1_l2_a5(extractData(attributeDtos,B1_L2_A5))
                .b1_l3_a1(extractData(attributeDtos,B1_L3_A1))
                .b1_l3_a2(extractData(attributeDtos,B1_L3_A2))
                .b1_l3_a3(extractData(attributeDtos,B1_L3_A3))
                .b1_l3_a4(extractData(attributeDtos,B1_L3_A4))
                .b1_l3_a5(extractData(attributeDtos,B1_L3_A5))
                .b1_l4_a1(extractData(attributeDtos,B1_L4_A1))
                .b1_l4_a2(extractData(attributeDtos,B1_L4_A2))
                .b1_l4_a3(extractData(attributeDtos,B1_L4_A3))
                .b1_l4_a4(extractData(attributeDtos,B1_L4_A4))
                .b1_l4_a5(extractData(attributeDtos,B1_L4_A5))
                .b1_l4_a6(extractData(attributeDtos,B1_L4_A6))
                .b1_l5_a1(extractData(attributeDtos,B1_L5_A1))
                .b1_l5_a2(extractData(attributeDtos,B1_L5_A2))
                .b1_l5_a3(extractData(attributeDtos,B1_L5_A3))
                .b1_l5_a4(extractData(attributeDtos,B1_L5_A4))
                .b1_l5_a5(extractData(attributeDtos,B1_L5_A5))
                .b1_l6_a1(extractData(attributeDtos,B1_L6_A1))
                .b1_l6_a2(extractData(attributeDtos,B1_L6_A2))
                .b1_l6_a3(extractData(attributeDtos,B1_L6_A3))
                .b1_l6_a4(extractData(attributeDtos,B1_L6_A4))
                .b1_l6_a5(extractData(attributeDtos,B1_L6_A5))
                .b1_l7_a1(extractData(attributeDtos,B1_L7_A1))
                .b1_l7_a2(extractData(attributeDtos,B1_L7_A2))
                .b1_l7_a3(extractData(attributeDtos,B1_L7_A3))
                .b1_l7_a4(extractData(attributeDtos,B1_L7_A4))
                .b1_l7_a5(extractData(attributeDtos,B1_L7_A5))
                .b1_l8_a1(extractData(attributeDtos,B1_L8_A1))
                .b1_l8_a2(extractData(attributeDtos,B1_L8_A2))
                .b1_l8_a3(extractData(attributeDtos,B1_L8_A3))
                .b1_l8_a4(extractData(attributeDtos,B1_L8_A4))
                .b1_l8_a5(extractData(attributeDtos,B1_L8_A5))
                .b1_l9_a1(extractData(attributeDtos,B1_L9_A1))
                .b1_l9_a2(extractData(attributeDtos,B1_L9_A2))
                .b1_l10_a1(extractData(attributeDtos,B1_L10_A1))
                .b1_l10_a2(extractData(attributeDtos,B1_L10_A2))
                .b1_l11_a1(extractData(attributeDtos,B1_L11_A1))
                .b1_l11_a2(extractData(attributeDtos,B1_L11_A2))
                .b1_l12_a1(extractData(attributeDtos,B1_L12_A1))
                .b1_l12_a2(extractData(attributeDtos,B1_L12_A2))
                .b1_l13_a1(extractData(attributeDtos,B1_L13_A1))
                .b1_l13_a2(extractData(attributeDtos,B1_L13_A2))
                .b1_l14_a1(extractData(attributeDtos,B1_L14_A1))
                .b1_l14_a2(extractData(attributeDtos,B1_L14_A2))
                .b1_l15_a1(extractData(attributeDtos,B1_L15_A1))
                .b1_l15_a2(extractData(attributeDtos,B1_L15_A2))
                .b1_l16_a1(extractData(attributeDtos,B1_L16_A1))
                .b1_l16_a2(extractData(attributeDtos,B1_L16_A2))
                .b1_l16_a3(extractData(attributeDtos,B1_L16_A3))
                .b1_l16_a4(extractData(attributeDtos,B1_L16_A4))
                .b1_l16_a5(extractData(attributeDtos,B1_L16_A5))
                .build();
    }
}

package su.petrosoft.apk_ack_integration.util;

import lombok.extern.slf4j.Slf4j;
import su.petrosoft.apk_ack_integration.model.CropProductionMainForm;
import su.petrosoft.apk_ack_integration.model.OperationalReport;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CropProductionUtil {
    public static final long TEMPLATE_ID = 11376;

    public static final long DATE_ATTR = 3743;

    public static final long B1_L1_A1 = 3585;
    public static final long B1_L1_A2 = 3586;
    public static final long B1_L1_A3 = 3587;
    public static final long B1_L1_A4 = 3588;
    public static final long B1_L1_A5 = 3589;

    public static final long B1_L2_A1 = 3590;
    public static final long B1_L2_A2 = 3591;
    public static final long B1_L2_A3 = 3592;
    public static final long B1_L2_A4 = 3593;
    public static final long B1_L2_A5 = 3594;

    public static final long B1_L3_A1 = 3595;
    public static final long B1_L3_A2 = 3596;
    public static final long B1_L3_A3 = 3597;
    public static final long B1_L3_A4 = 3598;
    public static final long B1_L3_A5 = 3599;

    public static final long B1_L4_A1 = 3600;
    public static final long B1_L4_A2 = 3601;
    public static final long B1_L4_A3 = 3602;
    public static final long B1_L4_A4 = 3603;
    public static final long B1_L4_A5 = 3604;
    public static final long B1_L4_A6 = 3605;

    public static final long B1_L5_A1 = 3606;
    public static final long B1_L5_A2 = 3607;
    public static final long B1_L5_A3 = 3608;
    public static final long B1_L5_A4 = 3609;
    public static final long B1_L5_A5 = 3610;

    public static final long B1_L6_A1 = 3611;
    public static final long B1_L6_A2 = 3612;
    public static final long B1_L6_A3 = 3613;
    public static final long B1_L6_A4 = 3614;
    public static final long B1_L6_A5 = 3615;

    public static final long B1_L7_A1 = 3616;
    public static final long B1_L7_A2 = 3617;
    public static final long B1_L7_A3 = 3618;
    public static final long B1_L7_A4 = 3619;
    public static final long B1_L7_A5 = 3620;

    public static final long B1_L8_A1 = 3621;
    public static final long B1_L8_A2 = 3622;
    public static final long B1_L8_A3 = 3623;
    public static final long B1_L8_A4 = 3624;
    public static final long B1_L8_A5 = 3625;

    public static final long B1_L9_A1 = 3626;
    public static final long B1_L9_A2 = 3627;

    public static final long B1_L10_A1 = 3628;
    public static final long B1_L10_A2 = 3629;

    public static final long B1_L11_A1 = 3630;
    public static final long B1_L11_A2 = 3631;

    public static final long B1_L12_A1 = 3632;
    public static final long B1_L12_A2 = 3633;

    public static final long B1_L13_A1 = 3634;
    public static final long B1_L13_A2 = 3635;

    public static final long B1_L14_A1 = 3636;
    public static final long B1_L14_A2 = 3637;

    public static final long B1_L15_A1 = 3638;
    public static final long B1_L15_A2 = 3639;

    public static final long B1_L16_A1 = 3964;
    public static final long B1_L16_A2 = 3965;
    public static final long B1_L16_A3 = 3966;
    public static final long B1_L16_A4 = 3967;
    public static final long B1_L16_A5 = 3968;

    public static void fillSowingCampaignFields(CropProductionMainForm mainForm, List<OperationalReport> reports) {
        log.info("Crop Production Main Form [{}] received to filled its Sowing Campaign fields. " +
                        "Associated reports count: [{}]",
                mainForm.getId(), reports.size());
        log.debug("Crop Production Main Form: [{}]", mainForm);
        Map<String, BigDecimal> summedValues = reports.stream()
                .peek(r -> log.debug(r.toString()))
                .map(OperationalReport::getReportValues)
                .filter(map -> !map.isEmpty())
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        BigDecimal::add
                ));
        log.debug("All reports values summed: [{}]", summedValues);

        mainForm.setB1_l1_a1(getDivision(summedValues, "value36", 1000, 3));
        mainForm.setB1_l1_a2(getPercent(summedValues, "value36", "value34", 0));
        mainForm.setB1_l1_a3(getDivision(summedValues, "value34", 1000, 1));
        mainForm.setB1_l1_a4(getPercent(summedValues, "value36", "value35", 0));
        mainForm.setB1_l1_a5(getDivision(summedValues, "value35", 1000, 1));

        mainForm.setB1_l2_a1(getDivision(summedValues, "value40", 1000, 3));
        mainForm.setB1_l2_a2(getPercent(summedValues, "value40", "value38", 0));
        mainForm.setB1_l2_a3(getDivision(summedValues, "value38", 1000, 1));
        mainForm.setB1_l2_a4(getPercent(summedValues, "value40", "value39", 0));
        mainForm.setB1_l2_a5(getDivision(summedValues, "value39", 1000, 1));

        mainForm.setB1_l3_a1(getDivision(summedValues, "value60", 1000, 3));
        mainForm.setB1_l3_a2(getPercent(summedValues, "value60", "value58", 0));
        mainForm.setB1_l3_a3(getDivision(summedValues, "value58", 1000, 1));
        mainForm.setB1_l3_a4(getPercent(summedValues, "value60", "value59", 0));
        mainForm.setB1_l3_a5(getDivision(summedValues, "value59", 1000, 1));

        mainForm.setB1_l4_a1(getDivision(summedValues, "value88", 1000, 3));
        mainForm.setB1_l4_a2(getPercent(summedValues, "value88", "value86", 0));
        mainForm.setB1_l4_a3(getDivision(summedValues, "value86", 1000, 1));
        mainForm.setB1_l4_a4(getPercent(summedValues, "value88", "value87", 0));
        mainForm.setB1_l4_a5(getDivision(summedValues, "value87", 1000, 1));
        mainForm.setB1_l4_a6(summedValues.get("value92"));

        mainForm.setB1_l5_a1(getDivision(summedValues, "value96", 1000, 3));
        mainForm.setB1_l5_a2(getPercent(summedValues, "value96", "value94", 0));
        mainForm.setB1_l5_a3(getDivision(summedValues, "value94", 1000, 1));
        mainForm.setB1_l5_a4(getPercent(summedValues, "value96", "value95", 0));
        mainForm.setB1_l5_a5(getDivision(summedValues, "value95", 1000, 1));

        mainForm.setB1_l6_a1(summedValues.get("value64"));
        mainForm.setB1_l6_a2(getPercent(summedValues, "value64", "value62", 0));
        mainForm.setB1_l6_a3(summedValues.get("value62"));
        mainForm.setB1_l6_a4(getPercent(summedValues, "value64", "value63", 0));
        mainForm.setB1_l6_a5(summedValues.get("value63"));

        mainForm.setB1_l7_a1(summedValues.get("value68"));
        mainForm.setB1_l7_a2(getPercent(summedValues, "value68", "value66", 0));
        mainForm.setB1_l7_a3(summedValues.get("value66"));
        mainForm.setB1_l7_a4(getPercent(summedValues, "value68", "value67", 0));
        mainForm.setB1_l7_a5(summedValues.get("value67"));

        mainForm.setB1_l8_a1(getDivision(summedValues, "value100", 1000, 3));
        mainForm.setB1_l8_a2(getPercent(summedValues, "value100", "value98", 0));
        mainForm.setB1_l8_a3(getDivision(summedValues, "value98", 1000, 1));
        mainForm.setB1_l8_a4(getPercent(summedValues, "value100", "value99", 0));
        mainForm.setB1_l8_a5(getDivision(summedValues, "value99", 1000, 1));

        mainForm.setB1_l16_a1(summedValues.get("value659"));
        mainForm.setB1_l16_a2(getPercent(summedValues, "value659", "value658", 0));
        mainForm.setB1_l16_a3(summedValues.get("value658"));
        mainForm.setB1_l16_a4(getPercent(summedValues, "value659", "value660", 0));
        mainForm.setB1_l16_a5(summedValues.get("value660"));

        mainForm.setB1_l9_a1(getDivision(summedValues, "value120", 1000, 1));
        mainForm.setB1_l9_a2(getDivision(summedValues, "value636", 1000, 1));

        mainForm.setB1_l10_a1(getDivision(summedValues, "value124", 1000, 1));
        mainForm.setB1_l10_a2(getDivision(summedValues, "value638", 1000, 1));

        mainForm.setB1_l11_a1(getDivision(summedValues, "value104", 1000, 1));
        mainForm.setB1_l11_a2(getDivision(summedValues, "value628", 1000, 1));

        mainForm.setB1_l12_a1(getDivision(summedValues, "value116", 1000, 1));
        mainForm.setB1_l12_a2(getDivision(summedValues, "value634", 1000, 1));

        mainForm.setB1_l13_a1(summedValues.get("value112"));
        mainForm.setB1_l13_a2(summedValues.get("value632"));

        mainForm.setB1_l14_a1(summedValues.get("value108"));
        mainForm.setB1_l14_a2(summedValues.get("value630"));

        mainForm.setB1_l15_a1(getDivision(summedValues, "value132", 1000, 1));
        mainForm.setB1_l15_a2(getDivision(summedValues, "value640", 1000, 1));

    }

    private static BigDecimal getPercent(
            Map<String, BigDecimal> summedValues,
            String numeratorField,
            String denominatorField,
            int scale) {
        BigDecimal numerator = summedValues.get(numeratorField);
        BigDecimal denominator = summedValues.get(denominatorField);

        if (numerator == null || denominator == null ||
                denominator.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.setScale(scale, RoundingMode.HALF_UP);
        }

        return numerator
                .divide(denominator, scale, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    private static BigDecimal getDivision(Map<String, BigDecimal> summedValues, String field, int divisor, int scale) {
        BigDecimal value = summedValues.get(field);

        if (value == null || divisor == 0) {
            return BigDecimal.ZERO.setScale(scale, RoundingMode.HALF_UP);
        }

        return value.divide(BigDecimal.valueOf(divisor), scale, RoundingMode.HALF_UP);
    }

}

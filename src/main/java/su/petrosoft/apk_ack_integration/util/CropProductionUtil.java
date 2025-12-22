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

    public static final long B2_L1_A1 = 3641;
    public static final long B2_L1_A2 = 3642;
    public static final long B2_L1_A3 = 3643;
    public static final long B2_L1_A4 = 3644;
    public static final long B2_L1_A5 = 3645;

    public static final long B2_L2_A1 = 3646;
    public static final long B2_L2_A2 = 3647;

    public static final long B2_L3_A1 = 3648;
    public static final long B2_L3_A2 = 3649;

    public static final long B2_L4_A1 = 3650;
    public static final long B2_L4_A2 = 3651;
    public static final long B2_L4_A3 = 3652;
    public static final long B2_L4_A4 = 3653;
    public static final long B2_L4_A5 = 3654;

    public static final long B2_L5_A1 = 3655;
    public static final long B2_L5_A2 = 3656;
    public static final long B2_L5_A3 = 3657;
    public static final long B2_L5_A4 = 3658;
    public static final long B2_L5_A5 = 3659;

    public static final long B2_L6_A1 = 3660;
    public static final long B2_L6_A2 = 3661;
    public static final long B2_L6_A3 = 3662;
    public static final long B2_L6_A4 = 3663;
    public static final long B2_L6_A5 = 3664;

    public static final long B2_L7_A1 = 3665;
    public static final long B2_L7_A2 = 3666;
    public static final long B2_L7_A3 = 3667;
    public static final long B2_L7_A4 = 3668;
    public static final long B2_L7_A5 = 3669;

    public static final long B2_L8_A1 = 3670;
    public static final long B2_L8_A2 = 3671;
    public static final long B2_L8_A3 = 3672;
    public static final long B2_L8_A4 = 3673;
    public static final long B2_L8_A5 = 3674;

    public static final long B2_L9_A1 = 3675;
    public static final long B2_L9_A2 = 3676;
    public static final long B2_L9_A3 = 3677;
    public static final long B2_L9_A4 = 3678;
    public static final long B2_L9_A5 = 3679;
    public static final long B2_L9_A6 = 3680;
    public static final long B2_L9_A7 = 3847;

    public static final long B3_L1_A1 = 3681;
    public static final long B3_L1_A2 = 3682;
    public static final long B3_L1_A3 = 3683;
    public static final long B3_L1_A4 = 3684;
    public static final long B3_L1_A5 = 3685;

    public static final long B3_L2_A1 = 3686;
    public static final long B3_L2_A2 = 3687;
    public static final long B3_L2_A3 = 3688;
    public static final long B3_L2_A4 = 3689;

    public static final long B3_L3_A1 = 3690;
    public static final long B3_L3_A2 = 3691;
    public static final long B3_L3_A3 = 3692;

    public static final long B3_L4_A1 = 3693;
    public static final long B3_L4_A2 = 3694;
    public static final long B3_L4_A3 = 3695;
    public static final long B3_L4_A4 = 3696;

    public static final long B3_L5_A1 = 3697;
    public static final long B3_L5_A2 = 3698;
    public static final long B3_L5_A3 = 3699;
    public static final long B3_L5_A4 = 3700;
    public static final long B3_L5_A5 = 3701;
    public static final long B3_L5_A6 = 3702;

    public static final long B3_L6_A1 = 3703;
    public static final long B3_L6_A2 = 3704;
    public static final long B3_L6_A3 = 3705;
    public static final long B3_L6_A4 = 3706;

    public static final long B3_L7_A1 = 3707;
    public static final long B3_L7_A2 = 3708;
    public static final long B3_L7_A3 = 3709;
    public static final long B3_L7_A4 = 3710;
    public static final long B3_L7_A5 = 3711;
    public static final long B3_L7_A6 = 3712;

    public static final long B3_L8_A1 = 3713;
    public static final long B3_L8_A2 = 3714;
    public static final long B3_L8_A3 = 3715;
    public static final long B3_L8_A4 = 3716;

    public static final long B3_L9_A1 = 3717;
    public static final long B3_L9_A2 = 3718;
    public static final long B3_L9_A3 = 3719;
    public static final long B3_L9_A4 = 3720;

    public static final long B3_L10_A1 = 3721;
    public static final long B3_L10_A2 = 3722;
    public static final long B3_L10_A3 = 3723;

    public static final long B3_L11_A1 = 3724;
    public static final long B3_L11_A2 = 3725;
    public static final long B3_L11_A3 = 3726;
    public static final long B3_L11_A4 = 3727;
    public static final long B3_L11_A5 = 3728;
    public static final long B3_L11_A6 = 3729;

    public static final long B3_L12_A1 = 3730;
    public static final long B3_L12_A2 = 3731;
    public static final long B3_L12_A3 = 3732;
    public static final long B3_L12_A4 = 3733;

    public static final long B3_L13_A1 = 3734;
    public static final long B3_L13_A2 = 3735;
    public static final long B3_L13_A3 = 3736;
    public static final long B3_L13_A4 = 3737;

    public static final long B3_L14_A1 = 3738;
    public static final long B3_L14_A2 = 3739;

    public static final long B3_L15_A1 = 4330;
    public static final long B3_L15_A2 = 4331;
    public static final long B3_L15_A3 = 4332;
    public static final long B3_L15_A4 = 4333;

    public static final long B3_L16_A1 = 4334;
    public static final long B3_L16_A2 = 4335;
    public static final long B3_L16_A3 = 4336;
    public static final long B3_L16_A4 = 4337;
    public static final long B3_L16_A5 = 4338;
    public static final long B3_L16_A6 = 4339;


    public static void fillSowingFields(CropProductionMainForm mainForm, List<OperationalReport> reports) {
        log.info("Crop Production Main Form [{}] updating by filling its Sowing' fields from Reports. " +
                "Associated reports count: [{}]", mainForm.getId(), reports.size());

        Map<String, BigDecimal> summedValues = getSummedValueMap(reports);

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
        log.debug("Updated Main Form after Sowing Campaign filling: [{}]", mainForm);
    }

    public static void fillFodderFields(CropProductionMainForm mainForm, List<OperationalReport> reports) {
        log.info("Crop Production Main Form [{}] updating by filling its Fodder' fields from Reports. " +
                "Associated reports count: [{}]", mainForm.getId(), reports.size());

        Map<String, BigDecimal> summedValueMap = getSummedValueMap(reports);

        mainForm.setB2_l1_a1(getDivision(summedValueMap, "winterCrops", 1000, 1));
        mainForm.setB2_l1_a2(getPercent(summedValueMap, "winterCrops", "totalArea", 0));
        mainForm.setB2_l1_a3(getDivision(summedValueMap, "totalArea", 1000, 0));
        mainForm.setB2_l1_a4(getPercent(summedValueMap, "winterCrops", "x", 1));
        mainForm.setB2_l1_a5(getDivision(summedValueMap, "x", 1000, 1));

        mainForm.setB2_l2_a1(summedValueMap.get("value7").setScale(0, RoundingMode.HALF_UP));
        mainForm.setB2_l2_a2(null);

        mainForm.setB2_l3_a1(summedValueMap.get("value10").setScale(0, RoundingMode.HALF_UP));
        mainForm.setB2_l3_a2(null);

        mainForm.setB2_l4_a1(getDivision(summedValueMap, "value37", 1000, 1));
        mainForm.setB2_l4_a2(getPercent(summedValueMap, "value37", "value36", 0));
        mainForm.setB2_l4_a3(getDivision(summedValueMap, "value36", 1000, 1));
        mainForm.setB2_l4_a4(getPercent(summedValueMap, "value37", "value35", 0));
        mainForm.setB2_l4_a5(getDivision(summedValueMap, "x", 1000, 1));

        mainForm.setB2_l5_a1(getDivision(summedValueMap, "value41", 1000, 1));
        mainForm.setB2_l5_a2(getPercent(summedValueMap, "value41", "value40", 0));
        mainForm.setB2_l5_a3(getDivision(summedValueMap, "value40", 1000, 1));
        mainForm.setB2_l5_a4(getPercent(summedValueMap, "value41", "value39", 0));
        mainForm.setB2_l5_a5(getDivision(summedValueMap, "value39", 1000, 1));

        mainForm.setB2_l6_a1(getDivision(summedValueMap, "value45", 1000, 1));
        mainForm.setB2_l6_a2(getPercent(summedValueMap, "value45", "value44", 0));
        mainForm.setB2_l6_a3(getDivision(summedValueMap, "value44", 1000, 1));
        mainForm.setB2_l6_a4(getPercent(summedValueMap, "value45", "value43", 0));
        mainForm.setB2_l6_a5(getDivision(summedValueMap, "value43", 1000, 1));

        mainForm.setB2_l7_a1(getDivision(summedValueMap, "value65", 1000, 1));
        mainForm.setB2_l7_a2(getPercent(summedValueMap, "value65", "value64", 0));
        mainForm.setB2_l7_a3(getDivision(summedValueMap, "value64", 1000, 1));
        mainForm.setB2_l7_a4(getPercent(summedValueMap, "value65", "value63", 0));
        mainForm.setB2_l7_a5(getDivision(summedValueMap, "value63", 1000, 1));

        mainForm.setB2_l8_a1(getDivision(summedValueMap, "value69", 1000, 1));
        mainForm.setB2_l8_a2(getPercent(summedValueMap, "value69", "value68", 0));
        mainForm.setB2_l8_a3(getDivision(summedValueMap, "value68", 1000, 1));
        mainForm.setB2_l8_a4(getPercent(summedValueMap, "value69", "value67", 0));
        mainForm.setB2_l8_a5(getDivision(summedValueMap, "value67", 1000, 1));

        mainForm.setB2_l9_a1(getDivision(summedValueMap, "value73", 1000, 1));
        mainForm.setB2_l9_a2(summedValueMap.get("value77").setScale(1, RoundingMode.HALF_UP));
        mainForm.setB2_l9_a3(summedValueMap.get("value75").setScale(1, RoundingMode.HALF_UP));
        mainForm.setB2_l9_a4(getPercent(summedValueMap, "value77", "value72", 0));
        mainForm.setB2_l9_a5(summedValueMap.get("value72").setScale(1, RoundingMode.HALF_UP));
    }

    public static void fillHarvestingFields(CropProductionMainForm mainForm, List<OperationalReport> reports) {
        log.info("Crop Production Main Form [{}] updating by filling its Harvesting' fields from Reports. " +
                "Associated reports count: [{}]", mainForm.getId(), reports.size());

        Map<String, BigDecimal> summedValues = getSummedValueMap(reports);

        mainForm.setB3_l1_a1(getDivision(summedValues, "value3", 1000, 1));
        mainForm.setB3_l1_a2(getDivision(summedValues, "value10", 1000, 1));
        mainForm.setB3_l1_a3(getFraction(
                summedValues.get("value3").add(summedValues.get("value10")).add(summedValues.get("value11")),
                summedValues.get("value2"), 100, 0));
        mainForm.setB3_l1_a4(getDivision(summedValues, "value2", 1000, 1));
        mainForm.setB3_l1_a5(getDivision(summedValues, "value1", 1000, 1));

        mainForm.setB3_l2_a1(getDivision(summedValues, "value15", 1000, 1));
        mainForm.setB3_l2_a2(getDivision(summedValues, "value108", 1000, 1));
        mainForm.setB3_l2_a3(getFraction(summedValues.get("value15"), summedValues.get("value3"), 10, 1));
        mainForm.setB3_l2_a4(getFraction(summedValues.get("value108"), summedValues.get("value206"), 10, 1));

        mainForm.setB3_l3_a1(getDivision(summedValues, "value17", 1000, 1));
        mainForm.setB3_l3_a2(getPercent(summedValues, "value17", "value16", 0));
        mainForm.setB3_l3_a3(getDivision(summedValues, "value16", 1000, 1));

        mainForm.setB3_l4_a1(summedValues.get("value41").setScale(1, RoundingMode.HALF_UP));
        mainForm.setB3_l4_a2(getFraction(
                summedValues.get("value41").add(summedValues.get("value42")),
                summedValues.get("value40"), 100, 0));
        mainForm.setB3_l4_a3(summedValues.get("value40").setScale(1, RoundingMode.HALF_UP));
        mainForm.setB3_l4_a4(summedValues.get("value39").setScale(1, RoundingMode.HALF_UP));

        mainForm.setB3_l5_a1(getDivision(summedValues, "value48", 1000, 1));
        mainForm.setB3_l5_a2(getPercent(summedValues, "value48", "value47", 0));
        mainForm.setB3_l5_a3(getDivision(summedValues, "value47", 1000, 1));
        mainForm.setB3_l5_a4(getDivision(summedValues, "value46", 1000, 1));
        mainForm.setB3_l5_a5(getFraction(summedValues.get("value48"), summedValues.get("value41"), 10, 1));
        mainForm.setB3_l5_a6(getFraction(summedValues.get("value46"), summedValues.get("value39"), 10, 1));

        mainForm.setB3_l6_a1(summedValues.get("value57").setScale(1, RoundingMode.HALF_UP));
        mainForm.setB3_l6_a2(getFraction(
                summedValues.get("value57").add(summedValues.get("value62")),
                summedValues.get("value56"), 100, 0));
        mainForm.setB3_l6_a3(summedValues.get("value56").setScale(1, RoundingMode.HALF_UP));
        mainForm.setB3_l6_a4(summedValues.get("value55").setScale(1, RoundingMode.HALF_UP));

        mainForm.setB3_l7_a1(getDivision(summedValues, "value68", 1000, 1));
        mainForm.setB3_l7_a2(getPercent(summedValues, "value68", "value67", 0));
        mainForm.setB3_l7_a3(getDivision(summedValues, "value67", 1000, 1));
        mainForm.setB3_l7_a4(getDivision(summedValues, "value66", 1000, 1));
        mainForm.setB3_l7_a5(getFraction(summedValues.get("value67"), summedValues.get("value56"), 10, 1));
        mainForm.setB3_l7_a6(getFraction(summedValues.get("value66"), summedValues.get("value55"), 10, 1));

        mainForm.setB3_l8_a1(summedValues.get("value31").setScale(1, RoundingMode.HALF_UP));
        mainForm.setB3_l8_a2(getPercent(summedValues, "value31", "value104", 0));
        mainForm.setB3_l8_a3(summedValues.get("value104").setScale(1, RoundingMode.HALF_UP));
        mainForm.setB3_l8_a4(summedValues.get("value103").setScale(1, RoundingMode.HALF_UP));

        mainForm.setB3_l9_a1(getDivision(summedValues, "value32", 1000, 1));
        mainForm.setB3_l9_a2(getDivision(summedValues, "value109", 1000, 1));
        mainForm.setB3_l9_a3(getFraction(summedValues.get("value32"), summedValues.get("value31"), 10, 1));
        mainForm.setB3_l9_a4(getFraction(summedValues.get("value109"), summedValues.get("value103"), 10, 1));

        mainForm.setB3_l10_a1(getDivision(summedValues, "value33", 1000, 1));
        mainForm.setB3_l10_a2(getPercent(summedValues, "value33", "value111", 0));
        mainForm.setB3_l10_a3(getDivision(summedValues, "value111", 1000, 1));

        mainForm.setB3_l11_a1(summedValues.get("value36").setScale(1, RoundingMode.HALF_UP));
        mainForm.setB3_l11_a2(summedValues.get("value116").setScale(1, RoundingMode.HALF_UP));
        mainForm.setB3_l11_a3(summedValues.get("value37").setScale(1, RoundingMode.HALF_UP));
        mainForm.setB3_l11_a4(summedValues.get("value117").setScale(1, RoundingMode.HALF_UP));
        mainForm.setB3_l11_a5(getFraction(summedValues.get("value37"), summedValues.get("value36"), 10, 1));
        mainForm.setB3_l11_a6(getFraction(summedValues.get("value117"), summedValues.get("value116"), 10, 1));

        mainForm.setB3_l12_a1(summedValues.get("value88").setScale(1, RoundingMode.HALF_UP));
        mainForm.setB3_l12_a2(getPercent(summedValues, "value88", "value197", 0));
        mainForm.setB3_l12_a3(summedValues.get("value197").setScale(1, RoundingMode.HALF_UP));
        mainForm.setB3_l12_a4(summedValues.get("value213").setScale(1, RoundingMode.HALF_UP));

        mainForm.setB3_l13_a1(summedValues.get("value84").add(summedValues.get("value85"))
                .add(summedValues.get("value86")).add(summedValues.get("value85")).setScale(1, RoundingMode.HALF_UP));
        mainForm.setB3_l13_a3(summedValues.get("value193").add(summedValues.get("value194"))
                .add(summedValues.get("value195")).add(summedValues.get("value196")).setScale(1, RoundingMode.HALF_UP));
        mainForm.setB3_l13_a2(getDivision(mainForm.getB3_l13_a1(), mainForm.getB3_l13_a3(), 1));
        mainForm.setB3_l13_a4(summedValues.get("value209").add(summedValues.get("value210"))
                .add(summedValues.get("value211")).add(summedValues.get("value212")).setScale(1, RoundingMode.HALF_UP));

        mainForm.setB3_l14_a1(summedValues.get("value90").setScale(1, RoundingMode.HALF_UP));
        mainForm.setB3_l14_a2(summedValues.get("value214").setScale(1, RoundingMode.HALF_UP));

        mainForm.setB3_l15_a1(summedValues.get("value119").setScale(1, RoundingMode.HALF_UP));
        mainForm.setB3_l15_a2(getFraction(
                summedValues.get("value119").add(summedValues.get("value123")),
                summedValues.get("value120"), 100, 0));
        mainForm.setB3_l15_a3(summedValues.get("value120").setScale(1, RoundingMode.HALF_UP));
        mainForm.setB3_l15_a4(summedValues.get("value118").setScale(1, RoundingMode.HALF_UP));

        mainForm.setB3_l16_a1(getDivision(summedValues, "value125", 1000, 1));
        mainForm.setB3_l16_a2(getPercent(summedValues, "value125", "value126", 0));
        mainForm.setB3_l16_a3(getDivision(summedValues, "value126", 1000, 1));
        mainForm.setB3_l16_a4(getDivision(summedValues, "value124", 1000, 1));
        mainForm.setB3_l16_a5(getFraction(summedValues.get("value125"), summedValues.get("value119"), 10, 1));
        mainForm.setB3_l16_a6(getFraction(summedValues.get("value124"), summedValues.get("value118"), 10, 1));
    }

    private static Map<String, BigDecimal> getSummedValueMap(List<OperationalReport> reports) {
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
        return summedValues;
    }

    private static BigDecimal getPercent(
            Map<String, BigDecimal> summedValues,
            String numeratorField,
            String denominatorField,
            int scale) {
        return getFraction(summedValues.get(numeratorField), summedValues.get(denominatorField), 100, scale);
    }

    private static BigDecimal getFraction(
            BigDecimal numerator,
            BigDecimal denominator,
            int multiplier,
            int scale) {

        if (numerator == null
                || denominator == null
                || multiplier == 0
                || denominator.compareTo(BigDecimal.ZERO) == 0
        ) {
            return BigDecimal.ZERO.setScale(scale, RoundingMode.HALF_UP);
        }

        return numerator
                .multiply(BigDecimal.valueOf(multiplier))
                .divide(denominator, scale, RoundingMode.HALF_UP);
    }

    private static BigDecimal getDivision(Map<String, BigDecimal> summedValues, String field, int divisor, int scale) {
        BigDecimal value = summedValues.get(field);

        if (value == null || divisor == 0) {
            return BigDecimal.ZERO.setScale(scale, RoundingMode.HALF_UP);
        }

        return value.divide(BigDecimal.valueOf(divisor), scale, RoundingMode.HALF_UP);
    }

    private static BigDecimal getDivision(BigDecimal divisible, BigDecimal divisor, int scale) {

        if (divisible == null || divisor == null || divisor.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.setScale(scale, RoundingMode.HALF_UP);
        }

        return divisible.divide(divisor, scale, RoundingMode.HALF_UP);
    }
}

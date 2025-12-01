package su.petrosoft.apk_ack_integration.util;

import su.petrosoft.apk_ack_integration.model.CropProductionMainForm;
import su.petrosoft.apk_ack_integration.model.OperationalReport;
import su.petrosoft.apk_ack_integration.model.dto.plicante.ReportFieldDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class CropProductionUtil {
    public static final long TEMPLATE_ID = 11376;

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

    public static void fillSowingCampaign(CropProductionMainForm updatedMainForm, List<OperationalReport> reports) {
        Map<String, BigDecimal> summedValues = reports.stream()
                .map(OperationalReport::getReportFile)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toMap(
                        ReportFieldDto::field,
                        dto -> dto.value() != null ? dto.value() : BigDecimal.ZERO,
                        BigDecimal::add
                ));

    }

}

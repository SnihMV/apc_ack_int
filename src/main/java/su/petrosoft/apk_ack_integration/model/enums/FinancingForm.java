package su.petrosoft.apk_ack_integration.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.function.BiPredicate;

@Getter
@AllArgsConstructor
public enum FinancingForm {
    OB("ОБ (областной бюджет)", (ob, fb) -> ob.compareTo(BigDecimal.ONE) == 0 && fb.compareTo(BigDecimal.ZERO) == 0),

    OFB("ОФБ (областной + федеральный бюджет)", (ob, fb) -> ob.compareTo(BigDecimal.ZERO) > 0 && ob.compareTo(BigDecimal.ONE) < 0
            && fb.compareTo(BigDecimal.ZERO) > 0 && fb.compareTo(BigDecimal.ONE) < 0),
    OVER("Сверх (областной + федеральный бюджет + дополнительные областные деньги)", (ob, fb) -> ob.compareTo(BigDecimal.ZERO) == 0 && fb.compareTo(BigDecimal.ZERO) == 0);

    private String name;
    private BiPredicate<BigDecimal, BigDecimal> matcher;

    public static FinancingForm define(BigDecimal obCoeff, BigDecimal fbCoeff) {

        return Arrays.stream(values())
                .filter(form -> form.matcher.test(obCoeff, fbCoeff))
                .findFirst()
                .orElse(OFB);
    }
}

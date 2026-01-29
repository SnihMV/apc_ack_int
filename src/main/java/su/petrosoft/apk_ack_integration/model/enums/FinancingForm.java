package su.petrosoft.apk_ack_integration.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import su.petrosoft.apk_ack_integration.exception.DictionaryException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.function.BiPredicate;

@Getter
@AllArgsConstructor
public enum FinancingForm {
    OB("ОБ", (ob, fb) -> ob.compareTo(BigDecimal.ONE) == 0 && fb.compareTo(BigDecimal.ZERO) == 0),

    OFB("ОФБ", (ob, fb) -> ob.compareTo(BigDecimal.ZERO) > 0 && ob.compareTo(BigDecimal.ONE) < 0
            && fb.compareTo(BigDecimal.ZERO) > 0 && fb.compareTo(BigDecimal.ONE) < 0),
    OVER("СВЕРХ", (ob, fb) -> ob.compareTo(BigDecimal.ZERO) == 0 && fb.compareTo(BigDecimal.ZERO) == 0);

    private String code;
    private BiPredicate<BigDecimal, BigDecimal> matcher;

    public static FinancingForm define(BigDecimal obCoeff, BigDecimal fbCoeff) {

        return Arrays.stream(values())
                .filter(form -> form.matcher.test(obCoeff, fbCoeff))
                .findFirst()
                .orElse(OFB);
    }

    public static FinancingForm define(String code) {
        return Arrays.stream(values())
                .filter(ff -> ff.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new DictionaryException("Не найдена форма финансирования с кодом [%s]".formatted(code)));
    }
}

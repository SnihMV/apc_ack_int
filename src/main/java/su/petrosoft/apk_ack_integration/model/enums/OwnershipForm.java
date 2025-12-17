package su.petrosoft.apk_ack_integration.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum OwnershipForm {
    GOS("ГОС", "244"),
    NEGOS("НЕГОС", "245"),
    IP("ИП", "246"),
    ALL("ВСЕ", "default");

    private String name;
    private String kosgu;

    public static OwnershipForm define(String kosgu) {
        return Arrays.stream(values())
                .filter(form -> form.getKosgu().equalsIgnoreCase(kosgu))
                .findFirst()
                .orElse(ALL);
    }
}

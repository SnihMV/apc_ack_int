package su.petrosoft.apk_ack_integration.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import su.petrosoft.apk_ack_integration.exception.DictionaryException;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum OwnershipForm {
    GOS("ГОС", "244"),
    NEGOS("НЕГОС", "245"),
    IP("ИП", "246"),
    ALL("ВСЕ", "default");

    private String code;
    private String kosgu;

    public static OwnershipForm ownFormByKosgu(String kosgu) {
        return Arrays.stream(values())
                .filter(form -> form.getKosgu().equalsIgnoreCase(kosgu))
                .findFirst()
                .orElse(ALL);
    }

    public static OwnershipForm ownFormByCode(String code) {
        return Arrays.stream(values())
                .filter(ff -> ff.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new DictionaryException("Не найдена форма собственности с кодом [%s]".formatted(code)));
    }
}

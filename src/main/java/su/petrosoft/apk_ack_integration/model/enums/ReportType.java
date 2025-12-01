package su.petrosoft.apk_ack_integration.model.enums;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportType {
    FORM_1("Форма 1", 630),
    FORM_2("Форма 2", 631),
    FORM_3("Форма 3", 632),
    FORM_4("Форма 4", 633),
    FORM_5("Форма 5", 634);

    private final String title;
    private final long id;

    public static ReportType getById(long id) {
        return Arrays.stream(values())
            .filter(type -> type.id == id)
            .findFirst()
            .orElseThrow();
    }
}

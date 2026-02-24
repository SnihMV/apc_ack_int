package su.petrosoft.apk_ack_integration.model.enums;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportType {
    FORM_1("Форма 1", 630, "/templates/summaryReportForm1.xlsx"),
    FORM_2("Форма 2", 631, "/templates/summaryReportForm2.xlsx"),
    FORM_3("Форма 3", 632, "/templates/summaryReportForm3.xlsx");

    private final String title;
    private final long id;
    private final String templatePath;

    public static ReportType getById(long id) {
        return Arrays.stream(values())
            .filter(type -> type.id == id)
            .findFirst()
            .orElseThrow();
    }
}

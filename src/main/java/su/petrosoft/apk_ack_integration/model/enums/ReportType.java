package su.petrosoft.apk_ack_integration.model.enums;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportType {
    FORM_1("Сев", 630, "/templates/summaryReportForm1.xlsx"),
    FORM_2("Заготовка кормов", 631, "/templates/summaryReportForm2.xlsx"),
    FORM_3("Уборка урожая", 632, "/templates/summaryReportForm3.xlsx"),
    FORM_4("Внесение минеральных удобрений", 633, "/templates/summaryReportForm4.xlsx");
//    FORM_5("Внесение органических удобрений", 634, "/templates/summaryReportForm5.xlsx");

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

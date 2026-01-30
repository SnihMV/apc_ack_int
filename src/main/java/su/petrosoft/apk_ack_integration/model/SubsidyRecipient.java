package su.petrosoft.apk_ack_integration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SubsidyRecipient extends PlicanteInstance {
    private String fullTitle;
    private String shortTitle;
    private String inn;
    private String ogrn;
    private LocalDate ogrnDate;
    private String kpp;
    private Long machinePark;
    private Long appType;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SubsidyRecipient that = (SubsidyRecipient) o;
        return Objects.equals(inn, that.inn);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(inn);
    }

    public boolean deepEquals(SubsidyRecipient that) {
        return Objects.equals(fullTitle, that.fullTitle) &&
                Objects.equals(shortTitle, that.shortTitle) &&
                Objects.equals(ogrn, that.ogrn) &&
                Objects.equals(ogrnDate, that.ogrnDate) &&
                Objects.equals(kpp, that.kpp);
    }
}

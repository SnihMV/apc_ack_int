package su.petrosoft.apk_ack_integration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubsidyProgram {
    private Long id;
    private Long version;
    private Long parentId;
    private Long level;
    private String code;
    private String kcsr;
    private String dopKr;
    private String title;
    private List<Long> cofinancingLevelIds;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SubsidyProgram that = (SubsidyProgram) o;
        return Objects.equals(level, that.level)
                && Objects.equals(code, that.code)
                && Objects.equals(kcsr, that.kcsr)
                && Objects.equals(dopKr, that.dopKr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, code, kcsr, dopKr);
    }
}

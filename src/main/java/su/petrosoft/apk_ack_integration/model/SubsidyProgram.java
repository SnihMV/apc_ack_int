package su.petrosoft.apk_ack_integration.model;

import java.util.Collection;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SubsidyProgram extends PlicanteInstance{
    private Long parentId;
    private Long level;
    //    private String code;
    private Long kcsr;
    private Long dopKr;
    private String title;
    private Collection<Long> cofinancingLevelIds;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SubsidyProgram that = (SubsidyProgram) o;
        return
//                Objects.equals(code, that.code) &&
//                Objects.equals(level, that.level) &&
                        Objects.equals(kcsr, that.kcsr) &&
                        Objects.equals(dopKr, that.dopKr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
//                code,
//                level,
                kcsr,
                dopKr
        );
    }
}


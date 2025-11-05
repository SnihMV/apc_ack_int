package su.petrosoft.apk_ack_integration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubsidyProgramFstLvl {
    private Long id;
    private Long version;
    private String shortForm;
    private Long parentId;
    private Long level;
    private String code;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SubsidyProgramFstLvl that = (SubsidyProgramFstLvl) o;
        return Objects.equals(level, that.level) && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, code);
    }
}

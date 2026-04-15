package su.petrosoft.apk_ack_integration.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPKR;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class SubsidyProgram extends PlicanteInstance implements DictionaryDataRequester {
    private Long parentId;
    private Long level;
    private Long kcsr;
    private Long dopKr;
    private String title;
    private Collection<Long> cofinancingLevelIds;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SubsidyProgram that = (SubsidyProgram) o;
        return Objects.equals(kcsr, that.kcsr) && Objects.equals(dopKr, that.dopKr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kcsr, dopKr);
    }

    @Override
    public Map<Dictionary, Long> requestedDictionaryIds() {
        return Map.of(
                DOPKR, getDopKr()
        );
    }
}


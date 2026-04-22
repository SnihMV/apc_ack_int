package su.petrosoft.apk_ack_integration.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.*;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.TEMPLATE_ID;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class FinancingSource extends PlicanteInstance implements DictionaryDataRequester {
    private Long year;
    private Long kvsr;
    private Long kfsr;
    private Long kcsr;
    private Long dopKr;
    private Long kvr;
    private Long kosgu;
    private Long dopFk;
    private Long dopEk;
    private Long purpose;
    private Long ownershipForm;
    private Long subsidyProgramId;
    private Set<Long> cashPlanLimitIds;
    private String concatenatedKBK;

    @Override
    public long getTemplateId() {
        return TEMPLATE_ID;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FinancingSource that = (FinancingSource) o;
        return Objects.equals(year, that.year) && Objects.equals(kvsr, that.kvsr)
                && Objects.equals(kfsr, that.kfsr) && Objects.equals(kcsr, that.kcsr)
                && Objects.equals(kvr, that.kvr) && Objects.equals(kosgu, that.kosgu)
                && Objects.equals(dopFk, that.dopFk) && Objects.equals(dopEk, that.dopEk)
                && Objects.equals(dopKr, that.dopKr) && Objects.equals(purpose, that.purpose);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, kvsr, kfsr, kcsr, kvr, kosgu, dopFk, dopEk, dopKr, purpose);
    }

    @Override
    public Map<Dictionary, Long> requestedDictionaryIds() {
        return Map.of(
                KVSR, getKvsr(),
                KFSR, getKfsr(),
                KCSR, getKcsr(),
                DOPKR, getDopKr(),
                KVR, getKvr()
        );
    }
}

package su.petrosoft.apk_ack_integration.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class SubsidyProgramTrdLvl extends SubsidyProgramSndLvl {
    protected String dopKr;

    public SubsidyProgramTrdLvl(Long parentId, String code, String kcsr, String dopKr, String title) {
        this(3, parentId, code, kcsr, dopKr, title);
    }

    public SubsidyProgramTrdLvl(int level, Long parentId, String code, String kcsr, String dopKr, String title) {
        super(level, parentId, code, kcsr, title);
        this.dopKr = dopKr;
    }
}

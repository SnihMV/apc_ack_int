package su.petrosoft.apk_ack_integration.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class SubsidyProgramSndLvl extends SubsidyProgramFstLvl {
    protected String kcsr;

    public SubsidyProgramSndLvl(Long parentId, String code, String kcsr, String title) {
        this(2, parentId, code, kcsr, title);
    }

    public SubsidyProgramSndLvl(int level, Long parentId, String code, String kcsr, String title) {
        super(level, parentId, code, title);
        this.kcsr = kcsr;
    }
}

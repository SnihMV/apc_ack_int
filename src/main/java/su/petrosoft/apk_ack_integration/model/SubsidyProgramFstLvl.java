package su.petrosoft.apk_ack_integration.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class SubsidyProgramFstLvl extends AbstractSubsidyProgram {
    protected String code;

    public SubsidyProgramFstLvl(String code, String title) {
        this(1, null, code, title);
    }

    public SubsidyProgramFstLvl(int level, Long parentId, String code, String title) {
        super(level, parentId, title);
        this.code = code;
    }
}

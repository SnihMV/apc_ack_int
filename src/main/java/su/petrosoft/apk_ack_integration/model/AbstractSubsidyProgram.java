package su.petrosoft.apk_ack_integration.model;

public abstract class AbstractSubsidyProgram {
    protected Long id;
    protected Long version;
    protected int level;
    protected Long parentId;
    protected String title;

    public AbstractSubsidyProgram(int level, Long parentId, String title) {
        this.level = level;
        this.parentId = parentId;
        this.title = title;
    }
}

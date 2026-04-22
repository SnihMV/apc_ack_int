package su.petrosoft.apk_ack_integration.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.TEMPLATE_ID;

@Getter
@Setter
@SuperBuilder
public class SubsidyRecipient extends PlicanteInstance {
    private String fullTitle;
    private String shortTitle;
    private String inn;
    private String ogrn;
    private LocalDate ogrnDate;
    private String kpp;
    private Long districtId;
    private Collection<Long> machineParkIds;
    private Long appType;

    @Override
    public long getTemplateId() {
        return TEMPLATE_ID;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubsidyRecipient recipient = (SubsidyRecipient) o;
        return Objects.equals(inn, recipient.inn) && Objects.equals(kpp, recipient.kpp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), inn, kpp);
    }
}

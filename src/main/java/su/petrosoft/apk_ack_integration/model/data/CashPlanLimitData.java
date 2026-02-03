package su.petrosoft.apk_ack_integration.model.data;

public interface CashPlanLimitData extends FinancingSourceData {

    default String recipientInn(){
        return "";
    }

    default String recipientKpp(){
        return "";
    }

    Double assignTotal();

    Double assignFederal();

    Double assignRegional();

    Double janLimit();

    Double febLimit();

    Double marLimit();

    Double aprLimit();

    Double mayLimit();

    Double junLimit();

    Double julLimit();

    Double augLimit();

    Double sepLimit();

    Double octLimit();

    Double novLimit();

    Double decLimit();
}

package su.petrosoft.apk_ack_integration.model.data;

public interface CashPlanLimitData {

    String kfsr();

    String kcsr();

    String kvr();

    String kosgu();

    String kvsr();

    String dopFk();

    String dopEk();

    String dopKr();

    String purpose();

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

package labrini.ouiam.eventsourcing_cqrs_spring_axon.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class AccountDebitedEvent {
    private String accountId;
    private double amount;
    private String currency;

}

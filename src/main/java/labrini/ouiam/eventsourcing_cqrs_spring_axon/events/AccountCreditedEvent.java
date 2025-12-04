package labrini.ouiam.eventsourcing_cqrs_spring_axon.events;

import labrini.ouiam.eventsourcing_cqrs_spring_axon.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class AccountCreditedEvent {
    private String accountId;
    private double amount;
    private String currency;

}

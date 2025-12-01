package labrini.ouiam.eventsourcing_cqrs_spring_axon.events;

import labrini.ouiam.eventsourcing_cqrs_spring_axon.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class AccountCreatedEvent {
    private String accountId;
    private double initialBalance;
    private String currency;
    private AccountStatus status;

}

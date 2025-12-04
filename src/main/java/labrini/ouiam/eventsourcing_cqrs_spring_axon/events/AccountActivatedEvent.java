package labrini.ouiam.eventsourcing_cqrs_spring_axon.events;

import labrini.ouiam.eventsourcing_cqrs_spring_axon.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class AccountActivatedEvent {
    private String accountId;
    private AccountStatus status;
}

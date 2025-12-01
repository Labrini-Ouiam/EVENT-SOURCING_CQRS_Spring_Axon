package labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.aggregates;

import labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.commands.AddAccountCommand;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.enums.AccountStatus;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.events.AccountCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Slf4j
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private double balance;
    private AccountStatus status;

    // Default constructor required by Axon
    public AccountAggregate() {
    }

    // Command handler for AddAccountCommand
    @CommandHandler
    //fonction de decision
    public AccountAggregate(AddAccountCommand command) {
        log.info("Handling AddAccountCommand for account ID: {}", command.getId());
        // Validate initial balance
        if(command.getInitialBalance() < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        AggregateLifecycle.apply(new AccountCreatedEvent(
                command.getId(),
                command.getInitialBalance(),
                command.getCurrency(),
                AccountStatus.CREATED
        ));

    }

    @EventSourcingHandler
    // fonction d'evolution
    public void on(AccountCreatedEvent event) {
        log.info("Applying AccountCreatedEvent for account ID: {}", event.getAccountId());
        this.accountId = event.getAccountId();
        this.balance = event.getInitialBalance();
        this.status = event.getStatus();
    }

}

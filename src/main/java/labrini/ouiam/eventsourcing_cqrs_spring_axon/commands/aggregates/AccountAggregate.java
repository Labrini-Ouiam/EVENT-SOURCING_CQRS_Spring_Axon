package labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.aggregates;

import labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.commands.AddAccountCommand;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.commands.CreditAccountCommand;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.commands.DebitAccountCommand;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.commands.UpdateAccountStatusCommand;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.enums.AccountStatus;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.events.*;
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

        AggregateLifecycle.apply(new AccountActivatedEvent(
                command.getId(),
                AccountStatus.ACTIVATED
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

    @CommandHandler
    //fonction de decision
    public void handle(CreditAccountCommand command) {
        log.info("Handling CreditAccountCommand for account ID: {}", command.getId());
        if(!status.equals(AccountStatus.ACTIVATED))
            throw new RuntimeException("Account : "+command.getId()+" not activated");
        if(command.getAmount() < 0) {
            throw new IllegalArgumentException("Credit amount cannot be negative");
        }
        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));

    }

    @EventSourcingHandler
    // fonction d'evolution
    public void on(AccountCreditedEvent event) {
        log.info("Applying AccountCreditedEvent for account ID: {}", event.getAccountId());
        this.accountId = event.getAccountId();
        this.balance += event.getAmount();
    }

    @EventSourcingHandler
    // fonction d'evolution
    public void on(AccountActivatedEvent event) {
        log.info("Applying AccountActivatedEvent for account ID: {}", event.getAccountId());
        this.accountId = event.getAccountId();
        this.status = event.getStatus();
    }

    @CommandHandler
    //fonction de decision
    public void handle(DebitAccountCommand command) {
        log.info("Handling DebitAccountCommand for account ID: {}", command.getId());
        if(!status.equals(AccountStatus.ACTIVATED))
            throw new RuntimeException("Account : "+command.getId()+" not activated");
        if(command.getAmount() > this.balance) {
            throw new RuntimeException("Insufficient balance");
        }
        if(command.getAmount() < 0) {
            throw new IllegalArgumentException("Credit amount cannot be negative");
        }
        AggregateLifecycle.apply(new AccountDebitedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));

    }

    @EventSourcingHandler
    // fonction d'evolution
    public void on(AccountDebitedEvent event) {
        log.info("Applying AccountDebitedEvent for account ID: {}", event.getAccountId());
        this.accountId = event.getAccountId();
        this.balance -= event.getAmount();
    }

    @CommandHandler
    //fonction de decision
    public void handle(UpdateAccountStatusCommand command) {
        log.info("Handling DebitAccountCommand for account ID: {}", command.getId());
        if(command.getStatus()==status)
            throw new RuntimeException("Account : "+command.getId()+" already in status "+command.getStatus());
        AggregateLifecycle.apply(new AccountStatusUpdatedEvent(
                command.getId(),
                command.getStatus()
        ));

    }

    @EventSourcingHandler
    // fonction d'evolution
    public void on(AccountStatusUpdatedEvent event) {
        log.info("Applying AccountStatusUpdatedEvent for account ID: {}", event.getAccountId());
        this.accountId = event.getAccountId();
        this.status = event.getStatus();
    }

}

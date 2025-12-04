package labrini.ouiam.eventsourcing_cqrs_spring_axon.query.handlers;

import labrini.ouiam.eventsourcing_cqrs_spring_axon.enums.OperationType;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.events.*;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.query.entities.Account;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.query.entities.AccountOperation;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.query.repository.AccountOperationRepository;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.query.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountEventHandler {
    private final QueryUpdateEmitter queryUpdateEmitter;
    private AccountRepository accountRepository;
    private AccountOperationRepository accountOperationRepository;


    public AccountEventHandler(AccountRepository accountRepository, AccountOperationRepository accountOperationRepository, QueryUpdateEmitter queryUpdateEmitter) {
        this.accountRepository = accountRepository;
        this.accountOperationRepository = accountOperationRepository;
        this.queryUpdateEmitter = queryUpdateEmitter;
    }

    @EventHandler
    public void on(AccountCreatedEvent event, EventMessage eventMessage) {
        log.info("AccountCreatedEvent received: {}", event);
        Account account=Account.builder()
                .id(event.getAccountId())
                .currency(event.getCurrency())
                .balance(event.getInitialBalance())
                .status(event.getStatus())
                .createdAt(eventMessage.getTimestamp())
                .build();
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountActivatedEvent event) {
        log.info("AccountActivatedEvent received: {}", event);
        Account account=accountRepository.findById(event.getAccountId()).orElseThrow(()->new RuntimeException("Account not found"));
        account.setStatus(event.getStatus());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountStatusUpdatedEvent event) {
        log.info("AccountStatusUpdatedEvent received: {}", event);
        Account account=accountRepository.findById(event.getAccountId()).orElseThrow(()->new RuntimeException("Account not found"));
        account.setStatus(event.getStatus());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountDebitedEvent event,EventMessage eventMessage) {
        log.info("AccountDebitedEvent received: {}", event);
        Account account=accountRepository.findById(event.getAccountId()).orElseThrow(()->new RuntimeException("Account not found"));
        AccountOperation operation=AccountOperation.builder()
                .amount(event.getAmount())
                .type(OperationType.DEBIT)
                .currency(event.getCurrency())
                .date(eventMessage.getTimestamp())
                .account(account)
                .build();
        accountOperationRepository.save(operation);
        account.setBalance(account.getBalance() - event.getAmount());
        accountRepository.save(account);
        queryUpdateEmitter.emit(e->true,operation);
    }

    @EventHandler
    public void on(AccountCreditedEvent event, EventMessage eventMessage) {
        log.info("AccountCreditedEvent received: {}", event);
        Account account=accountRepository.findById(event.getAccountId()).orElseThrow(()->new RuntimeException("Account not found"));
        AccountOperation operation=AccountOperation.builder()
                .amount(event.getAmount())
                .type(OperationType.DEBIT)
                .currency(event.getCurrency())
                .date(eventMessage.getTimestamp())
                .account(account)
                .build();
        accountOperationRepository.save(operation);
        account.setBalance(account.getBalance() + event.getAmount());
        accountRepository.save(account);
        queryUpdateEmitter.emit(e->true,operation);
    }
}

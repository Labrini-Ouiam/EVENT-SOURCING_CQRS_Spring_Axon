package labrini.ouiam.eventsourcing_cqrs_spring_axon.query.handlers;

import labrini.ouiam.eventsourcing_cqrs_spring_axon.query.dtos.AccountStatementResponseDTO;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.query.entities.Account;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.query.entities.AccountOperation;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.query.queries.GetAccountStatementQuery;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.query.queries.GetAllAccountsQuery;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.query.queries.WatchEventQuery;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.query.repository.AccountOperationRepository;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.query.repository.AccountRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountQueryHandler {
    private AccountRepository accountRepository;
    private AccountOperationRepository accountOperationRepository;
    private QueryUpdateEmitter queryUpdateEmitter;

    public AccountQueryHandler(AccountRepository accountRepository, AccountOperationRepository accountOperationRepository, QueryUpdateEmitter queryUpdateEmitter) {
        this.accountRepository = accountRepository;
        this.accountOperationRepository = accountOperationRepository;
        this.queryUpdateEmitter = queryUpdateEmitter;
    }

    @QueryHandler
    public List<Account> on(GetAllAccountsQuery query) {
        return accountRepository.findAll();
    }

    @QueryHandler
    public AccountStatementResponseDTO on(GetAccountStatementQuery query) {
        Account account=accountRepository.findById(query.getAccountId()).orElseThrow(()->new RuntimeException("Account not found"));
        List<AccountOperation> operations=accountOperationRepository.findByAccountId(query.getAccountId());
        return new AccountStatementResponseDTO(
                account,
                operations
        );
    }

    @QueryHandler
    public AccountOperation on(WatchEventQuery query) {
        return AccountOperation.builder().build();
    }
}

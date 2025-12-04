package labrini.ouiam.eventsourcing_cqrs_spring_axon.query.Controllers;

import labrini.ouiam.eventsourcing_cqrs_spring_axon.query.dtos.AccountStatementResponseDTO;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.query.entities.Account;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.query.entities.AccountOperation;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.query.queries.GetAccountStatementQuery;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.query.queries.GetAllAccountsQuery;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.query.queries.WatchEventQuery;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/query/accounts")
public class AccountQueryController {
    private QueryGateway queryGateway;

    public AccountQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/all")
    public CompletableFuture<List<Account>> getAllAccounts() {
        CompletableFuture<List<Account>> response= queryGateway.query(
                new GetAllAccountsQuery(),
                ResponseTypes.multipleInstancesOf(Account.class)
        );
        return response;
    }

    @GetMapping("/accountStatement/{accountId}")
    public CompletableFuture<AccountStatementResponseDTO> getAccountStatement(@PathVariable String accountId) {
        return queryGateway.query(new GetAccountStatementQuery(accountId),
                ResponseTypes.instanceOf(AccountStatementResponseDTO.class));
    }

    // Endpoint to watch account events in real-time using Server-Sent Events (SSE)
    @GetMapping(value = "/watch/{accountId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AccountOperation> watch(@PathVariable String accountId) {
        SubscriptionQueryResult<AccountOperation, AccountOperation> result =
                queryGateway.subscriptionQuery(
                        new WatchEventQuery(accountId),
                        ResponseTypes.instanceOf(AccountOperation.class),
                        ResponseTypes.instanceOf(AccountOperation.class)
                );

        return result.initialResult().concatWith(result.updates());
    }

}

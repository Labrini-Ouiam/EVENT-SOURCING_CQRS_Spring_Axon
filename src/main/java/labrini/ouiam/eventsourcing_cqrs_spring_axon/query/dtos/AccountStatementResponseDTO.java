package labrini.ouiam.eventsourcing_cqrs_spring_axon.query.dtos;

import labrini.ouiam.eventsourcing_cqrs_spring_axon.query.entities.Account;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.query.entities.AccountOperation;

import java.util.List;

public record AccountStatementResponseDTO(
        Account account,
        List<AccountOperation> Operations
) {
}

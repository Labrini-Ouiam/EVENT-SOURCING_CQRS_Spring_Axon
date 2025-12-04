package labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.dtos;

import labrini.ouiam.eventsourcing_cqrs_spring_axon.enums.AccountStatus;

public record UpdateAccountStatusRequestDTO(
        String accountId,
        AccountStatus status
) {
}

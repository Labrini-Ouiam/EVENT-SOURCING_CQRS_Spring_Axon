package labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.commands;

import labrini.ouiam.eventsourcing_cqrs_spring_axon.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter @AllArgsConstructor
public class UpdateAccountStatusCommand {
    @TargetAggregateIdentifier
    private String id;
    private AccountStatus status;
}

package labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.dtos;

public record AddNewAccountRequestDTO(double initialBalance, String currency) {
}

package labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.dtos;

public record CreditAccountRequestDTO(String accountId,double amount, String currency) {
}

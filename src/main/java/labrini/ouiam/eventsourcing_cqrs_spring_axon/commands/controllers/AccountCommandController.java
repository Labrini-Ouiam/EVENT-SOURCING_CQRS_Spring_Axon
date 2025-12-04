package labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.controllers;

import labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.commands.AddAccountCommand;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.commands.CreditAccountCommand;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.dtos.AddNewAccountRequestDTO;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.dtos.CreditAccountRequestDTO;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@RequestMapping("/commands/accounts")
public class AccountCommandController {
    private CommandGateway commandGateway;
    private EventStore evenStrore;

    public AccountCommandController(CommandGateway commandGateway, EventStore evenStrore) {
        this.commandGateway = commandGateway;
        this.evenStrore = evenStrore;
    }

    // Endpoint to add a new account
    @PostMapping("/add")
    public CompletableFuture<String> addNewAccount(@RequestBody AddNewAccountRequestDTO request) {
        // Create and send the AddAccountCommand
       CompletableFuture<String> response= commandGateway.send(new AddAccountCommand(
                java.util.UUID.randomUUID().toString(),
                request.initialBalance(),
                request.currency()
        ));
        return response;
    }

    // Endpoint to credit an existing account
    @PostMapping("/credit")
    public CompletableFuture<String> CreditAccount(@RequestBody CreditAccountRequestDTO request) {
        // Create and send the CreditAccountCommand
        CompletableFuture<String> response= commandGateway.send(new CreditAccountCommand(
                request.accountId(),
                request.amount(),
                request.currency()
        ));
        return response;
    }

    // Global exception handler
    @ExceptionHandler(Exception.class)
    public String exeptionHandler(Exception e){
        return e.getMessage();
    }

    // Endpoint to get event stream for a specific account
    @GetMapping("/events/{accountId}")
    public Stream evenStrore(@PathVariable String accountId){
        return evenStrore.readEvents(accountId).asStream();
    }


}

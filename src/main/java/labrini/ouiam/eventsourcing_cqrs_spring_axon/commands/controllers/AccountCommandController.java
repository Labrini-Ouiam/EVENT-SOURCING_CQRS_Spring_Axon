package labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.controllers;

import labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.commands.AddAccountCommand;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.commands.CreditAccountCommand;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.commands.DebitAccountCommand;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.commands.UpdateAccountStatusCommand;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.dtos.AddNewAccountRequestDTO;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.dtos.CreditAccountRequestDTO;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.dtos.DebitAccountRequestDTO;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.dtos.UpdateAccountStatusRequestDTO;
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

    // Endpoint to debit an existing account
    @PostMapping("/debit")
    public CompletableFuture<String> debitAccount(@RequestBody DebitAccountRequestDTO request) {
        // Create and send the DebitAccountCommand
        CompletableFuture<String> response= commandGateway.send(new DebitAccountCommand(
                request.accountId(),
                request.amount(),
                request.currency()
        ));
        return response;
    }

    // Endpoint to update account status
    @PutMapping("/updateStatus")
    public CompletableFuture<String> debitAccount(@RequestBody UpdateAccountStatusRequestDTO request) {
        // Create and send the DebitAccountCommand
        CompletableFuture<String> response= commandGateway.send(new UpdateAccountStatusCommand(
                request.accountId(),
                request.status()
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

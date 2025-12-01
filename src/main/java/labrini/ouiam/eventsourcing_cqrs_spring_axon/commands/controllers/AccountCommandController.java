package labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.controllers;

import labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.commands.AddAccountCommand;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.commands.dtos.AddNewAccountRequestDTO;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/commands/accounts")
public class AccountCommandController {
    private CommandGateway commandGateway;

    public AccountCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
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

    // Global exception handler
    @ExceptionHandler(Exception.class)
    public String exeptionHandler(Exception e){
        return e.getMessage();
    }


}

package ma.microservice.comptecqrseventsourcing.commands.controllers;


import lombok.AllArgsConstructor;
import ma.microservice.comptecqrseventsourcing.commands.commands.CreateAccountCommand;
import ma.microservice.comptecqrseventsourcing.commands.commands.CreditAccountCommand;
import ma.microservice.comptecqrseventsourcing.commands.commands.DebitAccountCommand;
import ma.microservice.comptecqrseventsourcing.commands.dto.CreateAccountRequestDTO;
import ma.microservice.comptecqrseventsourcing.commands.dto.CreditAccountRequestDTO;
import ma.microservice.comptecqrseventsourcing.commands.dto.DebitAccountRequestDTO;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/commands/account")
@AllArgsConstructor
public class AccountCommandController {

    private CommandGateway commandGateway;
    private EventStore eventStore;
    @PostMapping(path = "/create")
    public CompletableFuture<String> createAccount(@RequestBody CreateAccountRequestDTO request){
        CompletableFuture<String> commandResponse= commandGateway.send(new CreateAccountCommand(
                UUID.randomUUID().toString(),
                request.getInitialBalance(),
                request.getCurrency()
        )) ;
        return commandResponse;
    }

    @PutMapping(path = "/credit")
    public CompletableFuture<String> creditAccount(@RequestBody CreditAccountRequestDTO request){
        CompletableFuture<String> commandResponse= commandGateway.send(new CreditAccountCommand(
                request.getAccountId(),
                request.getAmount(),
                request.getCurrency()
        )) ;
        return commandResponse;
    }
    @PutMapping(path = "/debit")
    public CompletableFuture<String> debitAccount(@RequestBody DebitAccountRequestDTO request){
        CompletableFuture<String> commandResponse= commandGateway.send(new DebitAccountCommand(
                request.getAccountId(),
                request.getAmount(),
                request.getCurrency()
        )) ;
        return commandResponse;
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception e){
        ResponseEntity<String> entity=new ResponseEntity<>(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return entity;
    }
    @GetMapping("/eventStore/{accountId}")
    public Stream eventStore(@PathVariable String accountId){
        return  eventStore.readEvents(accountId).asStream();
    }
}

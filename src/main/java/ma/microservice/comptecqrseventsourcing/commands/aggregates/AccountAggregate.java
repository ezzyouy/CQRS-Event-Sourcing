package ma.microservice.comptecqrseventsourcing.commands.aggregates;

import ma.microservice.comptecqrseventsourcing.commands.commands.CreateAccountCommand;
import ma.microservice.comptecqrseventsourcing.commands.commands.CreditAccountCommand;
import ma.microservice.comptecqrseventsourcing.commands.commands.DebitAccountCommand;
import ma.microservice.comptecqrseventsourcing.commands.exceptions.AmountNegativeException;
import ma.microservice.comptecqrseventsourcing.commands.exceptions.BalanceNotSufficientException;
import ma.microservice.comptecqrseventsourcing.enums.AccountStatus;
import ma.microservice.comptecqrseventsourcing.events.AccountActivatedEvent;
import ma.microservice.comptecqrseventsourcing.events.AccountCreatedEvent;
import ma.microservice.comptecqrseventsourcing.events.AccountCreditedEvent;
import ma.microservice.comptecqrseventsourcing.events.AccountDebitedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import javax.persistence.Enumerated;

@Aggregate
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private double balance;
    private String currency;
    private AccountStatus status;

    public AccountAggregate() {
    }

    @CommandHandler
    public AccountAggregate(CreateAccountCommand createAccountCommand) {
        if(createAccountCommand.getInitialBalance()<0) throw  new RuntimeException("Impossible de creer un solde avec le montant negative");

        AggregateLifecycle.apply(new AccountCreatedEvent(
                createAccountCommand.getId(),
                createAccountCommand.getInitialBalance(),
                createAccountCommand.getCurrency(),
                AccountStatus.CREATED
        ));
    }
    @EventSourcingHandler
    public void on(AccountCreatedEvent event){
        this.accountId=event.getId();
        this.balance=event.getInitialBalance();
        this.currency=event.getCurrency();
        this.status=AccountStatus.CREATED;
        AggregateLifecycle.apply(new AccountActivatedEvent(
                event.getId(),
                AccountStatus.ACTIVATED
        ));
    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent event){
        this.status=event.getStatus();
    }

    @CommandHandler
    public void handle(CreditAccountCommand command){
        if(command.getAmount()<0) throw new AmountNegativeException("Amount should not be negative");
        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }
    @EventSourcingHandler
    public void on(AccountCreditedEvent event){
        this.balance += event.getAmount();
    }

    @CommandHandler
    public void handle(DebitAccountCommand command){
        if(command.getAmount()<0) throw new AmountNegativeException("Amount should not be negative");
        if(this.balance<command.getAmount()) throw new BalanceNotSufficientException("Balance not sufficient Exception =>"+balance);
        AggregateLifecycle.apply(new AccountDebitedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }
    @EventSourcingHandler
    public void on(AccountDebitedEvent event){
        this.balance -= event.getAmount();
    }
}

package ma.microservice.comptecqrseventsourcing.events;

import lombok.Getter;
import ma.microservice.comptecqrseventsourcing.enums.AccountStatus;

public class AccountActivatedEvent extends  BaseEvent<String>{

    @Getter private AccountStatus status;

    public AccountActivatedEvent(String id, AccountStatus status) {
        super(id);
        this.status=status;
    }
}

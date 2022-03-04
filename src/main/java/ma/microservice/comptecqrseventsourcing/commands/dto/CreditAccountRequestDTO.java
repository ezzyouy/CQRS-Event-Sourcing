package ma.microservice.comptecqrseventsourcing.commands.dto;

import lombok.Data;

@Data
public class CreditAccountRequestDTO {
    private String accountId;
    private double amount;
    private String currency;
}

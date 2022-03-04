package ma.microservice.comptecqrseventsourcing.commands.dto;

import lombok.Data;

@Data
public class DebitAccountRequestDTO {
    private String accountId;
    private double amount;
    private String currency;
}

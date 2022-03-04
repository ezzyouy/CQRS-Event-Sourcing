package ma.microservice.comptecqrseventsourcing.commands.exceptions;


public class AmountNegativeException extends RuntimeException {
    public AmountNegativeException(String message) {
        super(message);
    }
}

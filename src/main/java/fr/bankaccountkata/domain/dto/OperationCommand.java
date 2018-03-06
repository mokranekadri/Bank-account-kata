package fr.bankaccountkata.domain.dto;

public class OperationCommand {

    private long amount ;

    public OperationCommand() {
    }

    public OperationCommand(long amount) {
        this.amount = amount;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}

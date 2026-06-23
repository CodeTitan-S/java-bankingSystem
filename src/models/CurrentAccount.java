package models;

public class CurrentAccount extends Account {
    public CurrentAccount(String accountNumber, String holderName, String pin) {
        super(accountNumber, holderName, pin);
    }

    @Override
    public double calculateInterest() {
        return getBalance() * 0.01;
    }

    @Override
    public String toString() {
        return String.format("Current Account[%s] Holder: %s Balance: $%.2f",
                getAccountNumber(), getHolderName(), getBalance());
    }
}
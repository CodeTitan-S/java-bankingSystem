package models;

public class SavingsAccount extends Account {
    public SavingsAccount(String accountNumber, String holderName, String pin) {
        super(accountNumber, holderName, pin);
    }

    @Override
    public double calculateInterest() {
        return getBalance() * 0.04;
    }

    @Override
    public String toString() {
        return String.format("Savings Account[%s] Holder: %s Balance: $%.2f",
                getAccountNumber(), getHolderName(), getBalance());
    }
}
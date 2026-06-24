package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Account implements Serializable {
    private String accountNumber;
    private String holderName;
    private double balance;
    private String pin;
    private List<Transaction> transactionHistory;

    // New fields for PIN lockout
    private int failedLoginAttempts;
    private boolean locked;

    private static final long serialVersionUID = 1L;

    public Account(String accountNumber, String holderName, String pin) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.pin = pin;
        this.balance = 0.0;
        this.transactionHistory = new ArrayList<>();
        this.failedLoginAttempts = 0;
        this.locked = false;
    }

    public abstract double calculateInterest();

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            addTransaction(new Transaction("Deposit", amount));
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            addTransaction(new Transaction("Withdrawal", amount));
        }
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void addTransaction(Transaction t) {
        transactionHistory.add(t);
    }

    public boolean validatePin(String inputPin) {
        return this.pin.equals(inputPin);
    }

    // Lockout management
    public void incrementFailedAttempts() {
        failedLoginAttempts++;
        if (failedLoginAttempts >= 3) {
            locked = true;
        }
    }

    public void resetFailedAttempts() {
        failedLoginAttempts = 0;
        locked = false;
    }

    public boolean isLocked() {
        return locked;
    }
}
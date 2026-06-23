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

    public Account(String accountNumber, String holderName, String pin) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.pin = pin;
        this.balance = 0.0;
        this.transactionHistory = new ArrayList<>();
    }

    // Abstract method – to be implemented by each account type
    public abstract double calculateInterest();

    // Concrete methods
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            addTransaction(new Transaction("DEPOSIT", amount));
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            addTransaction(new Transaction("WITHDRAWAL", amount));
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
        return transactionHistory;   // returns reference – ok for in‑memory
    }

    public void addTransaction(Transaction t) {
        transactionHistory.add(t);
    }

    public boolean validatePin(String inputPin) {
        return this.pin.equals(inputPin);
    }

    // For serialization compatibility
    private static final long serialVersionUID = 1L;
}
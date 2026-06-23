package services;

import models.Account;
import models.SavingsAccount;
import models.CurrentAccount;
import storage.BankData;
import exceptions.InsufficientFundsException;

public class BankService {

    public static void checkBalance(Account acc) {
        System.out.printf("Current Balance: ₹%.2f%n", acc.getBalance());
    }

    public static void deposit(Account acc, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }
        acc.deposit(amount);
        TransactionService.logDeposit(acc, amount);
        System.out.printf("₹%.2f deposited. New balance: ₹%.2f%n",
                amount, acc.getBalance());
    }

    public static void withdraw(Account acc, double amount)
            throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }
        if (acc.getBalance() < amount) {
            throw new InsufficientFundsException(amount, acc.getBalance());
        }
        acc.withdraw(amount);
        TransactionService.logWithdrawal(acc, amount);
        System.out.printf("₹%.2f withdrawn. Remaining balance: ₹%.2f%n",
                amount, acc.getBalance());
    }

    public static void transfer(Account sender, String receiverAccNo, double amount)
            throws InsufficientFundsException {
        if (!BankData.accountExists(receiverAccNo)) {
            System.out.println("Account " + receiverAccNo + " not found.");
            return;
        }
        if (sender.getAccountNumber().equals(receiverAccNo)) {
            System.out.println("Cannot transfer to your own account.");
            return;
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero.");
        }
        if (sender.getBalance() < amount) {
            throw new InsufficientFundsException(amount, sender.getBalance());
        }
        Account receiver = BankData.getAccount(receiverAccNo);
        sender.withdraw(amount);
        receiver.deposit(amount);
        TransactionService.logTransfer(sender, receiver, amount);
        System.out.printf("₹%.2f transferred to Account %s (%s).%n",
                amount, receiverAccNo, receiver.getHolderName());
    }

    public static void calculateAndShowInterest(Account acc) {
        double interest = acc.calculateInterest();
        String rateLabel;
        if (acc instanceof SavingsAccount) {
            rateLabel = "4%";
        } else if (acc instanceof CurrentAccount) {
            rateLabel = "1%";
        } else {
            rateLabel = "unknown";
        }
        System.out.printf("Annual interest on ₹%.2f = ₹%.2f%n",
                acc.getBalance(), interest);
        System.out.println("  (Rate: " + rateLabel + ")");
    }
}
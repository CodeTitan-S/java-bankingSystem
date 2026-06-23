package services;

import models.Account;
import models.Transaction;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionService {

    // Format for display: DD-MM-YYYY HH:mm
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public static void logDeposit(Account acc, double amount) {
        Transaction t = new Transaction("Deposit", amount);
        acc.addTransaction(t);
    }

    public static void logWithdrawal(Account acc, double amount) {
        Transaction t = new Transaction("Withdrawal", amount);
        acc.addTransaction(t);
    }

    public static void logTransfer(Account sender, Account receiver, double amount) {
        Transaction out = new Transaction("Transfer Out", amount);
        Transaction in = new Transaction("Transfer In", amount);
        sender.addTransaction(out);
        receiver.addTransaction(in);
    }

    public static void printHistory(Account acc) {
        List<Transaction> history = acc.getTransactionHistory();
        if (history.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }
        int i = 1;
        for (Transaction t : history) {
            System.out.printf("[%d]. %s ₹%.2f — %s%n",
                    i++,
                    t.getType(),
                    t.getAmount(),
                    t.getDateTime().format(FORMATTER));
        }
    }
}
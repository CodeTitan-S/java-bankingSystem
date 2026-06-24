package features;

import models.Account;
import models.Transaction;
import services.BankService;
import storage.BankData;
import storage.FileManager;
import utils.InputHelper;
import exceptions.InsufficientFundsException;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class ATMMode {

    public static void launch() {
        System.out.println("\n╔══════════════════════╗");
        System.out.println("║     BANK ATM         ║");
        System.out.println("╚══════════════════════╝");
        String accNo = InputHelper.getString("Enter account number (insert card): ");
        Account acc = BankData.getAccount(accNo);
        if (acc == null) {
            System.out.println("Card not recognised.");
            return;
        }
        // PIN entry with lockout
        int attempts = 0;
        while (attempts < 3) {
            String pin = InputHelper.getString("Enter PIN: ");
            if (acc.isLocked()) {
                System.out.println("Account is locked. Please visit branch.");
                return;
            }
            if (acc.validatePin(pin)) {
                acc.resetFailedAttempts();
                FileManager.saveData();
                break;
            } else {
                acc.incrementFailedAttempts();
                if (acc.isLocked()) {
                    System.out.println("Account locked after 3 failed attempts.");
                    FileManager.saveData();
                    return;
                }
                System.out.println("Incorrect PIN. Try again.");
                attempts++;
                if (attempts == 3) {
                    System.out.println("Too many attempts. Account locked.");
                    FileManager.saveData();
                    return;
                }
            }
        }
        atmMenu(acc);
    }

    private static void atmMenu(Account acc) {
        while (true) {
            System.out.println("\n╔══════════════════════╗");
            System.out.println("║     BANK ATM         ║");
            System.out.println("╠══════════════════════╣");
            System.out.println("║ 1. Cash Withdrawal   ║");
            System.out.println("║ 2. Balance Inquiry   ║");
            System.out.println("║ 3. Mini Statement    ║");
            System.out.println("║ 4. Deposit           ║");
            System.out.println("║ 5. Eject Card        ║");
            System.out.println("╚══════════════════════╝");
            int choice = InputHelper.getInt("Select: ");
            switch (choice) {
                case 1:
                    double amt = InputHelper.getPositiveDouble("Enter amount: ₹");
                    try {
                        BankService.withdraw(acc, amt);
                        FileManager.saveData();
                    } catch (InsufficientFundsException e) {
                        System.out.println(e.getMessage());
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    BankService.checkBalance(acc);
                    break;
                case 3:
                    showMiniStatement(acc);
                    break;
                case 4:
                    double depAmt = InputHelper.getPositiveDouble("Enter deposit amount: ₹");
                    try {
                        BankService.deposit(acc, depAmt);
                        FileManager.saveData();
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    System.out.println("Thank you. Goodbye.");
                    FileManager.saveData();
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void showMiniStatement(Account acc) {
        List<Transaction> history = acc.getTransactionHistory();
        if (history.isEmpty()) {
            System.out.println("No transactions.");
            return;
        }
        int start = Math.max(0, history.size() - 5);
        System.out.println("Last 5 transactions:");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        for (int i = start; i < history.size(); i++) {
            Transaction t = history.get(i);
            System.out.printf("%s ₹%.2f — %s%n", t.getType(), t.getAmount(),
                    t.getDateTime().format(fmt));
        }
    }
}
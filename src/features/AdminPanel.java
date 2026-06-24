package features;

import models.Account;
import models.SavingsAccount;
import storage.BankData;
import storage.FileManager;
import utils.InputHelper;
import java.util.Collection;

public class AdminPanel {
    private static final String ADMIN_PASSWORD = "admin123";

    public static void launch() {
        System.out.print("Enter admin password: ");
        String pass = InputHelper.getString("");
        if (!pass.equals(ADMIN_PASSWORD)) {
            System.out.println("Access denied.");
            return;
        }
        while (true) {
            System.out.println("\n--- Admin Panel ---");
            System.out.println("1. View all accounts");
            System.out.println("2. Search account by name");
            System.out.println("3. Delete account");
            System.out.println("4. View total bank funds");
            System.out.println("5. Unlock a locked account");
            System.out.println("6. Exit admin panel");
            int choice = InputHelper.getInt("Enter choice: ");
            switch (choice) {
                case 1: viewAllAccounts(); break;
                case 2:
                    String q = InputHelper.getString("Enter name to search: ");
                    searchByName(q);
                    break;
                case 3:
                    String acc = InputHelper.getString("Enter account number: ");
                    deleteAccount(acc);
                    break;
                case 4: viewTotalFunds(); break;
                case 5:
                    String accNo = InputHelper.getString("Enter account number to unlock: ");
                    unlockAccount(accNo);
                    break;
                case 6: return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static void viewAllAccounts() {
        Collection<Account> all = BankData.getAllAccounts();
        if (all.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }
        System.out.printf("%-10s %-20s %-10s %-12s %-8s%n", "Acc No", "Name", "Type", "Balance", "Status");
        for (Account a : all) {
            String type = (a instanceof SavingsAccount) ? "Savings" : "Current";
            String status = a.isLocked() ? "LOCKED" : "Active";
            System.out.printf("%-10s %-20s %-10s ₹%-11.2f %-8s%n",
                    a.getAccountNumber(), a.getHolderName(), type, a.getBalance(), status);
        }
    }

    private static void searchByName(String query) {
        boolean found = false;
        for (Account a : BankData.getAllAccounts()) {
            if (a.getHolderName().toLowerCase().contains(query.toLowerCase())) {
                if (!found) {
                    System.out.printf("%-10s %-20s %-10s %-12s %-8s%n", "Acc No", "Name", "Type", "Balance", "Status");
                }
                String type = (a instanceof SavingsAccount) ? "Savings" : "Current";
                String status = a.isLocked() ? "LOCKED" : "Active";
                System.out.printf("%-10s %-20s %-10s ₹%-11.2f %-8s%n",
                        a.getAccountNumber(), a.getHolderName(), type, a.getBalance(), status);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No matching accounts found.");
        }
    }

    private static void deleteAccount(String accNo) {
        Account acc = BankData.getAccount(accNo);
        if (acc == null) {
            System.out.println("Account not found.");
            return;
        }
        String confirm = InputHelper.getString("Are you sure? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes")) {
            BankData.removeAccount(accNo);      // <-- fixed: using public method
            FileManager.saveData();
            System.out.println("Account " + accNo + " deleted.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static void viewTotalFunds() {
        double total = 0;
        for (Account a : BankData.getAllAccounts()) {
            total += a.getBalance();
        }
        System.out.printf("Total funds across %d accounts: ₹%.2f%n", BankData.getAllAccounts().size(), total);
    }

    private static void unlockAccount(String accNo) {
        Account acc = BankData.getAccount(accNo);
        if (acc == null) {
            System.out.println("Account not found.");
            return;
        }
        if (!acc.isLocked()) {
            System.out.println("Account is not locked.");
            return;
        }
        acc.resetFailedAttempts();
        FileManager.saveData();
        System.out.println("Account " + accNo + " unlocked.");
    }
}
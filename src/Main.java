import models.*;
import storage.BankData;
import utils.InputHelper;

public class Main {
    public static void main(String[] args) {
        while (true) {
            showMainMenu();
            int choice = InputHelper.getInt("Choose an option: ");
            switch (choice) {
                case 1:
                    handleCreateAccount();
                    break;
                case 2:
                    handleLogin();
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please enter 1, 2, or 3.");
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("\n===== BANK SYSTEM =====");
        System.out.println("[1] Create Account");
        System.out.println("[2] Login");
        System.out.println("[3] Exit");
    }

    private static void handleCreateAccount() {
        System.out.println("\n--- Create New Account ---");

        // 1. Get holder name (cannot be blank)
        String name;
        while (true) {
            name = InputHelper.getString("Enter full name: ");
            if (!name.isBlank()) break;
            System.out.println("Name cannot be blank.");
        }

        // 2. Choose account type (1 = Savings, 2 = Current)
        int type;
        while (true) {
            type = InputHelper.getInt("Account type: [1] Savings  [2] Current: ");
            if (type == 1 || type == 2) break;
            System.out.println("Please choose 1 or 2.");
        }

        // 3. Set PIN (exactly 4 digits, numeric)
        String pin;
        while (true) {
            pin = InputHelper.getString("Create a 4-digit PIN: ");
            if (pin.matches("\\d{4}")) break;
            System.out.println("PIN must be exactly 4 digits (0-9).");
        }

        // 4. Confirm PIN
        String confirmPin;
        while (true) {
            confirmPin = InputHelper.getString("Confirm PIN: ");
            if (pin.equals(confirmPin)) break;
            System.out.println("PINs do not match. Please try again.");
        }

        // 5. Generate account number and create the appropriate subclass
        String accNumber = BankData.generateAccountNumber();
        Account newAccount;
        if (type == 1) {
            newAccount = new SavingsAccount(accNumber, name, pin);
        } else {
            newAccount = new CurrentAccount(accNumber, name, pin);
        }

        // 6. Store and confirm
        BankData.addAccount(newAccount);
        System.out.println("Account created! Account Number: " + accNumber);
    }

    private static void handleLogin() {
        System.out.println("\n--- Login ---");
        String accNumber = InputHelper.getString("Enter account number: ");
        String pin = InputHelper.getString("Enter PIN: ");

        Account account = BankData.getAccount(accNumber);
        if (account != null && account.validatePin(pin)) {
            System.out.println("Welcome, " + account.getHolderName() + ".");
            showBankingMenu(account);
        } else {
            System.out.println("Invalid credentials. Please try again.");
        }
    }

    private static void showBankingMenu(Account acc) {
        // Stub for Module 2
        System.out.println("Banking menu coming in Module 2.");
    }
}
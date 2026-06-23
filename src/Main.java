import models.*;
import storage.BankData;
import utils.InputHelper;
import services.BankService;
import exceptions.InsufficientFundsException;
import services.TransactionService;

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
        String typeLabel = (acc instanceof SavingsAccount) ? "Savings Account" : "Current Account";
        while (true) {
            System.out.println();
            System.out.println("============================");
            System.out.printf("Welcome, %s (%s)%n", acc.getHolderName(), typeLabel);
            System.out.println("============================");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Money");
            System.out.println("5. Transaction History");
            System.out.println("6. Calculate Interest");
            System.out.println("7. Logout");

            int choice = InputHelper.getInt("Enter choice: ");
            switch (choice) {
                case 1:
                    BankService.checkBalance(acc);
                    break;
                case 2:
                    double depositAmt = InputHelper.getPositiveDouble("Enter amount to deposit: ₹");
                    try {
                        BankService.deposit(acc, depositAmt);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    double withdrawAmt = InputHelper.getPositiveDouble("Enter amount to withdraw: ₹");
                    try {
                        BankService.withdraw(acc, withdrawAmt);
                    } catch (InsufficientFundsException e) {
                        System.out.println(e.getMessage());
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    String toAcc = InputHelper.getString("Enter target account number: ");
                    double transferAmt = InputHelper.getPositiveDouble("Enter amount to transfer: ₹");
                    try {
                        BankService.transfer(acc, toAcc, transferAmt);
                    } catch (InsufficientFundsException e) {
                        System.out.println(e.getMessage());
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    TransactionService.printHistory(acc);
                    break;
                case 6:
                    BankService.calculateAndShowInterest(acc);
                    break;
                case 7:
                    System.out.printf("Logged out. Goodbye, %s.%n", acc.getHolderName());
                    return; // breaks the loop and returns to main menu
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
import models.*;
import storage.BankData;
import storage.FileManager;
import utils.InputHelper;
import services.BankService;
import services.TransactionService;
import exceptions.InsufficientFundsException;
import features.ATMMode;
import features.AdminPanel;
import features.FDCalculator;

public class Main {
    public static void main(String[] args) {
        FileManager.loadData();

        while (true) {
            System.out.println("\n===== BANK SYSTEM =====");
            System.out.println("[1] Create Account");
            System.out.println("[2] Login");
            System.out.println("[3] ATM Mode");
            System.out.println("[4] Admin Panel");
            System.out.println("[5] Exit");
            int choice = InputHelper.getInt("Choose an option: ");
            switch (choice) {
                case 1:
                    handleCreateAccount();
                    break;
                case 2:
                    handleLogin();
                    break;
                case 3:
                    ATMMode.launch();
                    break;
                case 4:
                    AdminPanel.launch();
                    break;
                case 5:
                    System.out.println("Goodbye!");
                    FileManager.saveData();
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please enter 1-5.");
            }
        }
    }

    private static void handleCreateAccount() {
        System.out.println("\n--- Create New Account ---");

        String name;
        while (true) {
            name = InputHelper.getString("Enter full name: ");
            if (!name.isBlank()) break;
            System.out.println("Name cannot be blank.");
        }

        int type;
        while (true) {
            type = InputHelper.getInt("Account type: [1] Savings  [2] Current: ");
            if (type == 1 || type == 2) break;
            System.out.println("Please choose 1 or 2.");
        }

        String pin;
        while (true) {
            pin = InputHelper.getString("Create a 4-digit PIN: ");
            if (pin.matches("\\d{4}")) break;
            System.out.println("PIN must be exactly 4 digits (0-9).");
        }

        String confirmPin;
        while (true) {
            confirmPin = InputHelper.getString("Confirm PIN: ");
            if (pin.equals(confirmPin)) break;
            System.out.println("PINs do not match. Please try again.");
        }

        String accNumber = BankData.generateAccountNumber();
        Account newAccount;
        if (type == 1) {
            newAccount = new SavingsAccount(accNumber, name, pin);
        } else {
            newAccount = new CurrentAccount(accNumber, name, pin);
        }

        BankData.addAccount(newAccount);
        FileManager.saveData();                 // <-- persistence
        System.out.println("Account created! Account Number: " + accNumber);
    }

    private static void handleLogin() {
        System.out.println("\n--- Login ---");
        String accNumber = InputHelper.getString("Enter account number: ");
        String pin = InputHelper.getString("Enter PIN: ");

        Account account = BankData.getAccount(accNumber);
        if (account == null) {
            System.out.println("Invalid credentials. Please try again.");
            return;
        }
        if (account.isLocked()) {
            System.out.println("Account " + accNumber + " is locked. Contact admin.");
            return;
        }
        if (!account.validatePin(pin)) {
            account.incrementFailedAttempts();
            if (account.isLocked()) {
                System.out.println("Account locked after 3 failed attempts.");
            } else {
                System.out.println("Invalid credentials. Please try again.");
            }
            FileManager.saveData();             // save lock state
            return;
        }
        account.resetFailedAttempts();
        FileManager.saveData();                 // reset saved as well
        System.out.println("Welcome, " + account.getHolderName() + ".");
        showBankingMenu(account);
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
            System.out.println("7. Monthly Statement");
            System.out.println("8. Fixed Deposit Calculator");
            System.out.println("9. Logout");

            int choice = InputHelper.getInt("Enter choice: ");
            switch (choice) {
                case 1:
                    BankService.checkBalance(acc);
                    break;
                case 2: {
                    double depAmt = InputHelper.getPositiveDouble("Enter amount to deposit: ₹");
                    try {
                        BankService.deposit(acc, depAmt);
                        FileManager.saveData();
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 3: {
                    double wAmt = InputHelper.getPositiveDouble("Enter amount to withdraw: ₹");
                    try {
                        BankService.withdraw(acc, wAmt);
                        FileManager.saveData();
                    } catch (InsufficientFundsException e) {
                        System.out.println(e.getMessage());
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 4: {
                    String toAcc = InputHelper.getString("Enter target account number: ");
                    double tAmt = InputHelper.getPositiveDouble("Enter amount to transfer: ₹");
                    try {
                        BankService.transfer(acc, toAcc, tAmt);
                        FileManager.saveData();
                    } catch (InsufficientFundsException e) {
                        System.out.println(e.getMessage());
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 5:
                    TransactionService.printHistory(acc);
                    break;
                case 6:
                    BankService.calculateAndShowInterest(acc);
                    break;
                case 7: {
                    int month = InputHelper.getInt("Enter month (1-12): ");
                    int year = InputHelper.getInt("Enter year (e.g. 2026): ");
                    if (month < 1 || month > 12) {
                        System.out.println("Invalid month.");
                        break;
                    }
                    BankService.showMonthlyStatement(acc, month, year);
                    break;
                }
                case 8:
                    FDCalculator.launch();
                    break;
                case 9:
                    System.out.printf("Logged out. Goodbye, %s.%n", acc.getHolderName());
                    FileManager.saveData();
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
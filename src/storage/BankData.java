package storage;

import models.Account;
import java.util.HashMap;
import java.util.Collection;

public class BankData {
    // package-private for FileManager (same package)
    static HashMap<String, Account> accounts = new HashMap<>();
    static int lastAccountNumber = 1000;

    private BankData() {}

    public static String generateAccountNumber() {
        lastAccountNumber++;
        return String.valueOf(lastAccountNumber);
    }

    public static void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public static Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public static Collection<Account> getAllAccounts() {
        return accounts.values();
    }

    public static boolean accountExists(String accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    // New public method for deleting an account
    public static void removeAccount(String accountNumber) {
        accounts.remove(accountNumber);
    }
}
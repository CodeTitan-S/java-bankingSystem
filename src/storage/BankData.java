package storage;

import models.Account;
import java.util.HashMap;
import java.util.Collection;

public class BankData {
    private static HashMap<String, Account> accounts = new HashMap<>();
    private static int lastAccountNumber = 1000;

    // Private constructor to prevent instantiation (optional)
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
}
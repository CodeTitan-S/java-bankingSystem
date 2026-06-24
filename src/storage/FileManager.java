package storage;

import models.Account;
import java.io.*;
import java.util.HashMap;

public class FileManager {
    private static final String FILE_PATH = "bank_data.dat";

    // Inner class to hold the complete state as one object
    static class BankSnapshot implements Serializable {
        private static final long serialVersionUID = 1L;
        HashMap<String, Account> accounts;
        int lastId;

        BankSnapshot(HashMap<String, Account> accounts, int lastId) {
            this.accounts = accounts;
            this.lastId = lastId;
        }
    }

    public static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            BankSnapshot snap = new BankSnapshot(BankData.accounts, BankData.lastAccountNumber);
            oos.writeObject(snap);
        } catch (IOException e) {
            System.out.println("Warning: Could not save data.");
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadData() {
        File f = new File(FILE_PATH);
        if (!f.exists()) {
            return; // first run, nothing to load
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            BankSnapshot snap = (BankSnapshot) ois.readObject();
            BankData.accounts = snap.accounts;
            BankData.lastAccountNumber = snap.lastId;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Warning: Could not load data. Starting fresh.");
            BankData.accounts = new HashMap<>();
            BankData.lastAccountNumber = 1000;
        }
    }
}
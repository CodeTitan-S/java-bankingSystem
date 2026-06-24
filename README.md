# Java Console Banking System

A terminal-based banking application built with core Java, demonstrating real-world application of Object-Oriented Programming principles. No frameworks, no GUI — clean architecture from the ground up.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [OOP Concepts Demonstrated](#oop-concepts-demonstrated)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Module Breakdown](#module-breakdown)
- [Class Design](#class-design)
- [Exception Handling](#exception-handling)
- [Data Persistence](#data-persistence)
- [Technologies Used](#technologies-used)

---

## Overview

This project simulates a real-world banking system entirely in the Java console. Users can create Savings or Current accounts, perform transactions, view history, and manage their money — all secured with PIN authentication. An Admin Panel and ATM Mode are included as bonus interfaces.

Built as a portfolio project to demonstrate:

- Clean OOP design with a multi-layer architecture
- Custom exception handling and input validation
- Java Serialization for persistent data storage
- Polymorphism through a Savings/Current account hierarchy

---

## Features

### Core Banking
- Create Savings or Current accounts with auto-generated account numbers
- PIN-based authentication (4-digit, numeric)
- Account lockout after 3 failed login attempts
- Deposit, Withdraw, and Transfer between accounts
- Real-time balance updates with ₹ formatted output
- Full timestamped transaction history
- Interest calculation via polymorphism (4% Savings / 1% Current)
- Monthly statement with deposit/withdrawal totals

### ATM Mode
- Card-style login flow (account number → PIN)
- Cash withdrawal, balance inquiry, deposit
- Mini statement showing last 5 transactions

### Admin Panel
- Password-protected access
- View all accounts with balance and lock status
- Search accounts by name (case-insensitive)
- Delete accounts with confirmation prompt
- View total funds across all accounts
- Unlock locked accounts

### Extras
- Fixed Deposit calculator with compound interest formula
- Loan eligibility estimator
- Data persists across restarts via Java Serialization

---

## Project Structure

```
bank-system/
│
├── Main.java                          # Entry point, menu routing
│
├── models/
│   ├── Account.java                   # Abstract base class (Serializable)
│   ├── SavingsAccount.java            # 4% interest rate
│   ├── CurrentAccount.java            # 1% interest rate
│   └── Transaction.java               # Transaction record (Serializable)
│
├── services/
│   ├── BankService.java               # Core banking logic
│   └── TransactionService.java        # Transaction creation and logging
│
├── storage/
│   ├── BankData.java                  # In-memory HashMap store
│   └── FileManager.java               # Serialization save/load
│
├── features/
│   ├── ATMMode.java                   # ATM simulation interface
│   ├── AdminPanel.java                # Admin operations
│   └── FDCalculator.java              # Fixed deposit calculator
│
├── exceptions/
│   └── InsufficientFundsException.java
│
└── utils/
    ├── InputHelper.java               # Scanner wrapper with validation
    └── DisplayHelper.java             # Formatted console output helpers
```

---

## OOP Concepts Demonstrated

| Concept | Where Applied |
|---|---|
| **Abstraction** | `Account` is abstract — cannot be instantiated directly |
| **Inheritance** | `SavingsAccount` and `CurrentAccount` extend `Account` |
| **Polymorphism** | `calculateInterest()` called via `Account` reference, resolves to correct subclass |
| **Encapsulation** | All fields private, accessed only via getters/setters |
| **Custom Exceptions** | `InsufficientFundsException` for insufficient balance |
| **Collections** | `HashMap` for account store, `ArrayList` for transaction history |
| **Serialization** | Full object graph persisted via `ObjectOutputStream` |
| **Date & Time API** | `LocalDateTime` with `DateTimeFormatter` for timestamps |
| **Service Layer** | Logic separated from UI — `BankService` handles operations, `Main` handles display |

---

## Getting Started

### Prerequisites

- Java JDK 17 or higher
- Any terminal / command prompt

Verify your Java version:

```bash
java -version
```

### Installation

1. Clone the repository:

```bash
git clone https://github.com/your-username/java-banking-system.git
cd java-banking-system
```

2. Compile all Java files:

```bash
javac -d out $(find . -name "*.java")
```

3. Run the application:

```bash
java -cp out Main
```

> On Windows, replace the `find` command with:
> ```cmd
> javac -d out Main.java models\*.java services\*.java storage\*.java features\*.java exceptions\*.java utils\*.java
> ```

---

## Usage

### Main Menu

```
=============================
   JAVA BANKING SYSTEM
=============================
1. Create Account
2. Login
3. ATM Mode
4. Admin Panel
5. Exit
```

### Creating an Account

```
Enter your name: Suhail
Select account type:
  1. Savings Account (4% interest)
  2. Current Account (1% interest)
Choice: 1
Set a 4-digit PIN: ****
Confirm PIN: ****

Account created successfully!
Account Number: 1001
```

### Banking Menu (after login)

```
============================
Welcome, Suhail (Savings)
============================
1. Check Balance
2. Deposit Money
3. Withdraw Money
4. Transfer Money
5. Transaction History
6. Calculate Interest
7. Monthly Statement
8. Fixed Deposit Calculator
9. Logout
```

### Sample Transaction Output

```
₹5000.00 deposited. New balance: ₹5000.00
₹1500.00 withdrawn. Remaining balance: ₹3500.00
₹1000.00 transferred to Account 1002 (Raj).
```

### Transaction History

```
Transaction History — Suhail (1001)
────────────────────────────────────
1. Deposit        ₹5000.00 — 15-06-2025 10:32
2. Withdrawal     ₹1500.00 — 15-06-2025 10:45
3. Transfer Out   ₹1000.00 — 15-06-2025 11:02
```

---

## Module Breakdown

This project was built in three progressive modules:

### Module 1 — Core OOP + Account System
Sets up the full data model and account creation/login flow.

- Abstract `Account` class with concrete subclasses
- `BankData` HashMap store with auto-incremented account numbers
- PIN validation and basic login

### Module 2 — Banking Operations + Services
Implements all 7 banking operations with a clean service layer.

- `BankService` for deposit, withdraw, transfer, interest
- `TransactionService` for logging all activity
- Custom `InsufficientFundsException`
- Full timestamped transaction history

### Module 3 — Persistence + Extra Features
Adds file persistence and bonus features for a complete application.

- Java Serialization via `FileManager` — data survives restarts
- PIN lockout (3 failed attempts) with admin unlock
- ATM simulation mode
- Admin panel with full account management
- Fixed Deposit calculator and monthly statements

---

## Class Design

### `Account` (Abstract)

```java
abstract class Account implements Serializable {
    private String accountNumber;
    private String holderName;
    private double balance;
    private String pin;
    private List<Transaction> transactionHistory;
    private int failedLoginAttempts;
    private boolean locked;

    public abstract double calculateInterest();
    // + deposit(), withdraw(), validatePin(), etc.
}
```

### `SavingsAccount`

```java
class SavingsAccount extends Account {
    @Override
    public double calculateInterest() {
        return getBalance() * 0.04;  // 4% annual
    }
}
```

### `Transaction`

```java
class Transaction implements Serializable {
    private String type;
    private double amount;
    private LocalDateTime dateTime;
}
```

---

## Exception Handling

| Exception | Trigger | Behaviour |
|---|---|---|
| `InsufficientFundsException` | Withdraw/transfer exceeds balance | Caught in `Main`, message printed, operation aborted |
| `IllegalArgumentException` | Amount ≤ 0 entered | Validation loop re-prompts user |
| `IOException` | File save/load failure | Warning printed, app continues without crash |
| `ClassNotFoundException` | Corrupt data file | Warning printed, fresh start initiated |

---

## Data Persistence

Account data is saved to `bank_data.dat` in the project root using Java Serialization.

A `BankSnapshot` object wraps both the accounts `HashMap` and the last-used account number, serialized as a single unit:

```java
// Saving
ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH));
out.writeObject(new BankSnapshot(BankData.accounts, BankData.lastAccountNumber));

// Loading
ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH));
BankSnapshot snap = (BankSnapshot) in.readObject();
```

Saves are triggered automatically after every write operation (create account, deposit, withdraw, transfer).

---

## Technologies Used

- **Java 17** — core language
- **Java Collections Framework** — `HashMap`, `ArrayList`
- **Java I/O & Serialization** — `ObjectOutputStream`, `ObjectInputStream`
- **Java Date & Time API** — `LocalDateTime`, `DateTimeFormatter`
- **Scanner** — console input handling

No external libraries or build tools required.

---
/*. Design a bank account system featuring different types of accounts using inheritance.  */
package practice;

public class file10 {
    private int accountNumber;
    private double balance;
    private String accountHolderName;
    public file10(int accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public void display() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Balance: " + balance);
    }   

    String getAccountHolderName() {
        return accountHolderName;
    }

    Double getBalance() {
        return balance;
    }

    Int getAccountNumber() {
        return accountNumber;
    }

    void deposit(double amount) {
        balance += amount;
        System.out.println("Amount deposited: " + amount);
    }

    void withdraw(double amount) {
        if (amount > balance) {
            System.out.println("Insufficient funds for withdrawal.");
        } else {
            balance -= amount;
            System.out.println("Amount withdrawn: " + amount);
        }
    }

    void acountDetails() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Holder Name: " + accountHolderName);
        System.out.println("Balance: " + balance);
    }
}

class SavingsAccount extends file10 {
    private double interestRate;

    public SavingsAccount(int accountNumber, double balance, double interestRate) {
        super(accountNumber, balance);
        this.interestRate = interestRate;
    }

    void calculateInterest() {
        double interest = getBalance() * (interestRate / 100);
        System.out.println("Interest: " + interest);
    }
}

class CurrentAccount extends file10 {
    private double overdraftLimit;

    public CurrentAccount(int accountNumber, double balance, double overdraftLimit) {
        super(accountNumber, balance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    void withdraw(double amount) {
        if (amount > getBalance() + overdraftLimit) {
            System.out.println("Insufficient funds for withdrawal, including overdraft limit.");
        } else {
            super.withdraw(amount);
        }
    }
}

class BankAccountSystem {
    public static void main(String[] args) {
        SavingsAccount savingsAccount = new SavingsAccount(12345, 1000.0, 5.0);
        CurrentAccount currentAccount = new CurrentAccount(67890, 500.0, 200.0);

        System.out.println("Savings Account Details:");
        savingsAccount.acountDetails();
        savingsAccount.calculateInterest();

        System.out.println("\nCurrent Account Details:");
        currentAccount.acountDetails();
        currentAccount.withdraw(600.0);
    }
}

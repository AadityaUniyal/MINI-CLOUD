package practice;

public class file9 {
    int num;
    double amount;

    file9(int num, double amount) {
        this.num = num;
        this.amount = amount;
    }

    void display() {
        System.out.println("Number: " + num);
        System.out.println("Amount: " + amount);
    }

    void withdraw(double withamount){
        if(withamount>amount){
            System.out.println("Insuffiecient Amount ");
        }
        else{
            amount = amount - withamount;
            System.out.println("Amount after withdrawal: " + amount);
        }
    }

    void deposit(double depoamount){
        amount+=depoamount;
        System.out.println("Amount after deposit: " + amount);
    }
}

class SavingsAccount extends file9{
    SavingsAccount(int num, double amount) {
        super(num, amount);
    }

    void withdraw(double amt){
        if(amt>amount){
            System.out.println("Insufficient Amount in Savings Account");
        }
        else{
            amount = amount - amt;
            System.out.println("Amount after withdrawal from Savings Account: " + amount);
        }
    }
}

class CurrentAccount extends file9{
    CurrentAccount(int num, double amount) {
        super(num, amount);
    }

    void withdraw(double amt){
        if(amt>amount){
            System.out.println("Insufficient Amount in Current Account");
        }
        else{
            amount = amount - amt;
            System.out.println("Amount after withdrawal from Current Account: " + amount);
        }
    }
}

public class BankManagementSystem {
    public static void main(String[] args) {
        SavingsAccount savings = new SavingsAccount(12345, 1000.0);
        CurrentAccount current = new CurrentAccount(67890, 2000.0);

        System.out.println("Savings Account:");
        savings.display();
        savings.withdraw(200);
        savings.deposit(500);

        System.out.println("\nCurrent Account:");
        current.display();
        current.withdraw(300);
        current.deposit(700);
    }
}

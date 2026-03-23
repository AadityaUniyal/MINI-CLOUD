package practice;
import java.util.Scanner;
class Bank{
    double balance;

    Bank(double balance){
        this.balance=balance;
    }
}

class Transaction extends Bank{
    Transaction(double balance){
        super(balance);
    }

    void deposit(double amount){
        balance+=amount;
        System.out.println("Deposited: "+amount);
        System.out.println("Current Balance: "+balance);
    }

    void withdraw(double amount){
        if(balance>=amount){
            balance-=amount;
            System.out.println("Withdrew: "+amount);
        } else {
            System.out.println("Insufficient funds for withdrawal of: "+amount);
        }
    }

    void displayBalance(){
        System.out.println("Current Balance :"+balance);
    }
}

public class Banksytem{
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        Transaction t=new Transaction(1000);
        int choice;
        choice=sc.nextInt();
        double amount;
        System.err.println("Enter 1 for Deposit, 2 for Withdraw, 3 for Display Balance, 0 to Exit");
        switch(choice){
            case 1:
                System.out.println("Enter the amount :");
                amount=sc.nextDouble();
                t.deposit(amount);
                break;
            case 2:
                System.out.println("Enter the amount :");
                amount=sc.nextDouble();
                t.withdraw(amount);
                break;
            case 3:
                t.displayBalance();
                break;
            default:
                System.out.println("Exiting...");
                break;
        }
    }
}

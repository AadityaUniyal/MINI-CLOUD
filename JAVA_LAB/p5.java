//USE a scanner class to take user input,implement a simple bank account sytem with deposit,withdraw.create a class having functions to accept multiple deposits and withdraws and display the balance,deposit money and withdraw money.
import java.util.Scanner;
class BankAccount{
    String name;
    int accnumber;
    double balance;
    Scanner sc=new Scanner(System.in);
    void input(){
        System.out.print("Enter the Name: ");
        name=sc.nextLine();
        System.out.print("Enter Account Number : ");
        accnumber=sc.nextInt();
        System.out.print("Enter the Balance : ");
        balance=sc.nextDouble();
    }

    void deposit(double amount){
        System.out.print("Enter the amount to be deposited : ");
        amount=sc.nextDouble();
        balance+=amount;
        System.out.println("Amount Deposited Successfully.");
        System.out.println("Updated Balance: "+balance);
    }

    void withdraw(double amount){
        System.out.print("Enter the amount to be withdrawn : ");
        amount=sc.nextDouble();
        balance-=amount;
        System.out.println("Amount Withdrawn Successfully.");
        System.out.println("Updated Balance: "+balance);
    }
}

public class p5{
    public static void main(String[] args){
        int n;
        System.out.print("Enter number of account holders: ");
        try(Scanner sc=new Scanner(System.in)){
            n=sc.nextInt();
            BankAccount b[]=new BankAccount[n];
            for(int i=0;i<n;i++){
                b[i]=new BankAccount();
                b[i].input();
            }
            for(int i=0;i<n;i++){
                System.out.println("For Account Holder: "+b[i].name);
                System.out.println("Account Number: "+b[i].accnumber);
                b[i].deposit(0);
                b[i].withdraw(0);
            }
        }
    }
}
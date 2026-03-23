//Use a  scanner  class to take user input, for product slection also calculate the total bill afer shopping create a class having fucntions t oa ccept multile customer data and perform operations like slect items(mobile,hadphones,laptops),choose quantity and get the total bill.
import java.util.Scanner;

public class p6 {
    static class Shpping{
        String name;
        int age;
        String product;
        int quantity;
        double price;
        double bill;
        Scanner sc=new Scanner(System.in);
        void acceptCustomerData(){
            System.out.println("Enter Customer Name:");
            name=sc.nextLine();
            System.out.println("Enter Customer Age:");
            age=sc.nextInt();
            sc.nextLine(); 
        }
        void selectItems(){
            System.out.println("Select Product (mobile/headphones/laptop):");
            product=sc.nextLine().toLowerCase();
            System.out.println("Enter Quantity:");
            quantity=sc.nextInt();
            switch(product){
                case "mobile":
                    price=500.0;
                    break;
                case "headphones":
                    price=150.0;
                    break;
                case "laptop":
                    price=1000.0;
                    break;
                default:
                    System.out.println("Invalid product selected.");
                    price=0;
            }
        }
        void calculateBill(){
            bill=price*quantity;
            System.out.println("Total Bill for " + name + " (age " + age + ") is: $" + bill);
        }
    }

    public static void main(String[] args){
        Shpping shop=new Shpping();
        shop.acceptCustomerData();
        shop.selectItems();
        shop.calculateBill();
    }
}


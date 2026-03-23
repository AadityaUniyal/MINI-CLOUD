//print details of student using array of objects
import java.util.Scanner;
class Student {
    String name;
    int rollNumber;
    float marks;

    void inputDetails(Scanner sc) {
        System.out.print("Enter Name: ");
        name = sc.nextLine();
        System.out.print("Enter Roll Number: ");
        rollNumber = sc.nextInt();
        System.out.print("Enter Marks: ");
        marks = sc.nextFloat();
        sc.nextLine(); 
    }

    void displayDetails() {
        System.out.println("Name: " + name);
        System.out.println("Roll Number: " + rollNumber);
        System.out.println("Marks: " + marks);
    }
}
//Sum of digits of a number
import java.util.Scanner;
public class p3 {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter a number to calculate the sum of its digits: ");
            int num = sc.nextInt();
            int sum = 0;

            while (num != 0) {
                int digit = num % 10;
                sum += digit;
                num /= 10;
            }

            System.out.println("Sum of digits: " + sum);
        }
    }
}
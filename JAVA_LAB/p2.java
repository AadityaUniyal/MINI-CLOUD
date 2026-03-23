//Palindrome number
import java.util.Scanner;
public class p2 {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter a number to check if it's a palindrome: ");
            int num = sc.nextInt();
            int originalNum = num;
            int reversed = 0;

            while (num != 0) {
                int digit = num % 10;
                reversed = reversed * 10 + digit;
                num /= 10;
            }

            if (originalNum == reversed) {
                System.out.println(originalNum + " is a palindrome.");
            } else {
                System.out.println(originalNum + " is not a palindrome.");
            }
        }
    }
}
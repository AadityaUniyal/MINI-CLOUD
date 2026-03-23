/* Program to find if the given numbers are Friendly pair or not (Amicable or not). Friendly Pair are two or more numbers with a common abundance. Input & Output format: • Input consists of 2 integers. • The first integer corresponds to number 1 and the second integer corresponds to number 2. If it is a Friendly Pair display Friendly Pair or displays Not Friendly Pair.  */
package practice;

public class file2 {
    void check(int num1, int num2) {
        int sum1 = 0, sum2 = 0;
        for (int i = 1; i < num1; i++) {
            if (num1 % i == 0) {
                sum1 += i;
            }
        }
        for (int j = 1; j < num2; j++) {
            if (num2 % j == 0) {
                sum2 += j;
            }
        }
        if (sum1 == num2 && sum2 == num1) {
            System.out.println("Friendly Pair");
        } else {
            System.out.println("Not Friendly Pair");
        }
    }
    
    public static void main(String[] args) {
        file2 obj = new file2();
        obj.check(220, 284);
    }
    
}

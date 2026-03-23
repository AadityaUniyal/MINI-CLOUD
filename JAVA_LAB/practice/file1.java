/* Design a command-line calculator that can perform basic arithmetic operations (+, -, *, /) on two numbers. The twist: it should be able to handle both integer and floating-point inputs, and if a non-numeric argument is provided, it should gracefully inform the user. The program should use Wrapper Classes for internal number representation and Autoboxing/Unboxing during calculations.  */
package practice;

public class file1 {
    public static void main(String[] args) {
        if(args.length!=3){
            System.out.println("Not proper");
            return;
        }
        String a=args[0];
        String b=args[1];
        String op=args[2];
        Double num1,num2;
        try{
            num1=Double.parseDouble(a);
            num2=Double.parseDouble(b);
        }catch(NumberFormatException e){
            System.out.println("Not a number");
            return;
        }
        Double result;
        switch(op){
            case "+":
                result=num1+num2;
                break;
            case "-":
                result=num1-num2;
                break;
            case "*":
                result=num1*num2;
                break;
            case "/":
                if(num2==0){
                    System.out.println("Cannot divide by zero");
                    return;
                }
                result=num1/num2;
                break;
            default:
                System.out.println("Invalid operator");
                return;
        }
        System.out.println("Result: " + result);
    }
    
}

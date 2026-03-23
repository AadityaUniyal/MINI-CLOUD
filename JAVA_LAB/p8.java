//user type a string. we have to count the number of palondromic words in that string

import java.util.Scanner;

public class p8 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a string: ");
        String str = sc.nextLine();
        String[] words = str.split("\\s+");
        int count = 0;
        for (String word : words) {
            if (isPalindrome(word)) {
                count++;
            }
        }
        System.out.println("Number of palindromic words: " + count);
    }

    public static boolean isPalindrome(String word) {
        String wordlower=word.toLowerCase();
        int l=0;
        int r=wordlower.length()-1;
        while(l<r){
            if(wordlower.charAt(l)!=wordlower.charAt(r)){
                return false;
            }
            l++;
            r--;
        }
        return true;
    }
}
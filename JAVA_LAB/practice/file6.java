/* Palindrome Checker and Analyzer for Large Text. Create a program that analyzes a given large text (e.g., a paragraph or multiple sentences) to identify all palindromic words and provide an analysis. A palindrome is a word that reads the same forwards and backwards (e.g., "madam", "level", "refer"). The program should ignore case and non alphabetic characters for palindrome checking. */
package practice;
import java.util.Scanner;
public class file6 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        String text = sc.nextLine();
        // split on whitespace (one or more) -- note escaping the backslash
        String[] words = text.split("\\s+");

        System.out.println("Palindromic words in the given text:");
        for (String word : words) {
            String cleanedWord = word.replaceAll("[^a-zA-Z]", "").toLowerCase();
            if (cleanedWord.length() > 0 && isPalindrome(cleanedWord)) {
                System.out.println(word);
            }
        }

    }
    public static boolean isPalindrome(String word){
        int left=0;
        int right=word.length()-1;
        while(left<right){
            if(word.charAt(left)!=word.charAt(right)){
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
    
}

/* The problem to rearrange positive and negative numbers in an array . Method: This approach moves all negative numbers to the beginning and positive numbers to the end but changes the order of appearance of the elements of the array. Steps: 1. Declare an array and input the array elements. 2. Start traversing the array and if the current element is negative, swap the current element with the first positive element and continue traversing until all the elements have been encountered. 3. Print the rearranged array.  */
package practice;

import java.util.Scanner;

public class file4 {
    public void rearrange(int a[],int n){
        int j=0;
        for(int i=0;i<n;i++){
            if(a[i]<0){
                if(i!=j){
                    int temp=a[i];
                    a[i]=a[j];
                    a[j]=temp;
                }
                j++;
            }
        }
    }
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of elements: ");
        int n = sc.nextInt();
        int[] arr = new int[n];
        System.out.println("Enter " + n + " integers (positive/negative):");
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        
        file4 obj = new file4();
        obj.rearrange(arr, n);
        
        System.out.println("Rearranged array:");
        for (int i = 0; i < n; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
        sc.close();
    }
}

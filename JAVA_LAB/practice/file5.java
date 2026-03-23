/* Program to find the saddle point coordinates in a given matrix. A saddle point is an element of the matrix, which is the minimum element in its row and the maximum in its column.  */
package practice;

public class file5 {
    public void findSaddlePoint(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        
        for (int i = 0; i < rows; i++) {
            int minRowElement = matrix[i][0];
            int minColIndex = 0;
            
            // Find the minimum element in the current row
            for (int j = 1; j < cols; j++) {
                if (matrix[i][j] < minRowElement) {
                    minRowElement = matrix[i][j];
                    minColIndex = j;
                }
            }
            
            // Check if the minimum element is the maximum in its column
            boolean isSaddlePoint = true;
            for (int k = 0; k < rows; k++) {
                if (matrix[k][minColIndex] > minRowElement) {
                    isSaddlePoint = false;
                    break;
                }
            }
            
            if (isSaddlePoint) {
                System.out.println("Saddle point found at coordinates: (" + i + ", " + minColIndex + ")");
                return;
            }
        }
        
        System.out.println("No saddle point found in the matrix.");
    }
    
    public static void main(String[] args) {
        int[][] matrix = {
            {3, 8, 7},
            {5, 6, 9},
            {1, 4, 2}
        };
        
        file5 obj = new file5();
        obj.findSaddlePoint(matrix);
    }
    
}

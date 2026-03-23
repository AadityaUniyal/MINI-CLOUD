/* Design a Java program that can perform a simple run-length encoding (RLE)  for text compression and its corresponding decompression. Example: "AAABBC" becomes "A3B2C1".   */
package practice;

public class file7 {
    public static void main(String[] args) {
        String input = "AAABBC";
        String encoded = encode(input);
        System.out.println("Encoded: " + encoded);
        String decoded = decode(encoded);
        System.out.println("Decoded: " + decoded);
    }

    public static String encode(String input) {
        StringBuilder encoded = new StringBuilder();
        int count = 1;
        for (int i = 1; i < input.length(); i++) {
            if (input.charAt(i) == input.charAt(i - 1)) {
                count++;
            } else {
                encoded.append(input.charAt(i - 1)).append(count);
                count = 1;
            }
        }
        // Append the last character and its count
        encoded.append(input.charAt(input.length() - 1)).append(count);
        return encoded.toString();
    }

    public static String decode(String encoded) {
        StringBuilder decoded = new StringBuilder();
        for (int i = 0; i < encoded.length(); i += 2) {
            char character = encoded.charAt(i);
            int count = Character.getNumericValue(encoded.charAt(i + 1));
            for (int j = 0; j < count; j++) {
                decoded.append(character);
            }
        }
        return decoded.toString();
    }
    
}

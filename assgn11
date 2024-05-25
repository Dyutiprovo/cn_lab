public class CRC {

    // Function to convert decimal to binary string
    private static String decimalToBinary(int decimal) {
        return Integer.toBinaryString(decimal);
    }

    // Function to perform CRC
    private static String performCRC(int data, int divisor) {
        String binaryData = decimalToBinary(data);
        String binaryDivisor = decimalToBinary(divisor);

        int dataLength = binaryData.length();
        int divisorLength = binaryDivisor.length();

        // Appending zeros to data to make it same length as divisor
        StringBuilder dataBuilder = new StringBuilder(binaryData);
        for (int i = 0; i < divisorLength - 1; i++) {
            dataBuilder.append("0");
        }

        binaryData = dataBuilder.toString();

        // Performing CRC
        StringBuilder result = new StringBuilder();
        String remainder = binaryData.substring(0, divisorLength);
        for (int i = 0; i < dataLength; i++) {
            result.append(remainder.charAt(0));
            if (remainder.charAt(0) == '1') {
                remainder = xor(remainder, binaryDivisor);
            }
            if (i == dataLength - 1) {
                break;
            }
            remainder = remainder.substring(1) + binaryData.charAt(divisorLength + i);
        }

        return result.toString();
    }

    // Function to perform XOR operation
    private static String xor(String a, String b) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            result.append(a.charAt(i) == b.charAt(i) ? "0" : "1");
        }
        return result.toString();
    }

    public static void main(String[] args) {
        int data = 123; // Example decimal input
        int divisor = 13; // Example divisor

        // Performing CRC
        String crc = performCRC(data, divisor);
        System.out.println("CRC of " + data + " is: " + crc);
    }
}

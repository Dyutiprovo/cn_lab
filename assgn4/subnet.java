import java.util.Scanner;

class IPAddressSubnetCalculator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Enter the IP address (e.g., 192.168.1.1): ");
        String ipAddress = scanner.nextLine();
        
        System.out.println("Enter the subnet mask (e.g., 255.255.255.0): ");
        String subnetMask = scanner.nextLine();
        
        String binaryIPAddress = toBinaryString(ipAddress);
        String binarySubnetMask = toBinaryString(subnetMask);
        
        String binarySubnetAddress = calculateSubnetAddress(binaryIPAddress, binarySubnetMask);
        String subnetAddress = binaryToDecimal(binarySubnetAddress);
        
        System.out.println("IP Address (binary): " + binaryIPAddress);
        System.out.println("Subnet Mask (binary): " + binarySubnetMask);
        System.out.println("Subnet Address (binary): " + binarySubnetAddress);
        
        System.out.println("Subnet Address (decimal): " + subnetAddress);
        System.out.println("Subnet Mask (decimal): " + subnetMask);
    }

    private static String toBinaryString(String ipAddress) {
        String[] octets = ipAddress.split("\\.");
        StringBuilder binaryString = new StringBuilder();
        for (String octet : octets) {
            int intValue = Integer.parseInt(octet);
            String binaryOctet = String.format("%8s", Integer.toBinaryString(intValue)).replace(' ', '0');
            binaryString.append(binaryOctet);
        }
        return binaryString.toString();
    }

    private static String calculateSubnetAddress(String binaryIPAddress, String binarySubnetMask) {
        StringBuilder binarySubnetAddress = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            binarySubnetAddress.append(binaryIPAddress.charAt(i) == '1' && binarySubnetMask.charAt(i) == '1' ? '1' : '0');
        }
        return binarySubnetAddress.toString();
    }

    private static String binaryToDecimal(String binaryAddress) {
        StringBuilder decimalAddress = new StringBuilder();
        for (int i = 0; i < 32; i += 8) {
            String byteSegment = binaryAddress.substring(i, i + 8);
            int decimalSegment = Integer.parseInt(byteSegment, 2);
            decimalAddress.append(decimalSegment).append(".");
        }
        return decimalAddress.substring(0, decimalAddress.length() - 1); // Remove trailing dot
    }
}

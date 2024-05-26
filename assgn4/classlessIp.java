import java.util.Scanner;

public class CIDRNetAndHostID {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the IP address (e.g., 192.168.1.1): ");
        String ipAddress = scanner.nextLine();

        System.out.println("Enter the prefix length (e.g., 24): ");
        int prefixLength = scanner.nextInt();

        String binaryIPAddress = toBinaryString(ipAddress);
        String binarySubnetMask = createSubnetMask(prefixLength);
        String binaryNetworkID = calculateNetworkID(binaryIPAddress, binarySubnetMask);
        String binaryHostID = calculateHostID(binaryIPAddress, binarySubnetMask);

        String networkID = binaryToDecimal(binaryNetworkID);
        String hostID = binaryToDecimal(binaryHostID);
        String subnetMask = binaryToDecimal(binarySubnetMask);

        System.out.println("IP Address (binary): " + binaryIPAddress);
        System.out.println("Subnet Mask (binary): " + binarySubnetMask);
        System.out.println("Network ID (binary): " + binaryNetworkID);
        System.out.println("Host ID (binary): " + binaryHostID);

        System.out.println("Network ID (decimal): " + networkID);
        System.out.println("Host ID (decimal): " + hostID);
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

    private static String createSubnetMask(int prefixLength) {
        StringBuilder subnetMask = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            subnetMask.append(i < prefixLength ? '1' : '0');
        }
        return subnetMask.toString();
    }

    private static String calculateNetworkID(String binaryIPAddress, String binarySubnetMask) {
        StringBuilder binaryNetworkID = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            binaryNetworkID.append(binaryIPAddress.charAt(i) == '1' && binarySubnetMask.charAt(i) == '1' ? '1' : '0');
        }
        return binaryNetworkID.toString();
    }

    private static String calculateHostID(String binaryIPAddress, String binarySubnetMask) {
        StringBuilder binaryHostID = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            binaryHostID.append(binarySubnetMask.charAt(i) == '0' ? binaryIPAddress.charAt(i) : '0');
        }
        return binaryHostID.toString();
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

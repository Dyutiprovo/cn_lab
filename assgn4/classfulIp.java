import java.util.Scanner;

class BinaryIPAddress {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the IP address in binary form (e.g., 11000000101010000000000100000001 for 192.168.1.1): ");
        String binaryIP = scanner.nextLine();

        if (binaryIP.length() != 32) {
            System.out.println("Invalid IP address. Please enter a 32-bit binary IP address.");
            return;
        }

        String decimalIP = binaryToDecimal(binaryIP);
        System.out.println("Decimal IP Address: " + decimalIP);

        char ipClass = findIPClass(decimalIP);
        System.out.println("IP Class: " + ipClass);

        String subnetID = getSubnetID(decimalIP, ipClass);
        String hostID = getHostID(decimalIP, ipClass);

        System.out.println("Subnet ID: " + subnetID);
        System.out.println("Host ID: " + hostID);
    }

    private static String binaryToDecimal(String binaryIP) {
        StringBuilder decimalIP = new StringBuilder();
        for (int i = 0; i < 32; i += 8) {
            String byteSegment = binaryIP.substring(i, i + 8);
            int decimalSegment = Integer.parseInt(byteSegment, 2);
            decimalIP.append(decimalSegment).append(".");
        }
        // Remove the trailing dot
        return decimalIP.substring(0, decimalIP.length() - 1);
    }

    private static char findIPClass(String decimalIP) {
        int firstOctet = Integer.parseInt(decimalIP.split("\\.")[0]);
        if (firstOctet >= 1 && firstOctet <= 126) {
            return 'A';
        } else if (firstOctet >= 128 && firstOctet <= 191) {
            return 'B';
        } else if (firstOctet >= 192 && firstOctet <= 223) {
            return 'C';
        } else if (firstOctet >= 224 && firstOctet <= 239) {
            return 'D';  // Multicast (not typically used for subnetting)
        } else if (firstOctet >= 240 && firstOctet <= 255) {
            return 'E';  // Reserved (not used for normal addressing)
        } else {
            return 'X';  // Undefined class (shouldn't occur in this context)
        }
    }

    private static String getSubnetID(String decimalIP, char ipClass) {
        String[] octets = decimalIP.split("\\.");
        switch (ipClass) {
            case 'A':
                return octets[0] + ".0.0.0";
            case 'B':
                return octets[0] + "." + octets[1] + ".0.0";
            case 'C':
                return octets[0] + "." + octets[1] + "." + octets[2] + ".0";
            default:
                return "N/A";
        }
    }

    private static String getHostID(String decimalIP, char ipClass) {
        String[] octets = decimalIP.split("\\.");
        switch (ipClass) {
            case 'A':
                return "0." + octets[1] + "." + octets[2] + "." + octets[3];
            case 'B':
                return "0.0." + octets[2] + "." + octets[3];
            case 'C':
                return "0.0.0." + octets[3];
            default:
                return "N/A";
        }
    }
}

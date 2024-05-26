import java.util.Scanner;

public class NetworkHostCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input the IP address with CIDR notation
        System.out.print("Enter the IP address with CIDR notation (e.g., 192.168.1.1/24): ");
        String input = scanner.nextLine();

        // Split the IP address and CIDR prefix length
        String[] parts = input.split("/");
        String ipAddress = parts[0];
        int prefixLength = Integer.parseInt(parts[1]);

        // Convert IP address to integer array
        int[] ip = convertToIntArray(ipAddress);

        // Calculate subnet mask from prefix length
        int[] mask = new int[4];
        for (int i = 0; i < 4; i++) {
            if (prefixLength >= 8) {
                mask[i] = 255;
                prefixLength -= 8;
            } else {
                mask[i] = (int)(256 - Math.pow(2, 8 - prefixLength));
                prefixLength = 0;
            }
        }

        // Calculate network ID
        int[] networkID = new int[4];
        for (int i = 0; i < 4; i++) {
            networkID[i] = ip[i] & mask[i];
        }

        // Calculate host ID
        int[] hostID = new int[4];
        for (int i = 0; i < 4; i++) {
            hostID[i] = ip[i] & ~mask[i];
        }

        // Display results
        System.out.println("IP Address: " + ipAddress);
        System.out.println("Subnet Mask: " + convertToString(mask));
        System.out.println("Network ID: " + convertToString(networkID));
        System.out.println("Host ID: " + convertToString(hostID));
    }

    // Convert dotted decimal string to integer array
    private static int[] convertToIntArray(String str) {
        String[] parts = str.split("\\.");
        int[] result = new int[4];
        for (int i = 0; i < 4; i++) {
            result[i] = Integer.parseInt(parts[i]);
        }
        return result;
    }

    // Convert integer array to dotted decimal string
    private static String convertToString(int[] array) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            result.append(array[i]);
            if (i < 3) {
                result.append(".");
            }
        }
        return result.toString();
    }
}

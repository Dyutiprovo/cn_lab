import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

class Getbyname {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the hostname of the machine: ");
        String hostname = scanner.nextLine();

        try {
            // Get the InetAddress object for the given hostname
            InetAddress inetAddress = InetAddress.getByName(hostname);

            // Get the IP address from the InetAddress object
            String ipAddress = inetAddress.getHostAddress();

            System.out.println("IP address of the machine " + hostname + " is: " + ipAddress);
        } catch (UnknownHostException e) {
            System.err.println("Unable to resolve hostname: " + e.getMessage());
        }
    }
}















import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

class Getbynameurl {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the URL (e.g., www.example.com): ");
        String url = scanner.nextLine();

        try {
            // Remove the protocol part if it exists
            if (url.startsWith("http://")) {
                url = url.substring(7);
            } else if (url.startsWith("https://")) {
                url = url.substring(8);
            }

            // Get the InetAddress object for the given URL
            InetAddress inetAddress = InetAddress.getByName(url);

            // Get the IP address from the InetAddress object
            String ipAddress = inetAddress.getHostAddress();

            System.out.println("IP address of the URL " + url + " is: " + ipAddress);
        } catch (UnknownHostException e) {
            System.err.println("Unable to resolve URL: " + e.getMessage());
        }
    }
}

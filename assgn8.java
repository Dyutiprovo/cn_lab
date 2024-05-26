//server multicast group
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {

    public static void main(String[] args) {
        String hostname = "localhost"; // Server's hostname
        int port = 12345; // Server's port

        try (Socket socket = new Socket(hostname, port)) {
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in);

            String userInput;
            System.out.println("Enter messages to send to the server (type 'exit' to quit):");
            while (true) {
                System.out.print("> ");
                userInput = scanner.nextLine();
                if ("exit".equalsIgnoreCase(userInput)) {
                    break;
                }
                output.println(userInput);
                String serverResponse = input.readLine();
                System.out.println(serverResponse);
            }
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }
}

//client multicast group
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastClient {

    public static void main(String[] args) {
        String multicastGroup = "230.0.0.1"; // Multicast group address
        int port = 12345; // Multicast port

        try (MulticastSocket multicastSocket = new MulticastSocket(port)) {
            InetAddress group = InetAddress.getByName(multicastGroup);

            // Join the multicast group
            multicastSocket.joinGroup(group);

            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            // Receive the packet
            multicastSocket.receive(packet);

            // Convert the packet data to a string
            String message = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Received message from multicast group: " + message);
        } catch (IOException e) {
            System.out.println("Error receiving multicast message: " + e.getMessage());
        }
    }
}

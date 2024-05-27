//server upper
import java.io.*;
import java.net.*;

public class UppercaseServer {
    public static void main(String[] args) {
        int port = 12345;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                try (DataInputStream input = new DataInputStream(socket.getInputStream());
                     DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

                    String message = input.readUTF();
                    System.out.println("Received: " + message);

                    String uppercaseMessage = message.toUpperCase();
                    output.writeUTF(uppercaseMessage);  // Send the uppercase message back to the client
                } catch (IOException ex) {
                    System.out.println("Client disconnected");
                }
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}


//client upper
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class UppercaseClient {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 12345;

        try (Socket socket = new Socket(hostname, port);
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             DataInputStream input = new DataInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.print("Enter message: ");
            String message = scanner.nextLine();

            output.writeUTF(message);  // Send message to the server

            String uppercaseMessage = input.readUTF();  // Read uppercase message from the server
            System.out.println("Uppercase from server: " + uppercaseMessage);

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}

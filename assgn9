//server upper
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class StringProcessingServer {

    public static void main(String[] args) {
        int port = 12345; // Server will listen on this port

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("New client connected");

                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

                    String clientMessage = input.readLine();
                    System.out.println("Received from client: " + clientMessage);

                    String response = clientMessage.toUpperCase();
                    output.println(response);
                } catch (IOException e) {
                    System.out.println("Error handling client connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error starting server: " + e.getMessage());
        }
    }
}

//client upper
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class StringProcessingClient {

    public static void main(String[] args) {
        String hostname = "localhost"; // Server's hostname
        int port = 12345; // Server's port

        try (Socket socket = new Socket(hostname, port)) {
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter a string to send to the server:");
            String userInput = scanner.nextLine();
            output.println(userInput);

            String serverResponse = input.readLine();
            System.out.println("Server response: " + serverResponse);
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }
}

import java.io.*;
import java.util.*;

public class StopAndWaitProtocol {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Simulating packet transmission
        List<String> packetsToSend = Arrays.asList("Packet 0", "Packet 1", "Packet 2", "Packet 3", "Packet 4");

        for (String packet : packetsToSend) {
            System.out.println("Sending packet: " + packet);
            sendPacket(packet);

            // Simulate delay before receiving acknowledgment
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Simulate receiving acknowledgment
            boolean ackReceived = receiveAcknowledgment();
            if (ackReceived) {
                System.out.println("Acknowledgment received for: " + packet);
            } else {
                System.out.println("Timeout! Resending packet: " + packet);
                sendPacket(packet); // Resend packet
            }
        }

        scanner.close();
    }

    // Simulate sending packet
    private static void sendPacket(String packet) {
        // Simulate sending packet over network
    }

    // Simulate receiving acknowledgment
    private static boolean receiveAcknowledgment() {
        // Simulate receiving acknowledgment over network
        Random random = new Random();
        return random.nextBoolean(); // Simulate random success/failure
    }
}

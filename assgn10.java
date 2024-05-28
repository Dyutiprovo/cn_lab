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


//underpowerversion
import java.util.Scanner;

public class StopAndWaitSlidingWindow {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of frames: ");
        int numOfFrames = scanner.nextInt();
        
        System.out.print("Enter the window size: ");
        int windowSize = scanner.nextInt();
        
        int[] frames = new int[numOfFrames];
        
        for (int i = 0; i < numOfFrames; i++) {
            frames[i] = i + 1;
        }
        
        System.out.println("\nStarting Stop-and-Wait Sliding Window Protocol...");
        
        int currentFrame = 0;
        
        while (currentFrame < numOfFrames) {
            for (int i = 0; i < windowSize && currentFrame < numOfFrames; i++) {
                System.out.println("Sending frame: " + frames[currentFrame]);
                System.out.println("Acknowledgment received for frame: " + frames[currentFrame]);
                currentFrame++;
            }
        }
        
        System.out.println("All frames have been sent and acknowledged.");
        scanner.close();
    }
}

//server multicast group
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastServer {
    public static void main(String[] args) {
        String multicastAddress = "224.0.0.1";
        int port = 12345;

        try (MulticastSocket socket = new MulticastSocket()) {
            InetAddress group = InetAddress.getByName(multicastAddress);
            String message = "Hello, multicast clients!";
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), group, port);

            socket.send(packet);
            System.out.println("Message sent to multicast group");

        } catch (IOException ex) {
            System.out.println("Multicast server error: " + ex.getMessage());
            ex.printStackTrace();
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
        String multicastAddress = "224.0.0.1";
        int port = 12345;

        try (MulticastSocket socket = new MulticastSocket(port)) {
            InetAddress group = InetAddress.getByName(multicastAddress);
            socket.joinGroup(group);
            System.out.println("Joined multicast group");

            byte[] buffer = new byte[256];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            socket.receive(packet);
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Received message: " + received);

            socket.leaveGroup(group);

        } catch (IOException ex) {
            System.out.println("Multicast client error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

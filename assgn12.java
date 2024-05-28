//packet code
import java.io.Serializable;

public class Packet implements Serializable {
    private int seqNum;
    private String data;

    public Packet(int seqNum, String data) {
        this.seqNum = seqNum;
        this.data = data;
    }

    public int getSeqNum() {
        return seqNum;
    }

    public String getData() {
        return data;
    }
}

//selective repeat arq
//sender
import java.io.*;
import java.net.*;
import java.util.Random;

public class Sender {
    private static final int WINDOW_SIZE = 4;
    private static final int TOTAL_PACKETS = 10;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        InetAddress receiverAddress = InetAddress.getByName("localhost");
        int receiverPort = 12345;
        DatagramSocket socket = new DatagramSocket();
        int base = 0;
        int nextSeqNum = 0;
        Packet[] window = new Packet[WINDOW_SIZE];
        boolean[] ackReceived = new boolean[WINDOW_SIZE];
        Random random = new Random();

        while (base < TOTAL_PACKETS) {
            while (nextSeqNum < base + WINDOW_SIZE && nextSeqNum < TOTAL_PACKETS) {
                String data = "Packet " + nextSeqNum;
                Packet packet = new Packet(nextSeqNum, data);
                window[nextSeqNum % WINDOW_SIZE] = packet;

                byte[] sendData = serialize(packet);
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receiverAddress, receiverPort);
                socket.send(sendPacket);
                System.out.println("Sent: " + data);

                nextSeqNum++;
            }

            socket.setSoTimeout(1000);
            try {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                Packet ack = (Packet) deserialize(receivePacket.getData());
                System.out.println("Received ACK for packet " + ack.getSeqNum());
                ackReceived[ack.getSeqNum() % WINDOW_SIZE] = true;

                while (ackReceived[base % WINDOW_SIZE]) {
                    ackReceived[base % WINDOW_SIZE] = false;
                    base++;
                }
            } catch (SocketTimeoutException e) {
                for (int i = base; i < nextSeqNum; i++) {
                    if (!ackReceived[i % WINDOW_SIZE]) {
                        Packet packet = window[i % WINDOW_SIZE];
                        byte[] sendData = serialize(packet);
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receiverAddress, receiverPort);
                        socket.send(sendPacket);
                        System.out.println("Resent: " + packet.getData());
                    }
                }
            }
        }
        socket.close();
    }

    private static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(obj);
        return byteStream.toByteArray();
    }

    private static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objStream = new ObjectInputStream(byteStream);
        return objStream.readObject();
    }
}

//receiver code
import java.io.*;
import java.net.*;

public class Receiver {
    private static final int WINDOW_SIZE = 4;
    private static final int TOTAL_PACKETS = 10;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int receiverPort = 12345;
        DatagramSocket socket = new DatagramSocket(receiverPort);
        boolean[] received = new boolean[TOTAL_PACKETS];
        int base = 0;

        while (base < TOTAL_PACKETS) {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            Packet packet = (Packet) deserialize(receivePacket.getData());
            System.out.println("Received: " + packet.getData());

            int seqNum = packet.getSeqNum();
            if (seqNum >= base && seqNum < base + WINDOW_SIZE) {
                received[seqNum] = true;

                Packet ack = new Packet(seqNum, "ACK");
                byte[] ackData = serialize(ack);
                DatagramPacket ackPacket = new DatagramPacket(ackData, ackData.length, receivePacket.getAddress(), receivePacket.getPort());
                socket.send(ackPacket);
                System.out.println("Sent ACK for packet " + seqNum);

                while (base < TOTAL_PACKETS && received[base]) {
                    base++;
                }
            }
        }
        socket.close();
    }

    private static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(obj);
        return byteStream.toByteArray();
    }

    private static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objStream = new ObjectInputStream(byteStream);
        return objStream.readObject();
    }
}

//go back n arq
//sender code
import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

public class GoBackNSender {
    private static final int WINDOW_SIZE = 4;
    private static final int TOTAL_PACKETS = 10;
    private static final int TIMEOUT = 3000;

    private static Timer timer = new Timer();
    private static int base = 0;
    private static int nextSeqNum = 0;
    private static Packet[] window = new Packet[WINDOW_SIZE];

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        InetAddress receiverAddress = InetAddress.getByName("localhost");
        int receiverPort = 12345;
        DatagramSocket socket = new DatagramSocket();

        while (base < TOTAL_PACKETS) {
            if (nextSeqNum < base + WINDOW_SIZE && nextSeqNum < TOTAL_PACKETS) {
                String data = "Packet " + nextSeqNum;
                Packet packet = new Packet(nextSeqNum, data);
                window[nextSeqNum % WINDOW_SIZE] = packet;

                byte[] sendData = serialize(packet);
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receiverAddress, receiverPort);
                socket.send(sendPacket);
                System.out.println("Sent: " + data);

                if (base == nextSeqNum) {
                    startTimer(socket, receiverAddress, receiverPort);
                }

                nextSeqNum++;
            }

            socket.setSoTimeout(1000);
            try {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                Packet ack = (Packet) deserialize(receivePacket.getData());
                System.out.println("Received ACK for packet " + ack.getSeqNum());

                base = ack.getSeqNum() + 1;
                if (base == nextSeqNum) {
                    timer.cancel();
                } else {
                    startTimer(socket, receiverAddress, receiverPort);
                }
            } catch (SocketTimeoutException e) {
                // Timeout, nothing to do
            }
        }
        socket.close();
    }

    private static void startTimer(DatagramSocket socket, InetAddress receiverAddress, int receiverPort) {
        timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Timeout, resending packets from " + base);
                for (int i = base; i < nextSeqNum; i++) {
                    try {
                        Packet packet = window[i % WINDOW_SIZE];
                        byte[] sendData = serialize(packet);
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receiverAddress, receiverPort);
                        socket.send(sendPacket);
                        System.out.println("Resent: " + packet.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, TIMEOUT);
    }

    private static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(obj);
        return byteStream.toByteArray();
    }

    private static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objStream = new ObjectInputStream(byteStream);
        return objStream.readObject();
    }
}

//receiver code
import java.io.*;
import java.net.*;

public class GoBackNReceiver {
    private static final int TOTAL_PACKETS = 10;
    private static int expectedSeqNum = 0;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int receiverPort = 12345;
        DatagramSocket socket = new DatagramSocket(receiverPort);

        while (expectedSeqNum < TOTAL_PACKETS) {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            Packet packet = (Packet) deserialize(receivePacket.getData());
            System.out.println("Received: " + packet.getData());

            if (packet.getSeqNum() == expectedSeqNum) {
                expectedSeqNum++;
            }

            Packet ack = new Packet(expectedSeqNum - 1, "ACK");
            byte[] ackData = serialize(ack);
            DatagramPacket ackPacket = new DatagramPacket(ackData, ackData.length, receivePacket.getAddress(), receivePacket.getPort());
            socket.send(ackPacket);
            System.out.println("Sent ACK for packet " + (expectedSeqNum - 1));
        }
        socket.close();
    }

    private static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(obj);
        return byteStream.toByteArray();
    }

    private static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objStream = new ObjectInputStream(byteStream);
        return objStream.readObject();
    }
}

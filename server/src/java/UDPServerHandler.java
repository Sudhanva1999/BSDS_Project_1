package server.src.java;

import resources.SimpleLogger;
import java.io.IOException;
import java.net.*;

public class UDPServerHandler {
    private int port;
    private KeyStore keyStore;

    public UDPServerHandler(int port, KeyStore keyValueStore) {
        this.port = port;
        this.keyStore = keyValueStore;
    }

    public void start() {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            SimpleLogger.log("UDP Server started on port " + port);

            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                socket.receive(packet);
                String request = new String(packet.getData(), 0, packet.getLength());
                SimpleLogger.log("Received UDP request from " + packet.getAddress() + ":" + packet.getPort());

                String response = handleRequest(request);
                byte[] responseBytes = response.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(responseBytes, responseBytes.length, packet.getAddress(), packet.getPort());
                socket.send(responsePacket);
                SimpleLogger.log("Sent UDP response: " + response);
            }
        } catch (IOException e) {
            SimpleLogger.logError("UDP server encountered an error: " + e.getMessage());
        }
    }

    private String handleRequest(String request) {
        // Handle PUT/GET/DELETE and return the response.
        return "Request handled"; // Placeholder for actual handling logic
    }
}

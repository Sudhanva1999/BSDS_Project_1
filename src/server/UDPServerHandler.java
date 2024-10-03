package src.server;

import java.io.IOException;
import java.net.*;

/**
 * UDPServerHandler class that handles incoming UDP packets and processes requests.
 * It extends the ServerHandler abstract class to manage key-value operations.
 */
public class UDPServerHandler extends ServerHandler {

    /**
     * Constructs a UDPServerHandler with the specified port and KeyStore.
     *
     * @param port          The port number the server will listen on.
     * @param keyValueStore The KeyStore instance to handle key-value operations.
     */
    public UDPServerHandler(int port, KeyStore keyValueStore) {
        super(port, keyValueStore);
    }

    /**
     * Starts the UDP server, receiving client requests and sending responses.
     * This method runs in a loop, processing incoming UDP packets.
     */
    public void start() {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            SimpleLogger.log("UDP Server started on port " + port);

            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                socket.receive(packet);
                String request = new String(packet.getData(), 0, packet.getLength());
                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();
                
                SimpleLogger.log("Received UDP request from IP: " + clientAddress.getHostAddress() + ", Port: " + clientPort);
                SimpleLogger.log("Client Connection Info: Remote Address: " + packet.getSocketAddress());

                String response;
                if (!isValidRequest(request)) {
                    SimpleLogger.logError("Received malformed UDP request from client " + clientAddress.getHostAddress() + ":" + clientPort);
                    response = "Error: Malformed request";
                } else {
                    response = handleRequest(request);
                }

                byte[] responseBytes = response.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(responseBytes, responseBytes.length, clientAddress, clientPort);
                socket.send(responsePacket);
                
                SimpleLogger.log("Sent UDP response to IP: " + clientAddress.getHostAddress() + ", Port: " + clientPort + " - " + response);
            }
        } catch (IOException e) {
            SimpleLogger.logError("UDP server encountered an error: " + e.getMessage());
        }
    }

    /**
     * Validates the incoming request format for UDP packets.
     *
     * @param request The request string to validate.
     * @return true if the request is valid; false otherwise.
     */
    private boolean isValidRequest(String request) {
        return request != null && !request.trim().isEmpty() && 
               (request.startsWith("PUT") || request.startsWith("GET") || request.startsWith("DELETE"));
    }
}
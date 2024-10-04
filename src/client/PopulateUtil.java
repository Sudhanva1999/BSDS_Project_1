package src.client;

import java.io.*;
import java.net.*;

/**
 * The PopulateUtil class is responsible for populating a key-value store on
 * both TCP and UDP servers.
 * It sends a series of PUT, GET, and DELETE requests to the server to populate
 * and interact with the stored data.
 */
public class PopulateUtil {

    private String hostname;
    private int port;

    private String[][] wordPairs = {
            { "USA", "Washington DC" },
            { "France", "Paris" },
            { "Germany", "Berlin" },
            { "Japan", "Tokyo" },
            { "Canada", "Ottawa" },
            { "India", "New Delhi" },
            { "Brazil", "Brasilia" },
            { "Australia", "Canberra" },
            { "Mexico", "Mexico City" },
            { "Russia", "Moscow" },
            { "Tesla", "Electric Car" },
            { "Apple", "Technology" },
            { "Nike", "Sportswear" },
            { "Amazon", "E-commerce" },
            { "Coca-Cola", "Beverage" },
            { "Google", "Search Engine" },
            { "Microsoft", "Software" },
            { "BMW", "Automobile" },
            { "Starbucks", "Coffee" },
            { "Samsung", "Electronics" }
    };

    /**
     * Constructs a PopulateUtil instance with the given hostname and port.
     *
     * @param hostname The hostname of the server.
     * @param port     The port of the server.
     */
    public PopulateUtil(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * Populates the key-value store on a TCP server by sending PUT requests for
     * each word pair
     * and retrieves or deletes data based on predefined logic.
     * It establishes a TCP connection, performs the operations, and logs the
     * results.
     */
    public void populateTCPServer() {

        try (Socket socket = new Socket(hostname, port);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            SimpleLogger.log("Connected to TCP server at " + hostname + ":" + port);

            for (String[] pair : wordPairs) {
                String key = pair[0];
                String value = pair[1];
                out.println("PUT:" + key + ":" + value);
                String response = in.readLine();
                SimpleLogger.log("TCP PUT Operation: Sent (" + key + ", " + value + ") - Response: " + response);
            }

            for (int i = 0; i < 5; i++) {
                String key = wordPairs[i][0];
                out.println("GET:" + key);
                String response = in.readLine();
                SimpleLogger.log("TCP GET Operation: Requested " + key + " - Response: " + response);
            }

            for (int i = 0; i < 5; i++) {
                String key = wordPairs[i][0];
                out.println("DELETE:" + key);
                String response = in.readLine();
                SimpleLogger.log("TCP DELETE Operation: Deleted " + key + " - Response: " + response);
            }

        } catch (IOException e) {
            SimpleLogger.logError("Error while populating the TCP server: " + e.getMessage());
        }
    }

    /**
     * Populates the key-value store on a UDP server by sending PUT requests for
     * each word pair,
     * and retrieves or deletes data based on predefined logic.
     * It uses UDP datagrams to communicate with the server and logs the results.
     */
    public void populateUDPServer() {

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName(hostname);
            socket.setSoTimeout(5000);

            SimpleLogger.log("Connected to UDP server at " + hostname + ":" + port);

            for (String[] pair : wordPairs) {
                String key = pair[0];
                String value = pair[1];
                String message = "PUT:" + key + ":" + value;

                byte[] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
                socket.send(packet);
                SimpleLogger.log("UDP PUT Operation: Sent (" + key + ", " + value + ")");

                DatagramPacket responsePacket = new DatagramPacket(new byte[1024], 1024);
                try {
                    socket.receive(responsePacket);
                    String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
                    SimpleLogger.log("UDP Response: " + response);
                } catch (SocketTimeoutException e) {
                    SimpleLogger.logError("No response from UDP server for (" + key + ", " + value + ")");
                }
            }

            for (int i = 0; i < 5; i++) {
                String key = wordPairs[i][0];
                String message = "GET:" + key;
                byte[] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
                socket.send(packet);
                SimpleLogger.log("UDP GET Operation: Requested " + key);

                DatagramPacket responsePacket = new DatagramPacket(new byte[1024], 1024);
                try {
                    socket.receive(responsePacket);
                    String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
                    SimpleLogger.log("UDP Response: " + response);
                } catch (SocketTimeoutException e) {
                    SimpleLogger.logError("No response from UDP server for GET request on " + key);
                }
            }

            for (int i = 0; i < 5; i++) {
                String key = wordPairs[i][0];
                String message = "DELETE:" + key;
                byte[] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
                socket.send(packet);
                SimpleLogger.log("UDP DELETE Operation: Deleted " + key);

                DatagramPacket responsePacket = new DatagramPacket(new byte[1024], 1024);
                try {
                    socket.receive(responsePacket);
                    String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
                    SimpleLogger.log("UDP Response: " + response);
                } catch (SocketTimeoutException e) {
                    SimpleLogger.logError("No response from UDP server for DELETE request on " + key);
                }
            }
        } catch (IOException e) {
            SimpleLogger.logError("Error while populating the UDP server: " + e.getMessage());
        }
    }
}
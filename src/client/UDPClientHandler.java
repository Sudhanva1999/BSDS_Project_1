package src.client;

import java.io.*;
import java.net.*;

/**
 * A helper class that facilitates communication between the client and a
 * UDP-based key-value store server.
 * It sends requests and receives responses using UDP protocol.
 */
public class UDPClientHandler extends ClientHelper {

    /**
     * Initializes a new UDP client handler with the specified port and hostname.
     *
     * @param port     the port number of the server
     * @param hostname the hostname or IP address of the server
     */
    public UDPClientHandler(int port, String hostname) {
        super(port, hostname);
    }

    /**
     * Starts the UDP client, allowing the user to interact with the server by
     * sending commands such as PUT, GET, and DELETE.
     * The client retries to establish a connection with the server and handles
     * responses or errors appropriately.
     */
    public void start() {

        PopulateUtil populateServer = new PopulateUtil(hostname, port);
        populateServer.populateUDPServer();

        try (DatagramSocket socket = new DatagramSocket();
                BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            InetAddress address = InetAddress.getByName(hostname);
            byte[] buffer;

            socket.setSoTimeout(5000);

            SimpleLogger.log("Connected to UDP server at " + hostname + ":" + port);
            displayMenu();

            String request;
            while (true) {
                System.out.print("> ");
                request = console.readLine();
                if (request.equalsIgnoreCase("exit")) {
                    SimpleLogger.log("User exited the application.");
                    break;
                }

                String formattedRequest = formatRequest(request);
                if (formattedRequest == null) {
                    System.err.println("Invalid command format. Please try again.");
                    continue;
                }

                buffer = formattedRequest.getBytes();
                DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length, address, port);
                socket.send(requestPacket);
                SimpleLogger.log("Sent UDP request: " + formattedRequest);

                DatagramPacket responsePacket = new DatagramPacket(new byte[1024], 1024);

                try {
                    socket.receive(responsePacket);
                    String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
                    if (isValidResponse(response)) {
                        SimpleLogger.log("Received UDP response: " + response);
                        System.out.println("Response: " + response);
                    } else {
                        SimpleLogger.logError("Received malformed or unsolicited response::PORT" + port + "::Length::"
                                + response.length());
                        System.err.println("Error: Received malformed or unsolicited response::PORT" + port
                                + "::Length::" + response.length());
                    }
                } catch (SocketTimeoutException e) {
                    SimpleLogger.logError("No response from server within 5 seconds.");
                    System.err.println("Error: No response from server within 5 seconds.");
                }
            }

        } catch (IOException e) {
            SimpleLogger.logError("UDP connection error: " + e.getMessage());
        }
    }

    /**
     * Displays the available commands for interacting with the key-value store
     * server.
     */
    private void displayMenu() {
        System.out.println("=== Key-Value Store UDP Client ===");
        System.out.println("Available commands:");
        System.out.println("PUT <key> <value>  - Store a value under a key");
        System.out.println("GET <key>         - Retrieve the value for a key");
        System.out.println("DELETE <key>      - Remove a key from the store");
        System.out.println("exit              - Exit the client");
        System.out.println("=============================");
    }

    /**
     * Formats the user's input into the correct format for a UDP request.
     *
     * @param request the user's input command
     * @return the formatted request or null if the input is invalid
     */
    private String formatRequest(String request) {
        String[] parts = request.split(" ");

        if (parts.length == 0) {
            return null; // Invalid request
        }

        String command = parts[0].toUpperCase();

        switch (command) {
            case "PUT":
                if (parts.length == 3) {
                    return "PUT:" + parts[1] + ":" + parts[2];
                } else {
                    SimpleLogger.logError("Invalid PUT command format. Expected PUT <key> <value>.");
                    return null;
                }
            case "GET":
                if (parts.length == 2) {
                    return "GET:" + parts[1];
                } else {
                    SimpleLogger.logError("Invalid GET command format. Expected GET <key>.");
                    return null;
                }
            case "DELETE":
                if (parts.length == 2) {
                    return "DELETE:" + parts[1];
                } else {
                    SimpleLogger.logError("Invalid DELETE command format. Expected DELETE <key>.");
                    return null;
                }
            default:
                SimpleLogger.logError("Unknown command: " + command);
                return null;
        }
    }

    /**
     * Validates whether the response received from the server is correctly
     * formatted.
     *
     * @param response the response received from the server
     * @return true if the response is valid, false otherwise
     */
    private boolean isValidResponse(String response) {
        if (response.startsWith("ERROR:") || response.startsWith("OK:") || response.startsWith("PUT")
                || response.startsWith("GET") || response.startsWith("DELETE")) {
            return true;
        }
        SimpleLogger.logError("Received unsolicited or malformed response");
        return false;
    }
}
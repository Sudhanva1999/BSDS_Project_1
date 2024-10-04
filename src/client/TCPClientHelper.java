package src.client;

import java.io.*;
import java.net.*;

/**
 * The {@code TCPClientHelper} class handles communication between the client
 * and a TCP server.
 * It attempts to establish a connection to the server, allows the user to send
 * requests, and
 * receives and validates server responses.
 * 
 * <p>
 * It supports commands such as PUT, GET, and DELETE for interacting with a
 * key-value store.
 * </p>
 */
public class TCPClientHelper extends ClientHelper {

    private static final int MAX_RETRIES = 5;

    /**
     * Constructs a new {@code TCPClientHelper} with the specified port and
     * hostname.
     *
     * @param port     The port number on which the server is listening.
     * @param hostname The hostname or IP address of the server.
     */
    public TCPClientHelper(int port, String hostname) {
        super(port, hostname);
    }

    /**
     * Starts the TCP client, establishes a connection to the server, and handles
     * user commands
     * to interact with the key-value store. Retries are attempted up to
     * {@code MAX_RETRIES} if
     * the connection fails.
     */
    public void start() {
        PopulateUtil populateServer = new PopulateUtil(hostname, port);
        populateServer.populateTCPServer();

        int retryCount = 0;
        boolean connected = false;
        while (retryCount < MAX_RETRIES && !connected) {
            try (Socket socket = new Socket(hostname, port);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

                socket.setSoTimeout(5000);
                SimpleLogger.log("Connected to TCP server at " + hostname + ":" + port);
                connected = true;
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

                    out.println(formattedRequest);
                    SimpleLogger.log("Sent request: " + formattedRequest);

                    try {
                        String response = in.readLine();
                        if (response != null) {
                            if (isValidResponse(response)) {
                                SimpleLogger.log("Received response: " + response);
                                System.out.println("Response: " + response);
                            } else {
                                SimpleLogger.logError("Received malformed response from SERVER");
                                System.err.println("Error: Received malformed response from SERVER.");
                            }
                        }
                    } catch (SocketTimeoutException e) {
                        SimpleLogger.logError("No response from server within 5 seconds.");
                        System.err.println("Error: No response from server within 5 seconds.");
                    }
                }

            } catch (IOException e) {
                retryCount++;
                SimpleLogger.logError("TCP connection error: " + e.getMessage());
                if (retryCount < MAX_RETRIES) {
                    SimpleLogger.log("Retrying connection attempt " + retryCount + "/" + MAX_RETRIES + "...");
                } else {
                    SimpleLogger.logError("Server is unresponsive after " + MAX_RETRIES + " attempts. Exiting.");
                    System.err.println("Server is unresponsive after " + MAX_RETRIES + " attempts. Exiting.");
                    break;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    SimpleLogger.logError("Retry sleep interrupted: " + ie.getMessage());
                }
            }
        }
    }

    /**
     * Displays the list of available commands for interacting with the key-value
     * store.
     */
    private void displayMenu() {
        System.out.println("=== Key-Value Store TCP Client ===");
        System.out.println("Available commands:");
        System.out.println("PUT <key> <value>  - Store a value under a key");
        System.out.println("GET <key>         - Retrieve the value for a key");
        System.out.println("DELETE <key>      - Remove a key from the store");
        System.out.println("exit              - Exit the client");
        System.out.println("=============================");
    }

    /**
     * Formats the user input into a protocol-specific request string.
     *
     * @param request The user input as a string.
     * @return A formatted request string or {@code null} if the input is invalid.
     */
    private String formatRequest(String request) {
        String[] parts = request.split(" ");

        if (parts.length == 0) {
            return null;
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
     * Validates the response received from the server.
     *
     * @param response The response string from the server.
     * @return {@code true} if the response is valid, {@code false} otherwise.
     */
    private boolean isValidResponse(String response) {
        if (response.startsWith("ERROR:") || response.startsWith("OK:") || response.startsWith("PUT")
                || response.startsWith("GET") || response.startsWith("DELETE")) {
            return true;
        }
        SimpleLogger.logError("Received unsolicited or malformed response from SERVER");
        return false;
    }
}
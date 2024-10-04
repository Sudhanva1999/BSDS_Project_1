package src.server;

import java.io.*;
import java.net.*;
import java.util.regex.Pattern;

/**
 * TCPServerHandler class that handles incoming TCP connections and processes
 * requests.
 * It extends the ServerHandler abstract class to manage key-value operations.
 */
public class TCPServerHandler extends ServerHandler {

    private static final Pattern VALID_PAYLOAD_PATTERN = Pattern.compile("^[a-zA-Z0-9:]+$");

    /**
     * Constructs a TCPServerHandler with the specified port and KeyStore.
     *
     * @param port          The port number the server will listen on.
     * @param keyValueStore The KeyStore instance to handle key-value operations.
     */
    public TCPServerHandler(int port, KeyStore keyValueStore) {
        super(port, keyValueStore);
    }

    /**
     * Starts the TCP server, accepting client connections and processing requests.
     * This method runs in a loop, handling multiple client connections.
     */
    public void start() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            SimpleLogger.log("TCP Server started on port " + port);

            populateKeyStore();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                InetAddress clientAddress = clientSocket.getInetAddress();
                int clientPort = clientSocket.getPort();
                SimpleLogger.log(
                        "TCP client connected from IP: " + clientAddress.getHostAddress() + ", Port: " + clientPort);

                try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    SimpleLogger.log("Client Connection Info: " +
                            "Remote IP: " + clientSocket.getRemoteSocketAddress() +
                            ", Local Port: " + clientSocket.getLocalPort() +
                            ", Is Connected: " + clientSocket.isConnected());

                    String request;
                    while ((request = in.readLine()) != null) {
                        SimpleLogger.log("Received request from " + clientAddress.getHostAddress() + ":" + clientPort
                                + " - " + request);
                        String response = handleRequest(request);

                        if (!isValidRequest(request)) {
                            SimpleLogger.logError("Received malformed request from client "
                                    + clientAddress.getHostAddress() + ":" + clientPort);
                            response = "Error: Malformed request from Client";
                        }

                        out.println(response);
                        SimpleLogger.log("Sent response to " + clientAddress.getHostAddress() + ":" + clientPort + " - "
                                + response);
                    }

                } catch (IOException e) {
                    SimpleLogger.logError("Error while handling client request from " + clientAddress.getHostAddress()
                            + ":" + clientPort + " - " + e.getMessage());
                } finally {
                    clientSocket.close();
                    SimpleLogger
                            .log("TCP client disconnected from " + clientAddress.getHostAddress() + ":" + clientPort);
                }
            }
        } catch (IOException e) {
            SimpleLogger.logError("TCP server encountered an error: " + e.getMessage());
        }
    }

    /**
     * Validates the incoming request format.
     * 
     * The request must:
     * - Be non-null and non-empty.
     * - Start with one of the commands: PUT, GET, DELETE.
     * - The payload should contain only alphanumeric characters and colons.
     *
     * @param request The request string to validate.
     * @return true if the request is valid; false otherwise.
     */
    private boolean isValidRequest(String request) {
        if (request == null || request.trim().isEmpty()) {
            return false;
        }

        String[] parts = request.split(":", 2);

        if (parts.length < 2) {
            return false;
        }

        String command = parts[0];
        String payload = parts[1];

        if (!(command.equals("PUT") || command.equals("GET") || command.equals("DELETE"))) {
            return false;
        }

        return VALID_PAYLOAD_PATTERN.matcher(payload).matches();
    }
}
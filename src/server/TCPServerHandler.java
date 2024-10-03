package src.server;

import java.io.*;
import java.net.*;

/**
 * TCPServerHandler class that handles incoming TCP connections and processes requests.
 * It extends the ServerHandler abstract class to manage key-value operations.
 */
public class TCPServerHandler extends ServerHandler {

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

            while (true) {
                Socket clientSocket = serverSocket.accept();
                InetAddress clientAddress = clientSocket.getInetAddress();
                int clientPort = clientSocket.getPort();
                SimpleLogger.log("TCP client connected from IP: " + clientAddress.getHostAddress() + ", Port: " + clientPort);

                try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    SimpleLogger.log("Client Connection Info: " +
                            "Remote IP: " + clientSocket.getRemoteSocketAddress() +
                            ", Local Port: " + clientSocket.getLocalPort() +
                            ", Is Connected: " + clientSocket.isConnected());

                    String request;
                    while ((request = in.readLine()) != null) {
                        SimpleLogger.log("Received request from " + clientAddress.getHostAddress() + ":" + clientPort + " - " + request);
                        String response = handleRequest(request);

                        if (!isValidRequest(request)) {
                            SimpleLogger.logError("Received malformed request from client " + clientAddress.getHostAddress() + ":" + clientPort);
                            response = "Error: Malformed request";
                        }

                        out.println(response);
                        SimpleLogger.log("Sent response to " + clientAddress.getHostAddress() + ":" + clientPort + " - " + response);
                    }

                } catch (IOException e) {
                    SimpleLogger.logError("Error while handling client request from " + clientAddress.getHostAddress() + ":" + clientPort + " - " + e.getMessage());
                } finally {
                    clientSocket.close();
                    SimpleLogger.log("TCP client disconnected from " + clientAddress.getHostAddress() + ":" + clientPort);
                }
            }
        } catch (IOException e) {
            SimpleLogger.logError("TCP server encountered an error: " + e.getMessage());
        }
    }

    /**
     * Validates the incoming request format.
     *
     * @param request The request string to validate.
     * @return true if the request is valid; false otherwise.
     */
    private boolean isValidRequest(String request) {
        return request != null && !request.trim().isEmpty() && (request.startsWith("PUT") || request.startsWith("GET") || request.startsWith("DELETE"));
    }
}
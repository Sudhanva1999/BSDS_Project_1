package server.src.java;

import java.io.*;
import java.net.*;
import resources.SimpleLogger;

public class TCPServerHandler extends ServerHandler {


    public TCPServerHandler(int port, KeyStore keyValueStore) {
        super(port, keyValueStore);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            SimpleLogger.log("TCP Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                SimpleLogger.log("TCP client connected from " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

                try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    String request;
                    while ((request = in.readLine()) != null) {
                        SimpleLogger.log("Received request: " + request);
                        String response = handleRequest(request);
                        out.println(response);
                        SimpleLogger.log("Sent response: " + response);
                    }
                } catch (IOException e) {
                    SimpleLogger.logError("Error while handling client request: " + e.getMessage());
                } finally {
                    clientSocket.close();
                    SimpleLogger.log("TCP client disconnected");
                }
            }
        } catch (IOException e) {
            SimpleLogger.logError("TCP server encountered an error: " + e.getMessage());
        }
    }

}

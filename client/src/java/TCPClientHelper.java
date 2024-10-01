package client.src.java;

import java.io.*;
import java.net.*;
import java.util.Date;

public class TCPClientHelper {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java TCPClient <hostname> <port>");
            return;
        }

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(hostname, port), 3000);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String userInput;

            while ((userInput = stdIn.readLine()) != null) {
                System.out.println(new Date().getTime() + " - Sending: " + userInput);
                out.println(userInput);

                String response = in.readLine();
                if (response != null) {
                    System.out.println(new Date().getTime() + " - Response: " + response);
                } else {
                    System.out.println(new Date().getTime() + " - No response from server.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

package src.server;

/**
 * The Server class is the entry point for starting the server application.
 * It initializes the server based on the specified protocol (TCP or UDP)
 * and listens on the provided port number.
 */
public class Server {

    /**
     * The main method that starts the server.
     *
     * @param args Command-line arguments:
     *             - args[0]: Protocol type ("TCP" or "UDP")
     *             - args[1]: Port number to listen on
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java server <TCP/UDP Protocol> <Port Number>");
            System.exit(1);
        }

        String protocol = args[0];
        int port = Integer.parseInt(args[1]);

        if (protocol.equals("TCP")) {
            TCPServerHandler tcpServer = new TCPServerHandler(port, new KeyStore());
            SimpleLogger.log("Server started on port::" + port + "::Server protocol::" + protocol);
            tcpServer.start();
        } else if (protocol.equals("UDP")) {
            UDPServerHandler udpServer = new UDPServerHandler(port, new KeyStore());
            SimpleLogger.log("Server started on port::" + port + "::Server protocol::" + protocol);
            udpServer.start();
        } else {
            System.err.println("Invalid protocol specified. Please use 'TCP' or 'UDP'.");
            System.exit(1);
        }
    }
}
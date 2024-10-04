package src.client;

/**
 * The {@code Client} class serves as the main entry point for the client-side
 * application.
 * It initiates either a TCP or UDP connection based on the provided protocol,
 * hostname, and port number.
 * 
 * <p>
 * Usage: java client &lt;TCP/UDP Protocol&gt; &lt;Hostname&gt; &lt;Port
 * Number&gt;
 * </p>
 */
public class Client {

    /**
     * The main method which starts the client application. It expects three
     * arguments:
     * <ul>
     * <li>The protocol type ("TCP" or "UDP").</li>
     * <li>The server's hostname or IP address.</li>
     * <li>The port number on which the server is listening.</li>
     * </ul>
     * 
     * <p>
     * If the protocol is TCP, the {@code TCPClientHelper} is started.
     * If the protocol is UDP, the {@code UDPClientHandler} is started.
     * </p>
     * 
     * @param args The command-line arguments. Should contain exactly 3 values:
     *             protocol, hostname, and port.
     *             If the number of arguments is incorrect, an error message will be
     *             displayed.
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java client <TCP/UDP Protocol> <Hostname> <Port Number>");
            System.exit(1);
        }

        String protocol = args[0];
        String hostname = args[1];
        int port = Integer.parseInt(args[2]);

        if (protocol.equals("TCP")) {
            TCPClientHelper tcpClient = new TCPClientHelper(port, hostname);
            SimpleLogger.log(
                    "Client started on port::" + port + "::Server protocol::" + protocol + "::Hostname::" + hostname);
            tcpClient.start();

        } else if (protocol.equals("UDP")) {
            UDPClientHandler udpClient = new UDPClientHandler(port, hostname);
            SimpleLogger.log(
                    "Client started on port::" + port + "::Server protocol::" + protocol + "::Hostname::" + hostname);
            udpClient.start();
        }
    }
}

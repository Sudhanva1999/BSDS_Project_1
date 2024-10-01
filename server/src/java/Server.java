package server.src.java;

import resources.SimpleLogger;

public class Server {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java server <TCP/UDP Protocol> <Port Number>");
            System.exit(1);
        }

        String protocol = args[0];
        int port = Integer.parseInt(args[1]);
        SimpleLogger.log("Server started on port::" + port + "::Server protocol::"+ protocol);

        if(protocol.equals("TCP")) {
            TCPServerHandler tcpServer = new TCPServerHandler(port, new KeyStore());
            tcpServer.start();
        } else if (protocol.equals("UDP")) {
            UDPServerHandler udpServer = new UDPServerHandler(port, new KeyStore());
            udpServer.start();
        }
    }

    
}




























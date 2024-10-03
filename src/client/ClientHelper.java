package src.client;

/**
 * The {@code ClientHelper} abstract class serves as the base class for client helpers
 * that establish and manage connections to a server using a specified protocol (TCP or UDP).
 * 
 * <p>This class holds the common attributes for a client (port and hostname) and
 * defines an abstract {@code start()} method that must be implemented by subclasses
 * to initiate the protocol-specific connection.</p>
 */
public abstract class ClientHelper {

    protected int port;
    protected String hostname;

    /**
     * Constructs a {@code ClientHelper} with the specified port number and hostname.
     * 
     * @param port The port number of the server.
     * @param hostname The hostname or IP address of the server.
     */
    public ClientHelper(int port, String hostname) {
        this.port = port;
        this.hostname = hostname;
    }
    
    /**
     * Abstract method that must be implemented by subclasses to start the client
     * and initiate communication using the protocol (TCP or UDP).
     */
    abstract void start();
}

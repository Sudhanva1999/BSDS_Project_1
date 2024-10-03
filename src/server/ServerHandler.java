package src.server;

/**
 * Abstract class ServerHandler that defines the structure for handling server requests.
 * It provides a mechanism to process incoming requests using the specified KeyStore.
 */
public abstract class ServerHandler {

    protected int port;
    protected KeyStore keyStore;

    /**
     * Constructor to initialize the ServerHandler with a port and KeyStore.
     *
     * @param port          The port number the server will listen on.
     * @param keyValueStore The KeyStore instance to handle key-value operations.
     */
    public ServerHandler(int port, KeyStore keyValueStore) {
        this.port = port;
        this.keyStore = keyValueStore;
    }

    /**
     * Abstract method to start the server handler.
     * This method must be implemented by subclasses.
     */
    abstract void start();

    /**
     * Handles an incoming request by parsing it and performing the appropriate operation.
     *
     * @param request The incoming request string in the format "COMMAND:key:value".
     * @return A response string indicating the result of the operation or an error message.
     */
    String handleRequest(String request) {
        String[] parts = request.split(":");
    
        if (parts.length == 0) {
            SimpleLogger.logError("Received malformed request::PORT" + port + "::Length::" + request.length());
            return "ERROR: Invalid command format ::PORT" + port + "::Length::" + request.length();
        }

        String command = parts[0].toUpperCase();

        switch (command) {
            case "PUT":
                if (parts.length == 3) {
                    keyStore.put(parts[1], parts[2]);
                    return "PUT successful for key: " + parts[1];
                } else {
                    SimpleLogger.logError("Malformed PUT request::PORT" + port + "::Length::" + request.length());
                    return "ERROR: PUT command format is PUT:key:value";
                }
            case "GET":
                if (parts.length == 2) {
                    String value = keyStore.get(parts[1]);
                    return value != null ? "GET successful: " + value : "ERROR: Key not found";
                } else {
                    SimpleLogger.logError("Malformed GET request::PORT" + port + "::Length::" + request.length());
                    return "ERROR: GET command format is GET:key";
                }
            case "DELETE":
                if (parts.length == 2) {
                    String key = parts[1];
                    if (keyStore.containsKey(key)) {
                        keyStore.delete(key);
                        return "DELETE successful for key: " + key;
                    } else {
                        SimpleLogger.logError("DELETE failed for key: " + key + " - Key does not exist::PORT" + port);
                        return "ERROR: Key does not exist";
                    }
                } else {
                    SimpleLogger.logError("Malformed DELETE request::PORT" + port + "::Length::" + request.length());
                    return "ERROR: DELETE command format is DELETE:key";
                }
            default:
                SimpleLogger.logError("Unknown command received::PORT" + port + "::Length::" + request.length());
                return "ERROR: Unknown command " + command;
        }
    }
}
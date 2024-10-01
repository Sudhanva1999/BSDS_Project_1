package server.src.java;


public abstract class ServerHandler {

    protected int port;
    protected KeyStore keyStore;

    abstract void start();  // Starts the server (TCP or UDP)

    public ServerHandler(int port, KeyStore keyValueStore) {
        this.port = port;
        this.keyStore = keyValueStore;
    }


    String handleRequest(String request) {
        String[] parts = request.split(" ", 3);
        if (parts.length < 2) {
            return "ERROR: Invalid request format";
        }

        String operation = parts[0].toUpperCase();
        String key = parts[1];

        switch (operation) {
            case "PUT":
                if (parts.length == 3) {
                    String value = parts[2];
                    keyStore.put(key, value);
                    return "OK: Key " + key + " has been updated.";
                } else {
                    return "ERROR: PUT operation requires both key and value.";
                }

            case "GET":
                String result = keyStore.get(key);
                return (result != null) ? result : "NOT FOUND: Key " + key + " does not exist.";

            case "DELETE":
                keyStore.delete(key);
                return "OK: Key " + key + " has been deleted.";

            default:
                return "ERROR: Unknown operation " + operation;
        }
    }


}
package server.src.java;
import java.util.HashMap;
import resources.SimpleLogger;

public class KeyStore {
    private final HashMap<String, String> store = new HashMap<>();

    public synchronized void put(String key, String value) {
        store.put(key, value);
        SimpleLogger.log("PUT operation - Key: " + key + ", Value: " + value);
    }

    public synchronized String get(String key) {
        String value = store.get(key);
        if (value != null) {
            SimpleLogger.log("GET operation - Key: " + key + ", Value: " + value);
        } else {
            SimpleLogger.log("GET operation - Key: " + key + ", Value not found");
        }
        return value;
    }

    public synchronized void delete(String key) {
        if (store.containsKey(key)) {
            store.remove(key);
            SimpleLogger.log("DELETE operation - Key: " + key + " removed");
        } else {
            SimpleLogger.log("DELETE operation - Key: " + key + " not found");
        }
    }
}
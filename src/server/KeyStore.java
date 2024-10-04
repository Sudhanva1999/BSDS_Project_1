package src.server;

import java.util.HashMap;

/**
 * The KeyStore class provides a simple key-value store implementation
 * that allows storing, retrieving, and deleting key-value pairs.
 * It is synchronized to support concurrent access from multiple threads.
 */
public class KeyStore {
    private final HashMap<String, String> store = new HashMap<>();

    /**
     * Stores a key-value pair in the key store.
     *
     * @param key   The key to be stored.
     * @param value The value associated with the key.
     */
    public synchronized void put(String key, String value) {
        store.put(key, value);
        SimpleLogger.log("PUT operation - Key: " + key + ", Value: " + value);
    }

    /**
     * Retrieves the value associated with the given key from the key store.
     *
     * @param key The key whose associated value is to be retrieved.
     * @return The value associated with the key, or null if the key is not found.
     */
    public synchronized String get(String key) {
        String value = store.get(key);
        if (value != null) {
            SimpleLogger.log("GET operation - Key: " + key + ", Value: " + value);
        } else {
            SimpleLogger.log("GET operation - Key: " + key + ", Value not found");
        }
        return value;
    }

    /**
     * Deletes the key-value pair associated with the given key from the key store.
     *
     * @param key The key to be deleted.
     */
    public synchronized void delete(String key) {
        if (store.containsKey(key)) {
            store.remove(key);
            SimpleLogger.log("DELETE operation - Key: " + key + " removed");
        } else {
            SimpleLogger.log("DELETE operation - Key: " + key + " not found");
        }
    }

    /**
     * Checks if the key exists in the key store.
     *
     * @param key The key to be checked.
     * @return true if the key exists, false otherwise.
     */
    public synchronized boolean containsKey(String key) {
        boolean exists = store.containsKey(key);
        if (exists) {
            SimpleLogger.log("ContainsKey operation - Key: " + key + " exists in the store.");
        } else {
            SimpleLogger.log("ContainsKey operation - Key: " + key + " does not exist in the store.");
        }
        return exists;
    }
}
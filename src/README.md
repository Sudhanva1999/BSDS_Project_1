# Key-Value Store Server: TCP and UDP Protocol Support

This project implements a key-value store server that supports both TCP and UDP communication protocols. The server allows clients to send requests for performing operations such as `PUT`, `GET`, and `DELETE` on the key-value store. The server is capable of handling both reliable (TCP) and faster but connectionless (UDP) communications.


find . -name "*.java" -print | xargs javac 

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Project Structure](#project-structure)
3. [How to Build](#how-to-build)
4. [Running the Server](#running-the-server)
    - [TCP Mode](#running-the-server-in-tcp-mode)
    - [UDP Mode](#running-the-server-in-udp-mode)
5. [Running the Client](#running-the-client)
    - [TCP Client Example](#running-a-tcp-client)
    - [UDP Client Example](#running-a-udp-client)
6. [Key-Value Operations](#key-value-operations)
7. [Logging](#logging)
8. [Closing Remarks](#closing-remarks)

## Prerequisites

To run this project, ensure you have the following installed:

1. **Java JDK 8+**
2. **Command-line terminal** for running the server and client


## How to Build

1. **Compile the Java files**: If using the terminal, navigate to the `server/` directory and run:

   ```bash
   javac -d bin src/java/*.java src/java/resources/SimpleLogger.java
   ```

2. **Create the necessary directories** (if not present):

   ```bash
   mkdir -p serverLogs/logs
   ```

## Running the Server

### Running the Server in TCP Mode

1. **Start the TCP server** by specifying the protocol (`TCP`) and the port number (e.g., `9090`):

   ```bash
   java -cp bin server.src.java.Server TCP 9090
   ```

   You should see a log message confirming that the server has started:

   ```
   TCP Server started on port 9090
   ```

2. **The TCP server is now ready** to accept client connections. It listens on the specified port for incoming requests.

### Running the Server in UDP Mode

1. **Start the UDP server** by specifying the protocol (`UDP`) and the port number (e.g., `9090`):

   ```bash
   java -cp bin server.src.java.Server UDP 9090
   ```

   You should see a log message confirming that the server has started:

   ```
   UDP Server started on port 9090
   ```

2. **The UDP server is now ready** to receive datagram packets and respond to requests.

## Running the Client

The project includes example client implementations for both TCP and UDP.

### Running a TCP Client

1. **Compile the TCP client**:

   ```bash
   javac -d bin client/TCPClient.java
   ```

2. **Start the TCP client** and connect to the server:

   ```bash
   java -cp bin client.TCPClient 127.0.0.1 9090
   ```

   Replace `127.0.0.1` with the IP address of the server if running on a different machine.

3. **Send commands** to the server from the client. Example commands:

   ```
   PUT:key1:value1
   GET:key1
   DELETE:key1
   ```

4. **Example Output**:

   ```
   PUT successful for key: key1
   GET successful: value1
   DELETE successful for key: key1
   ```

### Running a UDP Client

1. **Compile the UDP client**:

   ```bash
   javac -d bin client/UDPClient.java
   ```

2. **Start the UDP client** and connect to the server:

   ```bash
   java -cp bin client.UDPClient 127.0.0.1 9090
   ```

3. **Send commands** to the server. Example commands:

   ```
   PUT:key1:value1
   GET:key1
   DELETE:key1
   ```

4. **Example Output**:

   ```
   PUT successful for key: key1
   GET successful: value1
   DELETE successful for key: key1
   ```

## Key-Value Operations

The following commands are supported:

1. **PUT:key:value**  
   Adds or updates a key-value pair in the store.

2. **GET:key**  
   Retrieves the value associated with the specified key.

3. **DELETE:key**  
   Removes the key-value pair for the specified key.

Malformed or invalid commands will result in error responses, and the errors are logged.

## Logging

The server uses a custom logger (`SimpleLogger`) to track server operations. Logs are saved in the `serverLogs/logs/` directory with a daily rotation. Each log entry includes a timestamp and details about the operation being performed.

- **Log files**: Each log file is named with the date it was created, e.g., `2024-10-01.log`.
- **Error logs**: Errors such as malformed requests, server issues, and client disconnections are also logged.

Example log message:

```
2024-10-01 10:15:45.123 - PUT operation - Key: key1, Value: value1
2024-10-01 10:16:00.567 - GET operation - Key: key1, Value: value1
2024-10-01 10:17:30.789 - DELETE operation - Key: key1 removed
```

## Closing Remarks

This Key-Value Store Server supports both TCP and UDP protocols, demonstrating the flexibility of communication between clients and servers. By utilizing TCP for reliable connections and UDP for faster, connectionless communication, this system can be adapted for various real-world applications requiring efficient key-value storage and retrieval.

Ensure to gracefully close the server after use and monitor the log files for any issues or errors during operation.
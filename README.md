## Key-Value Store Server-Client Project

### Overview

This project implements a key-value store server that allows a single client to communicate with it using either **TCP** or **UDP**. The server supports three basic operations: 
1. **PUT** (key, value) – Stores a value against a key.
2. **GET** (key) – Retrieves the value for a given key.
3. **DELETE** (key) – Deletes the key-value pair associated with the key.

The server can be configured to communicate over either **TCP** or **UDP** by using sockets. It is designed to handle a single client connection at a time and does not use multithreading.

### Features

- **Single-threaded server**: The server handles one client at a time.
- **Key-value store**: A `HashMap` is used to store key-value pairs.
- **Protocol support**: Both TCP and UDP protocols are implemented.
- **Timeout handling**: The client can handle server timeouts for unresponsive servers.
- **Error handling**: Malformed requests are logged and reported.
- **Logging**: Both the server and the client log each request/response with a timestamp down to millisecond precision.
  
### Project Structure

```
/src
│
├── TCPServer.java       # Server implementation using TCP
├── TCPClient.java       # Client implementation using TCP
│
├── UDPServer.java       # Server implementation using UDP
├── UDPClient.java       # Client implementation using UDP
│
└── README.md            # This documentation file
```

### Requirements

- **Java Development Kit (JDK)**: Make sure you have Java (JDK) installed on your machine (version 8 or later).
- **Network environment**: Localhost or any network setup to support communication between client and server.

### Setup Instructions

1. **Clone or Download the Repository:**
   Download the project files or clone the repository using:
   ```bash
   git clone <repository-url>
   ```
   
2. **Compile the Java Files:**
   Navigate to the project directory and compile the Java files:
   ```bash
   javac TCPServer.java TCPClient.java UDPServer.java UDPClient.java
   ```

3. **Run the TCP Server and Client:**

   - **Start the TCP Server:**
     Open a terminal in the project directory and run:
     ```bash
     java TCPServer <port_number>
     ```
     Example:
     ```bash
     java TCPServer 12345
     ```

   - **Run the TCP Client:**
     In a new terminal, run:
     ```bash
     java TCPClient <hostname> <port_number>
     ```
     Example:
     ```bash
     java TCPClient localhost 12345
     ```

   The client will prompt you to enter commands such as:
   ```
   PUT:key:value
   GET:key
   DELETE:key
   ```

4. **Run the UDP Server and Client:**

   - **Start the UDP Server:**
     Open a terminal in the project directory and run:
     ```bash
     java UDPServer <port_number>
     ```
     Example:
     ```bash
     java UDPServer 12345
     ```

   - **Run the UDP Client:**
     In a new terminal, run:
     ```bash
     java UDPClient <hostname> <port_number>
     ```
     Example:
     ```bash
     java UDPClient localhost 12345
     ```

   Similar to TCP, the UDP client will prompt for commands in the format:
   ```
   PUT:key:value
   GET:key
   DELETE:key
   ```

### Command Line Arguments

For both the **TCP** and **UDP** clients:
- `hostname`: The hostname or IP address of the server (e.g., `localhost` or `127.0.0.1`).
- `port_number`: The port number on which the server is listening.

### Communication Protocol

Each request between the client and the server follows the format:
- **PUT**: `PUT:key:value`
- **GET**: `GET:key`
- **DELETE**: `DELETE:key`

The server responds with:
- `"PUT Successful"` – When a key-value pair is successfully stored.
- The value of the key or `"Key not found"` – When a `GET` request is received.
- `"DELETE Successful"` – When a key is deleted.
- `"Invalid Request"` – For malformed or incorrect requests.

### Error Handling

- **Timeout**: If the client does not receive a response from the server within 3 seconds, it logs the timeout and moves on.
- **Malformed Requests**: If the server receives a malformed request, it logs it and returns an `"Invalid Request"` response to the client.
- **Unresponsive Server**: In case the server does not respond, the client logs a message noting that the server is unresponsive.

### Logging

Both the server and the client log all interactions with a timestamp in milliseconds. Example log entries:
- Client log: `1632851673431 - Sending: PUT:key1:value1`
- Server log: `1632851673432 - Received: PUT:key1:value1 from 127.0.0.1:50432`

### Example Usage

1. **Start the TCP Server**:
   ```bash
   java TCPServer 12345
   ```

2. **Run the TCP Client**:
   ```bash
   java TCPClient localhost 12345
   ```

3. **Example client input**:
   ```
   PUT:name:John
   GET:name
   DELETE:name
   ```

4. **Start the UDP Server**:
   ```bash
   java UDPServer 12345
   ```

5. **Run the UDP Client**:
   ```bash
   java UDPClient localhost 12345
   ```

6. **Example client input**:
   ```
   PUT:age:30
   GET:age
   DELETE:age
   ```

### Known Limitations

- This is a single-threaded implementation, meaning it handles one request at a time. Future improvements will involve multithreading.
- Only basic input validation is performed (e.g., malformed requests).
- Clients do not support advanced error recovery for multiple timeouts; the program simply logs and skips failed requests.

### Future Enhancements

- **Multithreading**: Support multiple concurrent client connections.
- **Advanced protocol handling**: Implement more complex protocols for the communication.
- **RPC Support**: Future versions could include RPC mechanisms for more sophisticated client-server interactions.

# Key-Value Store Application

This Java application implements a simple key-value store server that can handle requests over TCP and UDP protocols. It provides functionalities to store, retrieve, and delete key-value pairs. This README provides detailed instructions on how to run both the server and client applications using the command line terminal, the commands available, and the requirements for running the application.

## Requirements

To run this application, you need the following:

- **Java Development Kit (JDK)**: Version 8 or higher installed on your system. Ensure that the `javac` and `java` commands are available in your terminal.

## Getting Started

## Running the application using bash script

To simplify the process of running the application, a Bash script is provided. This script will compile the necessary Java files, start the server, and launch the client. Follow the steps below to run the application using the script.

Open your terminal and navigate to the directory where the script is located. Make the script executable with the following command:

```bash
   chmod +x runApplication.sh
```

Execute the script by providing the protocol (TCP or UDP) and the port number on which you want the server to listen. For example, to run the server with TCP on port 8080, use the following command:

```bash
./runApplication.sh TCP 8080
```

The server will automatically stop when the client application exits.

## Running the application manually

### Building the Application

1. Open your terminal and navigate to the directory containing the Java source files. Ensure you are in the root directory of the project where the `src` folder is located.

2. Compile the Java classes using the following command:

   ```bash
   javac src/server/*.java
   ```

   This command compiles all Java files in the `src/server` directory and places the compiled classes in the same directory.

3. Make sure to also compile the client classes in the same way:

   ```bash
   javac src/client/*.java
   ```

### Running the Server

To run the server, use the following command format:

```bash
java src.server.Server <protocol> <port>
```

**Parameters**:

- `<protocol>`: The protocol to use (either `TCP` or `UDP`).
- `<port>`: The port number the server will listen on (e.g., `8080`).

Note: The server will prepopulate 10 records in the keystore on starting. For both UDP and TCP instances.

#### Example:

To start the server using TCP on port `8080`:

```bash
java src.server.Server TCP 8080
```

To start the server using UDP on port `8080`:

```bash
java src.server.Server UDP 8080
```

### Running the Client

To run the client application, use the following command format:

```bash
java src.client.Client <protocol> <hostname> <port>
```

**Parameters**:

- `<port>`: The port number on which the server is running (e.g., `8080`).
- `<hostname>`: The hostname or IP address of the server (use `localhost` if running on the same machine).
- `<protocol>`: The protocol to use (either `TCP` or `UDP`). (Note: Make sure its the same as the server you want to interact with)

#### Example:

To start the client connecting to a server on `localhost` and port `8080`:

```bash
java src.client.Client TCP localhost 3000
```

### Client Commands

Once the client application is running, you can interact with the key-value store using the following commands:

1. **PUT**: Store a value under a key.

   - **Format**: `PUT <key> <value>`
   - **Example**:
     ```
     PUT myKey myValue
     ```

2. **GET**: Retrieve the value for a key.

   - **Format**: `GET <key>`
   - **Example**:
     ```
     GET myKey
     ```

3. **DELETE**: Remove a key from the store.

   - **Format**: `DELETE <key>`
   - **Example**:
     ```
     DELETE myKey
     ```

4. **exit**: Exit the client application.
   - **Example**:
     ```
     exit
     ```

### Example Workflow

1. Start the server:

   ```bash
   java src.server.Server TCP 8080
   ```

2. Start the client:

   ```bash
   java src.client.Client 8080 localhost
   ```

3. In the client, execute commands:
   ```bash
   > PUT testKey testValue
   > GET testKey
   > DELETE testKey
   > exit
   ```

### Error Handling

- If you provide an invalid command format, the client will respond with an error message.
- The server logs all operations, including errors, to help you diagnose issues.

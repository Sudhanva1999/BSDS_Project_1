#!/bin/bash
if [ $# -ne 2 ]; then
    echo "Usage: $0 <protocol (TCP/UDP)> <port>"
    exit 1
fi

PROTOCOL=$1
PORT=$2

if [[ "$PROTOCOL" != "TCP" && "$PROTOCOL" != "UDP" ]]; then
    echo "Invalid protocol specified. Please use 'TCP' or 'UDP'."
    exit 1
fi

echo "Compiling Java classes..."
javac src/server/*.java src/client/*.java

if [ $? -ne 0 ]; then
    echo "Compilation failed. Please check your Java code."
    exit 1
fi

echo "Starting the server with $PROTOCOL protocol on port $PORT..."
java src.server.Server $PROTOCOL $PORT &

sleep 2

echo "Starting the client..."
java src.client.Client $PROTOCOL localhost $PORT

pkill -f "java src.server.Server $PROTOCOL $PORT"

echo "Server has been stopped."

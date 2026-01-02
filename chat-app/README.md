# Chat Application

A multi-client chat application built with Java, featuring a client-server architecture with a clean, modern UI.

## Features

- **Multi-client support**: Multiple clients can connect to a single server simultaneously
- **Real-time messaging**: Instant message delivery between clients
- **Modern UI**: Clean, dark-themed interface using Swing
- **Object-oriented design**: Proper separation of concerns with model, view, and controller components
- **Thread-safe operations**: Uses thread pools for handling multiple clients concurrently

## Architecture

The application follows a layered architecture:

```
chat-app/
├── ChatApp.java              # Main application entry point
├── client/
│   ├── ChatClient.java      # Client-side logic
│   ├── MessageListener.java # Interface for message handling
│   └── UI.java              # Client user interface
├── server/
│   ├── ChatServer.java      # Server-side logic
│   ├── ClientHandler.java   # Handles individual client connections
│   └── ClientManager.java   # Manages multiple client connections
├── model/
│   ├── Message.java         # Message data model
│   └── User.java            # User data model
└── util/
    └── Logger.java          # Logging utility
```

## How It Works

1. **Server**: Starts on port 5000 and listens for client connections
2. **Client**: Connects to the server and sends/receives messages
3. **Message Flow**: 
   - Client sends Message object to Server
   - Server receives and broadcasts to all other connected clients
   - Server UI displays messages from all clients
   - Client UI displays messages from server and other clients

## Getting Started

### Prerequisites

- Java 8 or higher
- A Java IDE (optional but recommended)

### Running the Application

1. **Compile the application**:
   ```bash
   javac -d bin chat-app/*.java chat-app/client/*.java chat-app/server/*.java chat-app/model/*.java chat-app/util/*.java
   ```

2. **Run the server**:
   ```bash
   java -cp bin ChatApp
   ```
   Or directly run the ChatServer:
   ```bash
   java -cp bin server.ChatServer
   ```

3. **Run the client(s)**:
   ```bash
   java -cp bin ChatApp
   ```
   Or directly run the ChatClient:
   ```bash
   java -cp bin client.ChatClient
   ```

4. **Choose mode**: When running ChatApp, you'll be prompted to choose between starting a server or client

### Usage

1. Start the server first (port 5000)
2. Start one or more clients
3. Enter a username when prompted
4. Send messages from any client - they will appear on all connected clients and the server

## Components

### Server Components
- **ChatServer**: Main server application with UI
- **ClientHandler**: Handles individual client connections
- **ClientManager**: Manages all connected clients and message broadcasting

### Client Components
- **ChatClient**: Client-side logic and connection management
- **UI**: Client user interface
- **MessageListener**: Interface for handling incoming messages

### Model Components
- **Message**: Represents a chat message with sender, content, and timestamp
- **User**: Represents a chat user with name and ID

### Utility
- **Logger**: Provides logging functionality with timestamps

## Design Patterns Used

- **Observer Pattern**: MessageListener interface for handling incoming messages
- **Singleton Pattern**: ClientManager manages all client connections
- **MVC Pattern**: Separation of model, view, and controller components
- **Thread Pool Pattern**: For handling multiple client connections efficiently

## Security Features

- Object serialization for secure message transmission
- Proper connection management and cleanup
- Thread-safe operations for concurrent access

## License

This project is created for educational purposes. Feel free to use and modify as needed.
package server;

import client.MessageListener;
import model.Message;
import model.User;
import util.Logger;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable, MessageListener {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private User user;
    private ClientManager clientManager;
    private boolean connected = true;

    public ClientHandler(Socket socket, ClientManager clientManager) {
        this.socket = socket;
        this.clientManager = clientManager;
    }

    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            // Get user information
            user = (User) input.readObject();
            clientManager.addClient(this, user);

            Logger.log("Client " + user.getName() + " connected");

            // Listen for messages
            while (connected) {
                try {
                    Message message = (Message) input.readObject();
                    // Log the received message on the server
                    Logger.log("Received message from " + message.getSender().getName() + ": " + message.getContent());
                    clientManager.broadcastMessage(message, this);
                } catch (IOException e) {
                    connected = false;
                    Logger.log("Client " + user.getName() + " disconnected due to read error");
                }
            }
        } catch (Exception e) {
            Logger.log("Error handling client: " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    public void sendMessage(Message message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            Logger.log("Error sending message to client " + user.getName() + ": " + e.getMessage());
            connected = false;
        }
    }

    @Override
    public void onMessageReceived(Message message) {
        sendMessage(message);
    }

    @Override
    public void onConnectionClosed() {
        disconnect();
    }

    public void disconnect() {
        connected = false;
        if (clientManager != null) {
            clientManager.removeClient(this);
        }
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            Logger.log("Error closing client connection: " + e.getMessage());
        }
        Logger.log("Client " + user.getName() + " disconnected");
    }

    public User getUser() {
        return user;
    }
}
package server;

import model.Message;
import model.User;
import util.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ClientManager {
    private Map<ClientHandler, User> clients;
    private ChatServer server;

    public ClientManager() {
        this.clients = new ConcurrentHashMap<>();
    }

    public void setServer(ChatServer server) {
        this.server = server;
    }

    public void addClient(ClientHandler clientHandler, User user) {
        clients.put(clientHandler, user);
        Logger.log("Added client " + user.getName() + ". Total clients: " + clients.size());
    }

    public void removeClient(ClientHandler clientHandler) {
        User user = clients.remove(clientHandler);
        if (user != null) {
            Logger.log("Removed client " + user.getName() + ". Total clients: " + clients.size());
        }
        clientHandler.disconnect();
    }

    public void broadcastMessage(Message message, ClientHandler sender) {
        Logger.log("Broadcasting message from " + message.getSender().getName() + ": " + message.getContent());

        // Notify the server UI about the received message
        if (server != null) {
            server.onMessageReceived(message);
        }

        for (Map.Entry<ClientHandler, User> entry : clients.entrySet()) {
            ClientHandler client = entry.getKey();
            // Don't send the message back to the sender
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    public int getClientCount() {
        return clients.size();
    }

    public Collection<User> getUsers() {
        return clients.values();
    }

    public void disconnectAll() {
        for (ClientHandler client : clients.keySet()) {
            client.disconnect();
        }
        clients.clear();
    }
}
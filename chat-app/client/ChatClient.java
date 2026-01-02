package client;

import model.Message;
import model.User;
import util.Logger;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatClient implements MessageListener {
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket socket;
    private User user;
    private UI ui;
    private ExecutorService executor;

    public ChatClient(String username) {
        this.user = new User(username);
        this.executor = Executors.newSingleThreadExecutor();

        // Initialize UI
        this.ui = new UI(this);
        connect();
    }


    private void connect() {
        executor.submit(() -> {
            try {
                socket = new Socket("localhost", 5000);
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());

                // Send user information to server
                output.writeObject(user);
                output.flush();

                Logger.log("Connected to server as " + user.getName());

                // Listen for messages from server
                while (!socket.isClosed()) {
                    try {
                        Object obj = input.readObject();
                        if (obj instanceof Message) {
                            Message message = (Message) obj;
                            onMessageReceived(message);
                        }
                    } catch (ClassNotFoundException e) {
                        Logger.error("Error reading message: " + e.getMessage());
                    }
                }
            } catch (IOException e) {
                Logger.error("Connection error: " + e.getMessage());
                if (ui != null) {
                    ui.appendMessage("Error: No server found.");
                } else {
                    System.out.println("Error: No server found.");
                }
            }
        });
    }

    // This method is now handled by the UI class
    // The UI class will call this method when the user sends a message
    public void sendClientMessage(String msg) {
        if (!msg.isEmpty()) {
            // Create a message object and send it
            Message message = new Message(user, msg);
            sendMessage(message);
            if (ui != null) {
                ui.displayMessage(message);
            } else {
                // Fallback to console output if UI is not available
                System.out.println("You: " + msg);
            }
        }
    }

    public void sendMessage(Message message) {
        try {
            if (output != null) {
                output.writeObject(message);
                output.flush();
            }
        } catch (IOException e) {
            Logger.error("Error sending message: " + e.getMessage());
        }
    }

    @Override
    public void onMessageReceived(Message message) {
        if (ui != null) {
            ui.displayMessage(message);
        } else {
            // Fallback to console output if UI is not available
            System.out.println(message.getSender().getName() + ": " + message.getContent());
        }
    }

    @Override
    public void onConnectionClosed() {
        Logger.log("Connection closed");
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            Logger.error("Error closing socket: " + e.getMessage());
        }
    }

    public User getUser() {
        return user;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChatClient("DefaultUser");
        });
    }
}

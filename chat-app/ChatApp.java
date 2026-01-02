import client.ChatClient;
import server.ChatServer;

import javax.swing.*;
import java.awt.*;

public class ChatApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create a simple dialog to choose between client and server
            Object[] options = {"Start Server", "Start Client", "Cancel"};
            int choice = JOptionPane.showOptionDialog(
                null,
                "Choose an option:",
                "Chat Application",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );

            switch (choice) {
                case 0: // Start Server
                    new ChatServer();
                    break;
                case 1: // Start Client
                    String username = JOptionPane.showInputDialog(null, "Enter your username:", "Username", JOptionPane.PLAIN_MESSAGE);
                    if (username != null && !username.trim().isEmpty()) {
                        new ChatClient(username.trim());
                    } else {
                        JOptionPane.showMessageDialog(null, "Username is required!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case 2: // Cancel
                default:
                    System.exit(0);
                    break;
            }
        });
    }
}

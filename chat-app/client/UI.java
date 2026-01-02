package client;

import model.Message;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UI extends JFrame {
    private JTextArea messageArea;
    private JTextField inputField;
    private ChatClient client;

    public UI(ChatClient client) {
        this.client = client;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Chat Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);

        inputField = new JTextField();
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void sendMessage() {
        String text = inputField.getText().trim();
        if (!text.isEmpty()) {
            // Use the client's method to send the message
            client.sendClientMessage(text);
            inputField.setText("");
        }
    }

    public void displayMessage(Message message) {
        messageArea.append(message.getSender().getName() + ": " + message.getContent() + "\n");
    }

    // Method to get the client's user (need to add this method to ChatClient too)
    public void appendMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            messageArea.append(message + "\n");
        });
    }
}
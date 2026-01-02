package server;

import client.MessageListener;
import model.Message;
import model.User;
import util.Logger;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer extends JFrame implements MessageListener {
    private JPanel chatContainer;
    private JScrollPane scrollPane;
    private JTextField messageField;
    private JLabel statusLabel;
    private ServerSocket serverSocket;
    private ClientManager clientManager;
    private ExecutorService executor;

    // Shadcn Zinc Palette
    private final Color BG_BASE = new Color(9, 9, 11);
    private final Color BORDER = new Color(39, 39, 42);
    private final Color TEXT_PRIMARY = new Color(250, 250, 250);
    private final Color ACCENT_PRIMARY = new Color(250, 250, 250);
    private final Color ACCENT_TEXT = new Color(9, 9, 11);

    public ChatServer() {
        this.clientManager = new ClientManager();
        this.clientManager.setServer(this);  // Set the server reference in ClientManager
        this.executor = Executors.newCachedThreadPool();

        setTitle("G4 // Server");
        setSize(450, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_BASE);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_BASE);
        header.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER), new EmptyBorder(0, 24, 0, 24)));
        header.setPreferredSize(new Dimension(0, 80));

        JLabel title = new JLabel("Server");
        title.setFont(new Font("Inter", Font.BOLD, 16));
        title.setForeground(TEXT_PRIMARY);

        statusLabel = new JLabel("● Standby");
        statusLabel.setForeground(new Color(161, 161, 170));
        statusLabel.setFont(new Font("Inter", Font.BOLD, 12));

        header.add(title, BorderLayout.WEST);
        header.add(statusLabel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Chat Container
        chatContainer = new JPanel();
        chatContainer.setLayout(new BoxLayout(chatContainer, BoxLayout.Y_AXIS));
        chatContainer.setBackground(BG_BASE);
        chatContainer.setBorder(new EmptyBorder(20, 20, 20, 20));

        scrollPane = new JScrollPane(chatContainer);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BG_BASE);
        add(scrollPane, BorderLayout.CENTER);

        // Input
        JPanel footer = new JPanel(new BorderLayout(12, 0));
        footer.setBackground(BG_BASE);
        footer.setBorder(new EmptyBorder(20, 24, 40, 24));

        messageField = new JTextField();
        messageField.setBackground(BG_BASE);
        messageField.setForeground(TEXT_PRIMARY);
        messageField.setCaretColor(TEXT_PRIMARY);
        messageField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(10, 16, 10, 16)));

        JButton sendButton = new JButton("Send");
        sendButton.setBackground(ACCENT_PRIMARY);
        sendButton.setForeground(ACCENT_TEXT);
        sendButton.setFocusPainted(false);
        sendButton.setBorder(new EmptyBorder(0, 20, 0, 20));

        footer.add(messageField, BorderLayout.CENTER);
        footer.add(sendButton, BorderLayout.EAST);
        add(footer, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());

        setVisible(true);
        startServer();
    }

    private void addBubble(String text, boolean isMe) {
        SwingUtilities.invokeLater(() -> {
            JPanel wrapper = new JPanel(new FlowLayout(isMe ? FlowLayout.RIGHT : FlowLayout.LEFT));
            wrapper.setBackground(BG_BASE);
            wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

            JPanel bubble = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(isMe ? ACCENT_PRIMARY : BORDER);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.dispose();
                }
            };
            bubble.setLayout(new BorderLayout());
            bubble.setOpaque(false);
            bubble.setBorder(new EmptyBorder(8, 14, 8, 14));

            JLabel label = new JLabel("<html><p style='width: 180px;'>" + text + "</p></html>");
            label.setForeground(isMe ? ACCENT_TEXT : TEXT_PRIMARY);
            bubble.add(label);

            wrapper.add(bubble);
            chatContainer.add(wrapper);
            chatContainer.add(Box.createVerticalStrut(10));
            chatContainer.revalidate();
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        });
    }

    private void startServer() {
        executor.submit(() -> {
            try {
                serverSocket = new ServerSocket(5000);
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("● Listening on port 5000");
                    statusLabel.setForeground(new Color(34, 197, 94));
                    addBubble("Server started on port 5000", true);
                });

                while (!serverSocket.isClosed()) {
                    Socket socket = serverSocket.accept();
                    Logger.log("New client connected: " + socket.getRemoteSocketAddress());

                    ClientHandler clientHandler = new ClientHandler(socket, clientManager);
                    executor.submit(clientHandler);
                }
            } catch (IOException e) {
                Logger.error("Server error: " + e.getMessage());
                SwingUtilities.invokeLater(() -> addBubble("System: Server error occurred.", false));
            }
        });
    }

    private void sendMessage() {
        String msg = messageField.getText().trim();
        if (!msg.isEmpty()) {
            // Create a system message from the server
            User serverUser = new User("Server");
            Message message = new Message(serverUser, msg);

            addBubble(msg, true);
            // Broadcast the server message to all clients
            clientManager.broadcastMessage(message, null);
            messageField.setText("");
        }
    }

    @Override
    public void onMessageReceived(Message message) {
        // Display the received message in the server UI
        SwingUtilities.invokeLater(() -> {
            addBubble(message.getSender().getName() + ": " + message.getContent(), false);
        });
    }

    @Override
    public void onConnectionClosed() {
        // Handle connection closed event if needed
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatServer());
    }
}

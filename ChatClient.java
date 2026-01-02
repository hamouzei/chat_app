/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatapp;

/**
 *
 * @author HP
 */ 
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ChatClient extends JFrame {
    private JPanel chatContainer;
    private JScrollPane scrollPane;
    private JTextField messageField;
    private PrintWriter out;

    private final Color BG_BASE = new Color(9, 9, 11);
    private final Color BORDER = new Color(39, 39, 42);
    private final Color TEXT_PRIMARY = new Color(250, 250, 250);
    private final Color ACCENT_PRIMARY = new Color(250, 250, 250);
    private final Color ACCENT_TEXT = new Color(9, 9, 11);

    public ChatClient() {
        setTitle("G4 // Client");
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

        JLabel title = new JLabel("Messenger");
        title.setFont(new Font("Inter", Font.BOLD, 16));
        title.setForeground(TEXT_PRIMARY);
        
        JLabel status = new JLabel("● Secure Line");
        status.setForeground(new Color(34, 197, 94));
        status.setFont(new Font("Inter", Font.BOLD, 12));

        header.add(title, BorderLayout.WEST);
        header.add(status, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Chat
        chatContainer = new JPanel();
        chatContainer.setLayout(new BoxLayout(chatContainer, BoxLayout.Y_AXIS));
        chatContainer.setBackground(BG_BASE);
        chatContainer.setBorder(new EmptyBorder(20, 20, 20, 20));

        scrollPane = new JScrollPane(chatContainer);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BG_BASE);
        add(scrollPane, BorderLayout.CENTER);

        // Footer
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
        connect();
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

    private void connect() {
        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 5000);
                out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                String msg;
                while ((msg = in.readLine()) != null) {
                    addBubble(msg, false);
                }
            } catch (IOException e) {
                addBubble("Error: No server found.", false);
            }
        }).start();
    }

    private void sendMessage() {
        String msg = messageField.getText().trim();
        if (!msg.isEmpty()) {
            addBubble(msg, true);
            if (out != null) out.println(msg);
            messageField.setText("");
        }
    }

    public static void main(String[] args) { new ChatClient(); }
}

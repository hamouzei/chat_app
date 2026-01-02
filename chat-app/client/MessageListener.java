package client;

import java.io.*;
import model.Message;

public interface MessageListener {
    void onMessageReceived(Message message);
    void onConnectionClosed();
}
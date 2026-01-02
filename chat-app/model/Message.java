package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private User sender;
    private String content;
    private LocalDateTime timestamp;
    
    public Message(User sender, String content) {
        this.sender = sender;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }
    
    public User getSender() {
        return sender;
    }
    
    public void setSender(User sender) {
        this.sender = sender;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "[" + timestamp + "] " + sender.getName() + ": " + content;
    }
}
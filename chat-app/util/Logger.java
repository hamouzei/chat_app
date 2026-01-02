package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public static void log(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.println("[LOG " + timestamp + "] " + message);
    }
    
    public static void error(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.err.println("[ERROR " + timestamp + "] " + message);
    }
    
    public static void warn(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.err.println("[WARN " + timestamp + "] " + message);
    }
}
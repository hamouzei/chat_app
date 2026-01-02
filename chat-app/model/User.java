package model;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String id;
    
    public User(String name) {
        this.name = name;
        this.id = generateId();
    }
    
    private String generateId() {
        return "user_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 10000);
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getId() {
        return id;
    }
    
    @Override
    public String toString() {
        return "User{name='" + name + "', id='" + id + "'}";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return id != null ? id.equals(user.id) : user.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
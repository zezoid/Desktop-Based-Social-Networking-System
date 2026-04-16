package model;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private int userId;
    private String username;

    public User(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public int getId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}

package model;

import java.io.Serializable;

public class Message implements Serializable {

    private int id;
    private int senderId;
    private int receiverId;
    private String text;

    public Message(int id, int senderId, int receiverId, String text) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.text = text;
    }

    public int getId() { return id; }
    public int getSenderId() { return senderId; }
    public int getReceiverId() { return receiverId; }
    public String getText() { return text; }
}
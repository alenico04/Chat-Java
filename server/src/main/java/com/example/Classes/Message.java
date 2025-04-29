package com.example.Classes;

import java.util.Date;

public class Message {

    private int chatId;
    private String text;
    private Date created_at;
    private String username;
    private int user_id;
    public final String type = "message";

    public Message(){
        
    }

    public Message(int chatId, String text, Date created_at, String username, int user_id){
        this.chatId = chatId;
        this.text = text;
        this.created_at = created_at;
        this.username = username;
        this.user_id = user_id;
    }

    // ---------------------- SETTER e GETTER
    public int getChatId() {
        return chatId;
    }
    public void setChatId(int chatId) {
        this.chatId = chatId;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public Date getCreated_at() {
        return created_at;
    }
    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}

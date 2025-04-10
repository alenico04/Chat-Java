package client;

import java.util.Date;

public class Message {

    Chat chat;
    String text;
    Date created_at;
    String username;
    String user_id;

    public Message(Chat chat, String text, Date created_at, String username, String user_id){
        this.chat = chat;
        this.text = text;
        this.created_at = created_at;
        this.username = username;
        this.user_id = user_id;
    }

    // ---------------------- SETTER e GETTER
    
    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}

package client;

import java.util.Date;

public class Message {
    private int chatId;
    private String text;
    private Date created_at;
    private String username;
    private int userId;
    public final String type = "message";

    public Message(int chatId, String text, Date created_at, String username, int userId){
        this.chatId = chatId;
        this.text = text;
        this.created_at = created_at;
        this.username = username;
        this.userId = userId;
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
    public int getUserId() {
        return userId;
    }
    public void setUserId(int user_id) {
        this.userId = user_id;
    }
}

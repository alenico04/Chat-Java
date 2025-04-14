package client.chat;

import client.Chat;
import client.Message;
import javafx.scene.layout.BorderPane;


public class ChatPage extends BorderPane {

    private int headerHeight = 150;
    private Header header;
    private MessageList messageList;
    private Chat chat;
    private Message[] messages;

    public ChatPage( Chat chat, Message[] messages ) {
        Header header = new Header(headerHeight);
        this.setTop(header);
        this.setCenter(messageList);

        this.header = header;
        this.chat = chat;
    }

    // Getters and Setters
    public Chat getChat() {
        return chat; 
    }

    public void setChat(Chat chat) {
        this.chat = chat; 
    }

    public Message[] getMessages() {
        return messages; 
    }

    public void setMessages(Message[] messages) {
        this.messages = messages;    
    }

    public int getHeaderHeight() {
        return headerHeight;
    }

    public void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public MessageList getMessageList() {
        return messageList;
    }

    public void setMessageList(MessageList messageList) {
        this.messageList = messageList;
    }
}

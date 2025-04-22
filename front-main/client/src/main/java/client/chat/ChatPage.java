package client.chat;

import client.Chat;
import client.Message;
import client.User;
import javafx.scene.layout.BorderPane;


public class ChatPage extends BorderPane {

    private int headerHeight = 150;
    private Header header;
    private MessageList messageList;
    private MessageInput messageInput;
    private Chat chat;
    private Message[] messages;
    private User currentUser;

    public ChatPage(User currentUser, Chat chat, Message[] messages ) {
        Header header = new Header(headerHeight, chat);
        BorderPane body = new BorderPane();
        body.prefHeightProperty().bind(this.heightProperty().subtract(this.headerHeight));
        
        this.setTop(header);
        this.setCenter(body);

        MessageList messageList = new MessageList(messages, currentUser);
        MessageInput messageInput = new MessageInput(chat, 100);
        body.setCenter(messageList);
        body.setBottom(messageInput);

        this.header = header;
        this.chat = chat;
        this.messages = messages;
        this.currentUser = currentUser;
        this.messageList = messageList;
        this.messageInput = messageInput;
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

    public User getCurrentUser() {
        return currentUser; 
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser; 
    }

    public MessageInput getMessageInput() {
        return messageInput; 
    }

    public void setMessageInput(MessageInput messageInput) {
        this.messageInput = messageInput;
    }
}

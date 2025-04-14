package client.homepage;

import client.Chat;
import client.User;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class HomePage extends BorderPane {
    
    private HBox header;
    private BorderPane body;
    private UserPanel userPanel;
    private ChatList chatList;
    private int headerHeight = 150;
    private int userPanelWidth = 300;

    public HomePage( User currentUser, Chat[] chats ) {
        Header header = new Header(headerHeight);
        BorderPane body = new BorderPane();
        body.prefHeightProperty().bind(this.heightProperty().subtract(this.headerHeight));

        UserPanel userPanel = new UserPanel(currentUser, this.userPanelWidth);
        ChatList chatList = new ChatList(chats);

        this.setTop(header);
        this.setCenter(body);

        body.setLeft(userPanel);
        body.setCenter(chatList);

        this.header = header;
        this.body = body;
        this.chatList = chatList;
        this.userPanel = userPanel;
    }

// -------------------SETTER E GETTER------------------------

    public HBox getHeader() {
        return header;
    }

    public void setHeader(HBox header) {
        this.header = header;
    }

    public BorderPane getBody() {
        return body;
    }

    public void setBody(BorderPane body) {
        this.body = body;
    }

    public int getHeaderHeight() {
        return headerHeight;
    }

    public void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
    }

    public int getUserPanelWidth() {
        return userPanelWidth;
    }

    public void setUserPanelWidth(int userPanelWidth) {
        this.userPanelWidth = userPanelWidth;
    }
    
    public UserPanel getUserPanel() {
        return userPanel;
    }

    public void setUserPanel(UserPanel userPanel) {
        this.userPanel = userPanel;
    }

    public ChatList getChatList() {
        return chatList;
    }

    public void setChatList(ChatList chatList) {
        this.chatList = chatList;
    }
}
package client.chat;

import javafx.scene.control.ScrollPane;
import client.Message;
import client.User;
import javafx.scene.layout.Region;

public class MessageList extends ScrollPane{
    public MessageList(Message[] messages, User currentUser) {
        this.setMinWidth(100);
        this.setFitToWidth(true);
        this.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); 
        this.setStyle("-fx-background-color: rgb(166, 184, 255); -fx-background: transparent; -fx-padding: 5;");

        this.parentProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.prefHeightProperty().bind(((Region)newValue).heightProperty());
            }
        });
    }
}

package client.homepage;

import client.Chat;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ChatList extends ScrollPane {
    public ChatList(Chat[] chats) {
        // Configurazione base dello ScrollPane
        this.setMinWidth(100);
        this.parentProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.prefHeightProperty().bind(((Region)newValue).heightProperty());
                this.prefWidthProperty().bind(((Region)newValue).widthProperty());
            }
        });

        this.setFitToWidth(true);
        this.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Hide horizontal scrollbar
        this.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Show vertical scrollbar when needed
        this.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-padding: 5;");
        
        // Create a VBox to hold all chat items
        VBox chatItemsContainer = new VBox(5); // 5 is the spacing between items
        chatItemsContainer.setAlignment(Pos.TOP_CENTER);
        chatItemsContainer.prefWidthProperty().bind(this.widthProperty().subtract(20)); // Subtract some padding
        
        // Add chat items to the container
        if (chats != null && chats.length > 0) {
            for (Chat chat : chats) {
                ChatItem chatItem = new ChatItem(chat, 10);
                chatItemsContainer.getChildren().add(chatItem);
            }
        }
        
        // Set the VBox as content of this ScrollPane
        this.setContent(chatItemsContainer);
    }
    
}
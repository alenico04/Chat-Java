package client.homePage;

import client.Chat;
import client.utils.ImageUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Creates an HBox representing a chat item with the specified design
 * @param chat The chat to display
 * @return HBox containing the chat item UI
 */
public class ChatItem extends HBox {
    private Chat chat;

    public ChatItem(Chat chat, double spacing) {
        this.setSpacing(spacing);
        this.setPrefHeight(150);
        this.prefWidthProperty().bind(this.widthProperty().subtract(40));
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(10, 15, 10, 15));
        this.setStyle("-fx-background-color: white; -fx-background-radius: 10");

        // Create circular photo
        int photoSize = 100;
        StackPane photoContainer = ImageUtils.createCircularImage(chat.getPhoto(), photoSize);

        // Create chat name label
        Label nameLabel = new Label(chat.getName());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");

        // Add components to the chat item
        this.getChildren().addAll(photoContainer, nameLabel);

        // Make the chat item clickable
        this.setOnMouseClicked(event -> handleChatItemClick());

        // Add hover effect to indicate it's clickable
        this.setOnMouseEntered(event -> this.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 10; -fx-cursor: hand;"));
        this.setOnMouseExited(event -> this.setStyle("-fx-background-color: white; -fx-background-radius: 10;"));
    
        this.chat = chat;
    }

    private void handleChatItemClick() {
        System.out.println("Chat clicked: " + chat.getId());
        Stage stage = (Stage) this.getScene().getWindow();

        // stage.setScene(new ChatPage(null, chat, null));
    }
}

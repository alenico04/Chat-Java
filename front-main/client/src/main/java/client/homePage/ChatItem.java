package client.homepage;

import client.Chat;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

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
        ImageView photoView = new ImageView();
        
        try {
            // Try to load the chat photo
            Image chatImage = new Image(chat.getPhoto(), true);
            photoView.setImage(chatImage);
        } catch (Exception e) {
            // If image loading fails, could use a default image here
            System.err.println("Failed to load chat image: " + e.getMessage());
        }
        
        photoView.setFitWidth(photoSize);
        photoView.setFitHeight(photoSize);
        
        // Create a circle clip for the image
        Circle clip = new Circle(photoSize / 2);
        clip.setCenterX(photoSize / 2);
        clip.setCenterY(photoSize / 2);
        photoView.setClip(clip);
        
        // Create chat name label
        Label nameLabel = new Label(chat.getName());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");

        // Add components to the chat item
        this.getChildren().addAll(photoView, nameLabel);

        // Make the chat item clickable
        this.setOnMouseClicked(event -> handleChatItemClick());

        // Add hover effect to indicate it's clickable
        this.setOnMouseEntered(event -> this.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 10; -fx-cursor: hand;"));
        this.setOnMouseExited(event -> this.setStyle("-fx-background-color: white; -fx-background-radius: 10;"));
    
        this.chat = chat;
    }

    private void handleChatItemClick() {
        System.out.println("Chat clicked: " + chat.getId());
    }
}
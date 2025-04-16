package client.chat;

import client.Chat;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class MessageInput extends HBox {
    private Chat chat;
    private int height;

    public MessageInput(Chat chat, int height) {
        this.setPrefHeight(height);
        this.setStyle("-fx-background-color: rgb(255, 255, 255); -fx-background: transparent; -fx-padding: 5;");

        TextField textField = new TextField();
        textField.setPromptText("Type your message here...");
        textField.prefHeightProperty().bind(this.heightProperty());
        textField.prefWidthProperty().bind(this.prefWidthProperty());
        
        Button sendButton = new Button("Invia");
        sendButton.prefHeightProperty().bind(this.heightProperty());
        sendButton.prefWidthProperty().bind(this.heightProperty());
        sendButton.setOnAction(e -> {
            String message = textField.getText();
            if (!message.isEmpty()) {
                this.sendMessage(message);
                textField.clear();
            }
        });

        this.getChildren().addAll(textField, sendButton);

        this.chat = chat;
        this.height = height;
    }

    public void sendMessage(String message) {
        // TODO: Implementa la logica per inviare il messaggio
        System.out.println("Messaggio inviato: " + message);
    }

    // ----------------- Getter e setter
    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public int getMessageInputHeight() {
        return height;
    }

    public void setMessageInputHeight(int height) {
        this.height = height;
    }
}

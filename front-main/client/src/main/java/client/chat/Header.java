package client.chat;

import client.Chat;
import client.User;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Header extends HBox{
    private int height;

    public Header(int height, Chat chat) {

        this.setPrefHeight(height);
        this.prefWidthProperty().bind(this.widthProperty());
        this.setStyle("-fx-background-color:rgb(47, 220, 119);");
        this.setAlignment(Pos.CENTER_LEFT);

        this.height = height;

        Button backButton = new Button("←");
        backButton.setPrefHeight(100);
        backButton.setStyle("-fx-background-color: transparent; -fx-background-radius: 0; -fx-border-color: transparent; -fx-border-radius: 0; -fx-cursor: hand;");
        backButton.setOnAction(event -> handleBackButtonClick());

        // Title container
        VBox rightContainer = new VBox();
        rightContainer.setAlignment(Pos.CENTER_RIGHT);
        this.setHgrow(rightContainer, Priority.ALWAYS);
        this.setMargin(rightContainer, new javafx.geometry.Insets(0, 30, 0, 0));

        // Upper side - Title
        Text title = new Text(chat.getName());
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        rightContainer.getChildren().add(title);
        
        // Lower side - Subtitle
        User[] users = chat.getUsers();
        if (users != null){
            String usersList = "";
            for (User user : users) {
                usersList += user;
            }
            Text usersLisText = new Text(usersList);
            rightContainer.getChildren().add(usersLisText);
        }
        
        // Add all components
        this.getChildren().addAll(backButton, rightContainer);
    }

    private void handleBackButtonClick() {
        // TODO: Implement the logic for handling the back button click
        System.out.println("Pulsante back cliccato, ma nessuna azione è stata definita");
    }
}

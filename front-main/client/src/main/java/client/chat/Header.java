package client.chat;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class Header extends HBox{
    int height;

    public Header(int height) {

        this.setPrefHeight(height);
        this.prefWidthProperty().bind(this.widthProperty());
        this.setStyle("-fx-background-color:rgb(47, 220, 119);");
        this.setAlignment(Pos.CENTER_LEFT);

        this.height = height;

        Button backButton = new Button("←");
        backButton.setPrefHeight(100);
        backButton.setStyle("-fx-background-color: transparent; -fx-background-radius: 0; -fx-border-color: transparent; -fx-border-radius: 0; -fx-cursor: hand;");

        // Title container
        VBox rightContainer = new VBox();
        rightContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(rightContainer, Priority.ALWAYS);
        this.setMargin(rightContainer, new javafx.geometry.Insets(0, 30, 0, 0));

        // Upper side - Title
        Text title = new Text("JAVA CHAT");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        this.setMargin(title, new javafx.geometry.Insets(0, 0, 0, 30));

        // Right side - Search components
        TextField searchField = new TextField();
        searchField.setPromptText("Inserisci nome di utente o gruppo");
        searchField.setPrefHeight(45);
        
        // Add all components
        rightContainer.getChildren().addAll(searchField, searchButton);
        this.getChildren().addAll(title, rightContainer);
    }
}
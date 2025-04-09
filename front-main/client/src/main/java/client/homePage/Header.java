package client.homePage;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

public class Header extends HBox {
    int headerHeight = 150;

    public Header() {
        this.setPrefHeight(this.headerHeight);
        this.prefWidthProperty().bind(this.widthProperty());
        this.setStyle("-fx-background-color:rgb(255, 146, 146);");
        this.setAlignment(Pos.CENTER_LEFT); // Add this line
        
        // Left side - Title
        Text title = new Text("JAVA CHAT");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        this.setMargin(title, new javafx.geometry.Insets(0, 0, 0, 30));

        // Right side container
        HBox rightContainer = new HBox();
        rightContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(rightContainer, Priority.ALWAYS);
        this.setMargin(rightContainer, new javafx.geometry.Insets(0, 30, 0, 0));
        
        // Right side - Search components
        TextField searchField = new TextField();
        searchField.setPromptText("Inserisci nome di utente o gruppo");
        searchField.setPrefHeight(45);
        
        Button searchButton = new Button("+");
        searchButton.setPrefHeight(searchField.getPrefHeight());
        searchButton.setPrefWidth(searchField.getPrefHeight());

        // Add all components
        rightContainer.getChildren().addAll(searchField, searchButton);
        this.getChildren().addAll(title, rightContainer);
    }
}
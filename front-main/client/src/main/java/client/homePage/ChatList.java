package client.homePage;

import client.Chat;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

public class ChatList extends FlowPane {
    private int userPanelWidth = 450;

    public ChatList(ReadOnlyDoubleProperty fatherHeight, ReadOnlyDoubleProperty fatherWidth, Chat[] chats) {
        this.setMinWidth(100);
        this.prefWidthProperty().bind(fatherWidth.subtract(userPanelWidth));
        this.prefHeightProperty().bind(fatherHeight);
        this.setStyle("-fx-background-color:rgb(98, 255, 237);");
        this.setAlignment(javafx.geometry.Pos.CENTER);

        Button button = new Button("prova");
        this.getChildren().add(button);
    }


}
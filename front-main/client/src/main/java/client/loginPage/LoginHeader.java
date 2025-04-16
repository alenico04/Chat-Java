package client.loginPage;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.beans.property.ReadOnlyDoubleProperty;

public class LoginHeader extends StackPane {
    private Rectangle headerBackground;
    private Text title;

    public LoginHeader(ReadOnlyDoubleProperty sceneWidth) {
        createHeader(sceneWidth);
    }

    private void createHeader(ReadOnlyDoubleProperty sceneWidth) {
        headerBackground = new Rectangle();
        headerBackground.widthProperty().bind(sceneWidth);
        headerBackground.heightProperty().bind(sceneWidth.divide(6));
        headerBackground.setFill(Color.LIGHTGRAY);

        title = new Text("JavaFX Chat!");
        title.setFont(Font.font("Verdana", 20));

        this.getChildren().addAll(headerBackground, title);
        this.setAlignment(Pos.CENTER);
    }
}
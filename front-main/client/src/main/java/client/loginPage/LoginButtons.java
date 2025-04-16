package client.loginPage;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.beans.property.ReadOnlyDoubleProperty;

public class LoginButtons extends HBox {
    private Button continueButton;
    private Button cancelButton;

    public LoginButtons(ReadOnlyDoubleProperty formWidth, ReadOnlyDoubleProperty formHeight) {
        createButtons();
        styleButtons(formWidth, formHeight);
        setupLayout(formWidth);
    }

    private void createButtons() {
        continueButton = new Button("Continue");
        cancelButton = new Button("Cancel");
    }

    private void styleButtons(ReadOnlyDoubleProperty formWidth, ReadOnlyDoubleProperty formHeight) {
        String buttonStyle = "-fx-font-family: 'Verdana'; -fx-font-size: 12; -fx-background-radius: 20; -fx-cursor: hand;";
        cancelButton.setStyle(buttonStyle + "-fx-background-color: #ff6b6b;");
        continueButton.setStyle(buttonStyle + "-fx-background-color: #51cf66;");

        DropShadow buttonShadow = new DropShadow();
        buttonShadow.setRadius(5.0);
        buttonShadow.setOffsetX(2.0);
        buttonShadow.setOffsetY(2.0);
        buttonShadow.setColor(Color.color(0.4, 0.4, 0.4, 0.3));
        
        cancelButton.setEffect(buttonShadow);
        continueButton.setEffect(buttonShadow);

        // Keep width at 0.2 but increase height from 0.1 to 0.13
        cancelButton.prefWidthProperty().bind(formWidth.multiply(0.2));
        continueButton.prefWidthProperty().bind(formWidth.multiply(0.2));
        cancelButton.prefHeightProperty().bind(formHeight.multiply(0.15));
        continueButton.prefHeightProperty().bind(formHeight.multiply(0.15));

        // Add minimum height to ensure buttons don't get too thin
        cancelButton.setMinHeight(35);
        continueButton.setMinHeight(35);
    }

    private void setupLayout(ReadOnlyDoubleProperty formWidth) {
        this.setAlignment(Pos.CENTER);
        // Reduce spacing between buttons from 0.2 to 0.1
        this.spacingProperty().bind(formWidth.multiply(0.1));
        this.getChildren().addAll(cancelButton, continueButton);
    }

    public Button getContinueButton() { return continueButton; }
    public Button getCancelButton() { return cancelButton; }
}
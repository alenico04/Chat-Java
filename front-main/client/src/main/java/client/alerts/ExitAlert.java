package client.alerts;

import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Modality;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.Scene;

import java.util.concurrent.CompletableFuture;

public class ExitAlert {
    private final Stage mainStage;
    private Stage alertStage;
    private final GaussianBlur blur;
    private boolean isShowing;
    private ButtonType stayButton;
    private ButtonType exitButton;
    private VBox dialogContent;
    private javafx.scene.control.Button stayBtn;
    private javafx.scene.control.Button exitBtn;

    public ExitAlert(Stage stage) {
        this.mainStage = stage;
        this.blur = new GaussianBlur(5);
        initializeAlert();
    }

    private void initializeAlert() {
        // Create custom dialog stage
        alertStage = new Stage(StageStyle.TRANSPARENT);
        alertStage.initModality(Modality.APPLICATION_MODAL);
        alertStage.initOwner(mainStage);

        setupContent();
        setupButtons();

        // Create scene with transparent background
        Scene scene = new Scene(dialogContent);
        scene.setFill(Color.TRANSPARENT);
        alertStage.setScene(scene);

        styleAlert();
    }

    private void setupContent() {
        dialogContent = new VBox(20);
        dialogContent.setAlignment(Pos.CENTER);
        dialogContent.setPadding(new Insets(30));
                dialogContent.setPrefWidth(600);
        dialogContent.setPrefHeight(337.5);
        dialogContent.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #cccccc;" +
            "-fx-border-width: 2px;" +
            "-fx-background-radius: 15px;" +
            "-fx-border-radius: 15px;"
        );

        Label headerLabel = new Label("Are you sure you want to exit?");
        headerLabel.setFont(Font.font("Verdana", 18));
        
        Label messageLabel = new Label("Click 'Stay' to return to the chat\nor 'Exit' to close the application.");
        messageLabel.setFont(Font.font("Verdana", 14));
        messageLabel.setStyle("-fx-text-fill: #666666;");
        
        Label warningLabel = new Label("Any unsaved changes will be lost!");
        warningLabel.setFont(Font.font("Verdana", 12));
        warningLabel.setStyle("-fx-text-fill: #ff6b6b;");

        // Create buttons
        stayButton = new ButtonType("Stay", ButtonData.CANCEL_CLOSE);
        exitButton = new ButtonType("Exit", ButtonData.OK_DONE);

        dialogContent.getChildren().addAll(
            headerLabel, 
            messageLabel, 
            warningLabel
        );
    }

    private void setupButtons() {
        // Create button container
        Region spacer = new Region();
        spacer.setPrefHeight(20);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        String buttonStyle = "-fx-font-family: 'Verdana';" +
                            "-fx-font-size: 14px;" +
                            "-fx-background-radius: 20;" +
                            "-fx-min-width: 150px;" +    // Aumentato da 100px a 150px
                            "-fx-min-height: 35px;" +    // Diminuito da 40px a 35px
                            "-fx-max-height: 35px;";     // Aggiunto per mantenere l'altezza fissa

        stayBtn = new javafx.scene.control.Button("Stay");
        exitBtn = new javafx.scene.control.Button("Exit");

        stayBtn.setStyle(buttonStyle + "-fx-background-color: #51cf66; -fx-text-fill: white;");
        exitBtn.setStyle(buttonStyle + "-fx-background-color: #ff6b6b; -fx-text-fill: white;");

        // Add hover effects
        stayBtn.setOnMouseEntered(e -> stayBtn.setStyle(buttonStyle + 
            "-fx-background-color: #40c057; -fx-text-fill: white;"));
        stayBtn.setOnMouseExited(e -> stayBtn.setStyle(buttonStyle + 
            "-fx-background-color: #51cf66; -fx-text-fill: white;"));
        
        exitBtn.setOnMouseEntered(e -> exitBtn.setStyle(buttonStyle + 
            "-fx-background-color: #fa5252; -fx-text-fill: white;"));
        exitBtn.setOnMouseExited(e -> exitBtn.setStyle(buttonStyle + 
            "-fx-background-color: #ff6b6b; -fx-text-fill: white;"));

        stayBtn.setOnAction(e -> {
            alertStage.close();
            mainStage.getScene().getRoot().setEffect(null);
        });

        exitBtn.setOnAction(e -> {
            Platform.exit();
        });

        buttonBox.setSpacing(30);  // Aumentato da 10
        // Removed setOrientation as HBox is already horizontal

        buttonBox.getChildren().addAll(stayBtn, exitBtn);
        
        // Add buttons to dialog content
        dialogContent.getChildren().addAll(spacer, buttonBox);
    }

    private void styleAlert() {
        // Make dialog draggable
        dialogContent.setOnMousePressed(press -> {
            alertStage.setX(press.getScreenX() - alertStage.getX());
            alertStage.setY(press.getScreenY() - alertStage.getY());
        });

        dialogContent.setOnMouseDragged(drag -> {
            alertStage.setX(drag.getScreenX() - alertStage.getX());
            alertStage.setY(drag.getScreenY() - alertStage.getY());
        });
    }

    public CompletableFuture<Boolean> showAlert() {
        CompletableFuture<Boolean> result = new CompletableFuture<>();
        
        if (isShowing || mainStage.getScene() == null) {
            result.complete(false);
            return result;
        }

        try {
            isShowing = true;
            if (mainStage.getScene() != null && mainStage.getScene().getRoot() != null) {
                mainStage.getScene().getRoot().setEffect(blur);
            }

            // Ensure the alert stage is sized before calculating position
            alertStage.sizeToScene();

            // Calculate center position relative to main stage
            double centerX = mainStage.getX() + (mainStage.getWidth() - dialogContent.getPrefWidth()) / 2;
            double centerY = mainStage.getY() + (mainStage.getHeight() - dialogContent.getPrefHeight()) / 2;
            
            // Set position
            alertStage.setX(centerX);
            alertStage.setY(centerY);
            
            stayBtn.setOnAction(e -> {
                cleanup();
                result.complete(false);
            });

            exitBtn.setOnAction(e -> {
                cleanup();
                result.complete(true);
            });

            alertStage.show();
        } catch (Exception e) {
            cleanup();
            result.complete(true);
        }

        return result;
    }

    public void cleanup() {
        try {
            if (mainStage.getScene() != null && mainStage.getScene().getRoot() != null) {
                mainStage.getScene().getRoot().setEffect(null);
            }
            if (alertStage != null && alertStage.isShowing()) {
                alertStage.hide();
            }
        } catch (Exception e) {
            // Ignore cleanup errors
        } finally {
            isShowing = false;
        }
    }
}
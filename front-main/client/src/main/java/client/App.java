package client;

import client.homePage.*;
import client.loginPage.Login;
import client.addProfilePicture.ProfilePictureSelect;
import client.alerts.ExitAlert;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.concurrent.CompletableFuture;

public class App extends Application {
    private Stage stage;
    private Scene loginScene;
    private Scene homeScene;
    private ExitAlert exitAlert;
    private volatile boolean isClosing = false;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        SceneChanger.setStage(stage);
        DataCaller.connect();
        //TODO: IMPLEMENTARE I METODI DI SceneChanger e DataCaller
        initializeLogin(); 
        setupStage();
        
        Platform.runLater(() -> {
            this.exitAlert = new ExitAlert(stage);
            stage.setOnCloseRequest(event -> {
                event.consume();
                if (!isClosing && exitAlert != null) {
                    handleApplicationClose();
                }
            });
        });
    }

    private void handleApplicationClose() {
        if (isClosing) return;
        
        isClosing = true;
        CompletableFuture.runAsync(() -> {
            Platform.runLater(() -> {
                try {
                    if (exitAlert != null) {
                        exitAlert.showAlert().thenAccept(shouldExit -> {
                            if (shouldExit) {
                                cleanupAndExit();
                            } else {
                                isClosing = false;
                            }
                        });
                    }
                } catch (Exception e) {
                    cleanupAndExit();
                }
            });
        });
    }

    private void cleanupAndExit() {
        Platform.runLater(() -> {
            try {
                if (exitAlert != null) {
                    exitAlert.cleanup();
                }
                if (stage != null) {
                    stage.hide();
                }
                Platform.exit();
                System.exit(0);
            } catch (Exception e) {
                System.exit(1);
            }
        });
    }

    private void initializeLogin() {
        Login login = new Login();
        login.setOnLoginSuccess(this::switchToHomePage);
        loginScene = new Scene(login);
        stage.setScene(loginScene);
    }

    private void setupStage() {
        stage.setTitle("Chat JavaFX");
        stage.setMinWidth(600);
        stage.setWidth(1280);
        stage.setMinHeight(400);
        stage.setHeight(720);
        stage.show();
    }

    private void switchToHomePage(String username) {
        // Show profile picture selection dialog before creating HomePage
        ProfilePictureSelect pictureSelect = new ProfilePictureSelect(stage);
        pictureSelect.showDialog().thenAccept(selectedImageUrl -> {
            Platform.runLater(() -> {
                // Initialize chat and user data
                Chat[] chats = new Chat[10];
                for (int i = 0; i < chats.length; i++) {
                    String chatName = "paolo " + String.valueOf(i);
                    chats[i] = new Chat(i, chatName, "https://www.striscialanotizia.mediaset.it/wp-content/uploads/2023/07/Gabibbo.jpeg");
                }
                
                // Use selected image URL for user's profile picture
                User currentUser = new User(0, username, selectedImageUrl);

                HomePage homePage = new HomePage(currentUser, chats);
                homeScene = new Scene(homePage);    
                stage.setScene(homeScene);
            });
        });
    }

    @Override
    public void stop() {
        if (!isClosing) {
            cleanupAndExit();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}

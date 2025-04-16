package client;

import client.homePage.HomePage;
import client.loginPage.Login;
import client.alerts.ExitAlert;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private Stage stage;
    private Scene loginScene;
    private Scene homeScene;
    private Chat[] chats;
    private User currentUser;
    private ExitAlert exitAlert;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        
        initializeLogin();
        setupStage();
        
        // Initialize the exit alert after the stage is set up
        Platform.runLater(() -> {
            this.exitAlert = new ExitAlert(stage);
            // Add close handler after everything is initialized
            stage.setOnCloseRequest(event -> {
                event.consume();
                if (exitAlert != null) {
                    exitAlert.showAlert(stage);
                }
            });
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
        // Initialize chat and user data
        chats = new Chat[1];
        chats[0] = new Chat(0, "paolo", "https://www.striscialanotizia.mediaset.it/wp-content/uploads/2023/07/Gabibbo.jpeg");
        
        currentUser = new User(0, username, "https://www.striscialanotizia.mediaset.it/wp-content/uploads/2023/07/Gabibbo.jpeg");
        HomePage homePage = new HomePage(currentUser, chats);
        
        homeScene = new Scene(homePage);    
        stage.setScene(homeScene);
    }

    @Override
    public void stop() {
        // Clean up resources when the application is closing
        Platform.exit();
    }

    public static void main(String[] args) {
        launch();
    }
}
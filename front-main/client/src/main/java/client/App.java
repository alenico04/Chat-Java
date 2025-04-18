package client;

import client.homepage.HomePage;
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
    private ExitAlert exitAlert;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        SceneChanger.setStage(stage);
        DataCaller.connect();
        //TODO: IMPLEMENTARE I METODI DI SceneChanger e DataCaller
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
        Chat[] chats = new Chat[10];
        for (int i = 0; i < chats.length; i++) {
            String chatName = "paolo " + String.valueOf(i);
            chats[i] = new Chat(i, chatName, "https://www.striscialanotizia.mediaset.it/wp-content/uploads/2023/07/Gabibbo.jpeg");
        }
        User currentUser = new User(0, username, "https://www.striscialanotizia.mediaset.it/wp-content/uploads/2023/07/Gabibbo.jpeg");

        HomePage homePage = new HomePage(currentUser, chats);
        // ChatPage ChatPage = new ChatPage(currentUser, null, null);
        
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

package client;

import client.homePage.HomePage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch();
    }

    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) throws Exception {

        Chat[] chats = new Chat[1];
        for (Chat chat : chats) {
            chat = new Chat(0, "paolo", "https://www.striscialanotizia.mediaset.it/wp-content/uploads/2023/07/Gabibbo.jpeg");
        }

        User currentUser = new User(0, "I AM STEVE", "https://www.striscialanotizia.mediaset.it/wp-content/uploads/2023/07/Gabibbo.jpeg");
        HomePage homePage = new HomePage(currentUser, chats);

        Scene scene = new Scene(homePage);    
        stage.setScene(scene);

        // ----- setup stage

        stage.setTitle("CHAT JAVA!!");
        stage.setMinWidth(600);
        stage.setWidth(1280);

        stage.setMinHeight(400);
        stage.setHeight(720);
        stage.show();
    }
}
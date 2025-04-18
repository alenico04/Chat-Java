package client;

import client.chat.ChatPage;
import client.homepage.HomePage;
import client.loginPage.Login;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneChanger {
    private static Stage stage;

    // Metodo statico per impostare lo stage (alternativa al costruttore)
    public static void setStage(Stage stage) {
        SceneChanger.stage = stage;
    }

    private static void sceneChangerStateVerify() {
        // Verifica che lo stage sia stato impostato
        if (stage == null) {
            throw new IllegalStateException("Stage non impostato. Chiamare prima setStage() o utilizzare il costruttore.");
        }
    } 

    public static void setHomePage() {
        sceneChangerStateVerify();

        User currentUserData = DataCaller.getCurrentUser();
        Chat[] chatsData = DataCaller.getChats();

        HomePage homePage = new HomePage(currentUserData, chatsData);
        Scene scene = new Scene(homePage);

        stage.setScene(scene);
    }

    public static void setLoginPage() {
        sceneChangerStateVerify();

        Login login = new Login();
        Scene scene = new Scene(login);
        stage.setScene(scene);
    }

    public static void setChatPage(int chatId) {
        sceneChangerStateVerify();

        // ChatPage chatPage = new ChatPage();
    }
}

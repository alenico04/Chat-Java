package client.homePage;

import client.User;
import client.utils.ImageUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class UserPanel extends VBox {

    private int userPanelWidth;
    private User user;

    public UserPanel(User currentUser, int userPanelWidth) {
        this.setMinWidth(userPanelWidth);
        this.parentProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.prefHeightProperty().bind(((Region)newValue).heightProperty());
            }
        });
        this.setStyle("-fx-background-color:rgb(225, 253, 99);");
        this.setAlignment(Pos.CENTER);  

        this.user = currentUser;
        this.userPanelWidth = userPanelWidth;
        
        // Create profile picture circle
        int imageSize = 220;
        StackPane profileImageContainer = ImageUtils.createCircularImage(
            currentUser.getProfilePicture(), 
            imageSize
        );

        // Create username label
        Label usernameLabel = new Label(currentUser.getUsername());
        usernameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");

        VBox.setMargin(usernameLabel, new javafx.geometry.Insets(30, 0, 0, 0));
        this.getChildren().addAll(profileImageContainer, usernameLabel);
    }

    // -------------------------- SETTER E GETTER

    public int getUserPanelWidth() {
        return userPanelWidth;
    }

    public void setUserPanelWidth(int userPanelWidth) {
        this.userPanelWidth = userPanelWidth;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

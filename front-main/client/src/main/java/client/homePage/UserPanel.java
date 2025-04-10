package client.homePage;

import client.User;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class UserPanel extends VBox {

    private int userPanelWidth = 350;
    private User user;

    public UserPanel(ReadOnlyDoubleProperty fatherHeight, User user) {
        this.setMinWidth(this.userPanelWidth);
        this.prefHeightProperty().bind(fatherHeight);
        this.setStyle("-fx-background-color:rgb(225, 253, 99);");
        this.setAlignment(Pos.CENTER);  

        this.user = user;
        
        // Create profile picture circle
        int imageSize = 220;
        ImageView profileImageView = new ImageView();
        
        try {
            Image profileImage = new Image(user.getProfilePicture(), true);
            profileImageView.setImage(profileImage);
        } catch (Exception e) {
            // If image loading fails, use a default image or handle the error
            System.err.println("Failed to load profile image: " + e.getMessage());
        }
        
        profileImageView.setFitWidth(imageSize);
        profileImageView.setFitHeight(imageSize);
        
        // Create a circle clip for the image
        Circle clip = new Circle(imageSize / 2);
        clip.setCenterX(imageSize / 2);
        clip.setCenterY(imageSize / 2);
        profileImageView.setClip(clip);
        
        // Create username label
        Label usernameLabel = new Label(user.getUsername());
        usernameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");

        VBox.setMargin(usernameLabel, new javafx.geometry.Insets(30, 0, 0, 0));
        this.getChildren().addAll(profileImageView, usernameLabel);
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
package client.loginPage;

import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.beans.property.ReadOnlyDoubleProperty;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

public class LoginForm extends StackPane {
    private Rectangle formBackground;
    private TextField usernameField;
    private PasswordField passwordField;
    private TextField visiblePasswordField;
    private Button togglePasswordButton;
    private Text usernameError;
    private Text passwordError;
    private VBox loginBox;
    private static final Set<String> reservedWords = new HashSet<>(Arrays.asList(
        "null", "false", "true", "undefined", "admin", "root", "system",
        "guest", "anonymous", "user", "test", "testing", "administrator"
    ));
    
    public LoginForm(ReadOnlyDoubleProperty parentWidth, ReadOnlyDoubleProperty parentHeight) {
        createFormBackground(parentWidth, parentHeight);
        createFields();
        createPasswordToggle();
        createErrorMessages();
        setupLayout();
    }

    private void createFormBackground(ReadOnlyDoubleProperty parentWidth, ReadOnlyDoubleProperty parentHeight) {
        formBackground = new Rectangle();
        formBackground.widthProperty().bind(parentWidth.divide(1.5));
        formBackground.heightProperty().bind(parentHeight.divide(2.5));
        formBackground.setFill(Color.WHITE);
        formBackground.setArcHeight(50);
        formBackground.setArcWidth(50);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(10.0);
        shadow.setOffsetX(3.0);
        shadow.setOffsetY(3.0);
        shadow.setColor(Color.color(0.4, 0.4, 0.4, 0.5));
        formBackground.setEffect(shadow);
    }

    private void createFields() {
        String textFieldStyle = "-fx-font-family: 'Courier New'; -fx-font-size: 14; -fx-font-weight: bold; " +
                              "-fx-background-radius: 10; -fx-border-radius: 10;";

        usernameField = new TextField();
        passwordField = new PasswordField();
        visiblePasswordField = new TextField();

        // Setup username field
        usernameField.setPromptText("Username");
        usernameField.setStyle(textFieldStyle);
        usernameField.setFocusTraversable(false);
        usernameField.prefWidthProperty().bind(formBackground.widthProperty().multiply(0.7));
        usernameField.maxWidthProperty().bind(formBackground.widthProperty().multiply(0.7));
        usernameField.prefHeightProperty().bind(formBackground.heightProperty().multiply(0.15));

        // Setup password fields
        setupPasswordFields(textFieldStyle);
    }

    private void setupPasswordFields(String style) {
        passwordField.setPromptText("Password");
        passwordField.setStyle(style);
        passwordField.setFocusTraversable(false);
        passwordField.prefWidthProperty().bind(formBackground.widthProperty().multiply(0.7));
        passwordField.maxWidthProperty().bind(formBackground.widthProperty().multiply(0.7));
        passwordField.prefHeightProperty().bind(formBackground.heightProperty().multiply(0.15));

        visiblePasswordField.setPromptText("Password");
        visiblePasswordField.setStyle(style);
        visiblePasswordField.setFocusTraversable(false);
        visiblePasswordField.prefWidthProperty().bind(formBackground.widthProperty().multiply(0.7));
        visiblePasswordField.maxWidthProperty().bind(formBackground.widthProperty().multiply(0.7));
        visiblePasswordField.prefHeightProperty().bind(formBackground.heightProperty().multiply(0.15));
        visiblePasswordField.setVisible(false);
        visiblePasswordField.setManaged(false);

        passwordField.textProperty().bindBidirectional(visiblePasswordField.textProperty());
    }

    private void createErrorMessages() {
        String errorStyle = "-fx-fill: red; -fx-font-size: 11;";
        
        usernameError = new Text("Username not valid");
        passwordError = new Text("Password not valid");
        
        usernameError.setStyle(errorStyle);
        passwordError.setStyle(errorStyle);
        usernameError.setVisible(false);
        passwordError.setVisible(false);
    }

    private void createPasswordToggle() {
        togglePasswordButton = new Button();
        Image eyeOpen = new Image("https://cdn-icons-png.flaticon.com/512/709/709612.png");
        Image eyeClosed = new Image("https://cdn-icons-png.flaticon.com/512/2767/2767146.png");
        ImageView eyeOpenView = new ImageView(eyeOpen);
        ImageView eyeClosedView = new ImageView(eyeClosed);
        
        eyeOpenView.setFitHeight(16);
        eyeOpenView.setFitWidth(16);
        eyeClosedView.setFitHeight(16);
        eyeClosedView.setFitWidth(16);
        
        togglePasswordButton.setGraphic(eyeOpenView);
        togglePasswordButton.setStyle("-fx-background-radius: 15; -fx-min-width: 25; -fx-min-height: 25; -fx-cursor: hand; -fx-background-color: transparent; -fx-padding: 0;");

        togglePasswordButton.setOnAction(e -> {
            if (passwordField.isVisible()) {
                passwordField.setVisible(false);
                passwordField.setManaged(false);
                visiblePasswordField.setVisible(true);
                visiblePasswordField.setManaged(true);
                visiblePasswordField.requestFocus();
                togglePasswordButton.setGraphic(eyeClosedView);
            } else {
                passwordField.setVisible(true);
                passwordField.setManaged(true);
                visiblePasswordField.setVisible(false);
                visiblePasswordField.setManaged(false);
                passwordField.requestFocus();
                togglePasswordButton.setGraphic(eyeOpenView);
            }
        });
    }

    private void setupLayout() {
        // Create containers for username and password groups
        VBox usernameGroup = new VBox(5);
        VBox passwordGroup = new VBox(5);
        
        // Create containers for error messages
        HBox usernameContainer = new HBox();
        HBox passwordContainer = new HBox();
        
        // Setup error message containers
        usernameContainer.setAlignment(Pos.CENTER_LEFT);
        passwordContainer.setAlignment(Pos.CENTER_LEFT);
        usernameContainer.translateXProperty().bind(formBackground.widthProperty().multiply(0.15));
        passwordContainer.translateXProperty().bind(formBackground.widthProperty().multiply(0.15));
        
        usernameContainer.getChildren().add(usernameError);
        passwordContainer.getChildren().add(passwordError);

        // Create password field container with toggle button
        StackPane passwordStack = new StackPane();
        passwordStack.getChildren().addAll(passwordField, visiblePasswordField);
        
        HBox buttonContainer = new HBox(togglePasswordButton);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.setPadding(new Insets(0, 5, 0, 0));
        buttonContainer.setPickOnBounds(false);
        
        StackPane passwordWithButton = new StackPane(passwordStack, buttonContainer);
        passwordWithButton.prefWidthProperty().bind(formBackground.widthProperty().multiply(0.7));
        
        HBox passwordFieldContainer = new HBox(5);
        passwordFieldContainer.setAlignment(Pos.CENTER);
        passwordFieldContainer.getChildren().add(passwordWithButton);

        // Group username components
        usernameGroup.setAlignment(Pos.CENTER);
        usernameGroup.getChildren().addAll(usernameField, usernameContainer);

        // Group password components
        passwordGroup.setAlignment(Pos.CENTER);
        passwordGroup.getChildren().addAll(passwordFieldContainer, passwordContainer);

        // Setup main container
        loginBox = new VBox();
        loginBox.setAlignment(Pos.CENTER);
        loginBox.spacingProperty().bind(formBackground.heightProperty().multiply(0.12));
        loginBox.setPadding(new Insets(formBackground.heightProperty().multiply(0.1).get(), 0, 0, 0));
        loginBox.maxWidthProperty().bind(formBackground.widthProperty().multiply(0.9));
        loginBox.maxHeightProperty().bind(formBackground.heightProperty().multiply(0.9));
        loginBox.getChildren().addAll(usernameGroup, passwordGroup);

        // Add dynamic validation listeners
        setupValidationListeners();

        this.getChildren().addAll(formBackground, loginBox);
    }

    private void setupValidationListeners() {
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isValidUsername(newValue.trim())) {
                usernameError.setText(
                    newValue.trim().isEmpty() ? "Username required" : 
                    newValue.length() < 4 ? "Username must be at least 4 characters long" :
                    newValue.length() > 127 ? "Username must be less than 127 characters" :
                    reservedWords.contains(newValue.toLowerCase()) ? "Username not valid" :
                    "Username must contains only letters, numbers, - or _"
                );
                usernameError.setVisible(true);
            } else {
                usernameError.setVisible(false);
            }
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isValidPassword(newValue)) {
                passwordError.setText(
                    newValue.isEmpty() ? "Password required" :
                    newValue.length() < 8 ? "Password must have to be 8 characters long" :
                    !newValue.matches(".*[A-Z].*") ? "Password must contain at least one uppercase letter" :
                    "Password must contain at least one special character (!@#$%^&*()+-={};:,.<>/?)"
                );
                passwordError.setVisible(true);
            } else {
                passwordError.setVisible(false);
            }
        });
    }

    private boolean isValidUsername(String username) {
        return username != null 
            && !username.trim().isEmpty()
            && username.length() >= 4
            && username.length() <= 127
            && !reservedWords.contains(username.toLowerCase())
            && username.matches("^[a-zA-Z0-9_-]+$");
    }

    private boolean isValidPassword(String password) {
        return password != null 
            && password.length() >= 8
            && password.matches(".*[A-Z].*")
            && password.matches(".*[!@#$%^&*()_+={};:,.<>/?].*");
    }

    // Getters
    public TextField getUsernameField() { return usernameField; }
    public PasswordField getPasswordField() { return passwordField; }
    public TextField getVisiblePasswordField() { return visiblePasswordField; }
    public Text getUsernameError() { return usernameError; }
    public Text getPasswordError() { return passwordError; }

    // Clear methods
    public void clearFields() {
        usernameField.clear();
        passwordField.clear();
        visiblePasswordField.clear();
        usernameError.setVisible(false);
        passwordError.setVisible(false);
    }
}
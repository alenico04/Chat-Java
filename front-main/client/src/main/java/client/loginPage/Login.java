package client.loginPage;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import java.util.function.Consumer;

public class Login extends BorderPane {
    private LoginHeader header;
    private LoginForm form;
    private LoginButtons buttons;
    private VBox centerContainer;
    private Consumer<String> onLoginSuccess;
    
    private static final Set<String> reservedWords = new HashSet<>(Arrays.asList(
        "null", "false", "true", "undefined", "admin", "root", "system",
        "guest", "anonymous", "user", "test", "testing", "administrator"
    ));

    public Login() {
        this.setPrefSize(600, 600);
        createComponents();
        setupLayout();
        setupHandlers();
    }

    private void createComponents() {
        header = new LoginHeader(this.widthProperty());
        form = new LoginForm(this.widthProperty(), this.heightProperty());
        buttons = new LoginButtons(form.widthProperty(), form.heightProperty());
        
        centerContainer = new VBox();
        centerContainer.setAlignment(javafx.geometry.Pos.CENTER);
        centerContainer.spacingProperty().bind(this.heightProperty().multiply(0.1));
        centerContainer.getChildren().addAll(form, buttons);
    }

    private void setupLayout() {
        this.setTop(header);
        this.setCenter(centerContainer);
    }

    private void setupHandlers() {
        buttons.getContinueButton().setOnAction(e -> handleLogin());
        buttons.getCancelButton().setOnAction(e -> handleCancel());
    }

    public void setOnLoginSuccess(Consumer<String> callback) {
        this.onLoginSuccess = callback;
    }

    private void handleLogin() {
        String username = form.getUsernameField().getText().trim();
        String password = form.getPasswordField().getText();

        if (validateCredentials(username, password)) {
            if (onLoginSuccess != null) {
                onLoginSuccess.accept(username);
            }
            clearFields();
        }
    }

    private void handleCancel() {
        clearFields();
    }

    private void clearFields() {
        form.getUsernameField().clear();
        form.getPasswordField().clear();
        form.getVisiblePasswordField().clear();
        form.getUsernameError().setVisible(false);
        form.getPasswordError().setVisible(false);
    }

    private static boolean isValidUsername(String username) {
        return username != null 
            && !username.trim().isEmpty()
            && username.length() >= 4
            && username.length() <= 127
            && !reservedWords.contains(username.toLowerCase())
            && username.matches("^[a-zA-Z0-9_-]+$");
    }

    private static boolean isValidPassword(String password) {
        return password != null 
            && password.length() >= 8
            && password.matches(".*[A-Z].*")
            && password.matches(".*[!@#$%^&*].*");
    }

    private boolean validateCredentials(String username, String password) {
        boolean isValid = true;
        
        if (!isValidUsername(username)) {
            form.getUsernameError().setVisible(true);
            isValid = false;
        }
        
        if (!isValidPassword(password)) {
            form.getPasswordError().setVisible(true);
            isValid = false;
        }
        
        return isValid;
    }
}
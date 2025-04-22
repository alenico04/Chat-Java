package client.loginPage;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import java.util.function.Consumer;
import javafx.scene.input.KeyCode;

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
        setupKeyboardNavigation();
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

        // Add real-time validation for username
        form.getUsernameField().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isValidUsername(newValue)) {
                form.getUsernameError().setText(
                    newValue.trim().isEmpty() ? "Username required" : 
                    newValue.length() < 4 ? "Username must be at least 4 characters long" :
                    newValue.length() > 127 ? "Username must be less than 127 characters" :
                    containsReservedWord(newValue) ? "Username contains reserved words" :
                    "Username must contain only letters, numbers, - or _"
                );
                form.getUsernameError().setVisible(true);
            } else {
                form.getUsernameError().setVisible(false);
            }
        });
    }

    // Add helper method to check for reserved words
    private static boolean containsReservedWord(String username) {
        String lowerUsername = username.toLowerCase();
        return reservedWords.stream()
            .anyMatch(word -> lowerUsername.contains(word.toLowerCase()));
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
        if (username == null || username.trim().isEmpty()) {
            return false;
        }

        String lowerUsername = username.toLowerCase();
        
        // Check if username contains any reserved word
        for (String reserved : reservedWords) {
            if (lowerUsername.contains(reserved.toLowerCase())) {
                return false;
            }
        }

        return username.length() >= 4 
            && username.length() <= 127
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

    private void setupKeyboardNavigation() {
        form.getUsernameField().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                event.consume(); // Prevent default tab behavior
                form.getPasswordField().requestFocus();
            }
        });

        form.getPasswordField().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                event.consume();
                if (event.isShiftDown()) {
                    form.getUsernameField().requestFocus();
                } else {
                    buttons.getContinueButton().requestFocus();
                }
            }
        });

        buttons.getContinueButton().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                event.consume();
                if (event.isShiftDown()) {
                    form.getPasswordField().requestFocus();
                } else {
                    buttons.getCancelButton().requestFocus();
                }
            }
        });

        buttons.getCancelButton().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                event.consume();
                if (event.isShiftDown()) {
                    buttons.getContinueButton().requestFocus();
                } else {
                    form.getUsernameField().requestFocus();
                }
            }
        });
    }
}
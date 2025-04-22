package client.chat;

import javafx.scene.control.ScrollPane;
import client.Message;
import client.User;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;

public class MessageList extends ScrollPane {
    public MessageList(Message[] messages, User currentUser) {
        this.setMinWidth(100);
        this.setFitToWidth(true);
        this.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); 
        this.setStyle("-fx-background-color: rgb(166, 184, 255); -fx-background: transparent; -fx-padding: 5;");

        this.parentProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.prefHeightProperty().bind(((Region)newValue).heightProperty());
            }
        });
        
        // Contenitore principale per tutti i messaggi
        VBox messagesContainer = new VBox(10); // 10px di spazio tra i messaggi
        messagesContainer.setPadding(new Insets(10));
        messagesContainer.setFillWidth(true);
        
        if (messages != null && messages.length > 0) {
            for (Message message : messages) {
                // Creo un HBox per ogni messaggio
                HBox messageBox = new HBox();
                
                // Creo un VBox per contenere il nome utente e il testo del messaggio
                VBox messageContent = new VBox(5); // 5px di spazio tra nome e testo
                
                // Nome utente in piccolo
                Text userName = new Text(message.getUsername());
                userName.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");
                
                // Testo del messaggio
                Text messageText = new Text(message.getText());
                messageText.setStyle("-fx-font-size: 14px;");
                
                // Creo un TextFlow per il testo del messaggio (permette il wrapping del testo)
                TextFlow messageTextFlow = new TextFlow(messageText);
                messageTextFlow.setPadding(new Insets(8));
                messageTextFlow.setStyle("-fx-background-color: #7FFFD4; -fx-background-radius: 10;"); // Verde acqua
                
                // Aggiungo nome e testo al contenitore del messaggio
                messageContent.getChildren().addAll(userName, messageTextFlow);
                
                // Controllo se il messaggio è dell'utente corrente
                boolean isCurrentUser = message.getUser_id().equals(currentUser.getId());
                
                if (isCurrentUser) {
                    // Messaggio dell'utente corrente (a destra)
                    messageBox.setAlignment(Pos.CENTER_RIGHT);
                    userName.setFill(Color.DARKBLUE);
                    messageContent.setAlignment(Pos.CENTER_RIGHT);
                } else {
                    // Messaggio di altri utenti (a sinistra)
                    messageBox.setAlignment(Pos.CENTER_LEFT);
                    userName.setFill(Color.DARKRED);
                    messageContent.setAlignment(Pos.CENTER_LEFT);
                }
                
                // Aggiungo il contenuto al box del messaggio
                messageBox.getChildren().add(messageContent);
                
                // Aggiungo il box del messaggio al contenitore principale
                messagesContainer.getChildren().add(messageBox);
            }
        } else {
            // Se non ci sono messaggi, mostro un testo informativo
            Text noMessages = new Text("Nessun messaggio disponibile");
            noMessages.setStyle("-fx-font-size: 14px; -fx-fill: gray;");
            HBox noMessagesBox = new HBox(noMessages);
            noMessagesBox.setAlignment(Pos.CENTER);
            messagesContainer.getChildren().add(noMessagesBox);
        }
        
        // Imposto il contenitore come contenuto dello ScrollPane
        this.setContent(messagesContainer);
        
        // Scroll automatico verso il basso per mostrare gli ultimi messaggi
        this.setVvalue(1.0);
    }
}

package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class DataCaller {
    private static User currentUser;
    private static Chat[] chats;
    private static Message[] messages;
    private static final String SERVER_IP = "127.0.0.1";
    private static final int PORT = 42069;
    private static Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static boolean isConnected = false;
    private static Thread listenerThread;

    public DataCaller() {
        connect();
    }
    
    // Metodo per inizializzare la connessione socket
    public static boolean connect() {
        try {
            if (isConnected && socket != null && !socket.isClosed()) {
                return true; // Già connesso
            }
            
            socket = new Socket(SERVER_IP, PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            isConnected = true;
            
            // Avvia il thread di ascolto
            startListening();
            
            return true;
        } catch (IOException e) {
            System.err.println("Errore di connessione: " + e.getMessage());
            isConnected = false;
            return false;
        }
    }
    
    // Metodo per chiudere la connessione
    public static void disconnect() {
        try {
            isConnected = false;
            if (listenerThread != null) {
                listenerThread.interrupt();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Errore durante la chiusura della connessione: " + e.getMessage());
        }
    }

    // Thread di ascolto per i messaggi in arrivo
    private static void startListening() {
        listenerThread = new Thread(() -> {
            try {
                String receivedData;
                while (isConnected && (receivedData = in.readLine()) != null) {
                    processReceivedData(receivedData);
                }
            } catch (IOException e) {
                if (isConnected) {
                    System.err.println("Errore nella lettura dei dati: " + e.getMessage());
                }
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    // Elabora i dati ricevuti dal server
    private static void processReceivedData(String data) {
        try {
            // Verifica se è un JSON valido
            JsonNode jsonNode = objectMapper.readTree(data);
            
            // Verifica se contiene il campo "type"
            if (jsonNode.has("type")) {
                String type = jsonNode.get("type").asText();
                
                switch (type) {
                    case "user":
                        User currentUser = objectMapper.readValue(data, User.class);
                        setCurrentUser(currentUser);
                        break;
                    case "chat":
                        Chat chat = objectMapper.readValue(data, Chat.class);
                        addChat(chat);
                        break;
                    case "message":
                        Message message = objectMapper.readValue(data, Message.class);
                        addMessage(message);
                        break;
                    default:
                        System.out.println("Tipo di dato sconosciuto: " + type);
                }
            }
        } catch (Exception e) {
            System.err.println("Errore nell'elaborazione dei dati: " + e.getMessage());
        }
    }



    // ----------------------- AGGIUNGI DATI

    // Aggiunge una chat all'array delle chat
    private static void addChat(Chat newChat) {
        if (chats == null) {
            chats = new Chat[1];
            chats[0] = newChat;
        } else {
            Chat[] newChats = new Chat[chats.length + 1];
            System.arraycopy(chats, 0, newChats, 0, chats.length);
            newChats[chats.length] = newChat;
            chats = newChats;
        }
    }

    // Aggiunge un messaggio all'array dei messaggi
    private static void addMessage(Message newMessage) {
        if (messages == null) {
            messages = new Message[1];
            messages[0] = newMessage;
        } else {
            Message[] newMessages = new Message[messages.length + 1];
            System.arraycopy(messages, 0, newMessages, 0, messages.length);
            newMessages[messages.length] = newMessage;
            messages = newMessages;
        }
    }



    // ----------------------- INVIARE DATI AL SERVER
    public static boolean sendMessage(String message) {
        if (!isConnected || socket == null || socket.isClosed()) {
            return false;
        }
        
        //TODO: Implementa la logica per inviare il messaggio
        // out.println(message);
        return true;
    }

    //TODO: implementare metodo per inviare dati al servere dopo login


    // ----------------------- SETTER AND GETTER
    public static Chat[] getChats() {
        return chats;
    }

    public static Message[] getMessages() {
        return messages;
    }
    
    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }
    
    public static void setChats(Chat[] newChats) {
        chats = newChats;
    }
    
    public static void setMessages(Message[] newMessages) {
        messages = newMessages;
    }

    // --------------------- METODI DI TEST
    
    /**
     * Genera e processa dati di test: un utente, due chat e dieci messaggi
     */
    public static void generateTestData() {
        try {
            // Crea e processa un utente di test
            String testUser = "{"
                + "\"type\": \"user\","
                + "\"id\": 1,"
                + "\"name\": \"Test User\","
                + "\"photo\": \"https://www.striscialanotizia.mediaset.it/wp-content/uploads/2023/07/Gabibbo.jpeg\""
                + "}";
            processReceivedData(testUser);
            
            // Crea e processa due chat di test
            String testChat1 = "{"
                + "\"type\": \"chat\","
                + "\"id\": 1,"
                + "\"name\": \"Chat di Test 1\","
                + "\"photo\": \"https://www.striscialanotizia.mediaset.it/wp-content/uploads/2023/07/Gabibbo.jpeg\""
                + "}";
            processReceivedData(testChat1);
            
            String testChat2 = "{"
                + "\"type\": \"chat\","
                + "\"id\": 2,"
                + "\"name\": \"Chat di Test 2\","
                + "\"photo\": \"https://www.striscialanotizia.mediaset.it/wp-content/uploads/2023/07/Gabibbo.jpeg\""
                + "}";
            processReceivedData(testChat2);
            
            // Crea e processa dieci messaggi di test (5 per ogni chat)
            for (int i = 1; i <= 10; i++) {
                int chatId = (i <= 5) ? 1 : 2;  // Primi 5 messaggi nella chat 1, gli altri nella chat 2
                int senderId = (i % 2 == 0) ? 1 : 2;  // Alterna tra utente corrente (1) e altro utente (2)
                String senderName = (senderId == 1) ? "Test User" : "Altro Utente";
                
                String testMessage = "{"
                    + "\"type\": \"message\","
                    + "\"id\": " + i + ","
                    + "\"chatId\": " + chatId + ","
                    + "\"senderId\": " + senderId + ","
                    + "\"senderName\": \"" + senderName + "\","
                    + "\"text\": \"Questo è il messaggio di test numero " + i + "\","
                    + "\"timestamp\": \"" + System.currentTimeMillis() + "\""
                    + "}";
                processReceivedData(testMessage);
            }
            
            System.out.println("Dati di test generati con successo:");
            System.out.println("- 1 utente");
            System.out.println("- 2 chat");
            System.out.println("- 10 messaggi");
            
        } catch (Exception e) {
            System.err.println("Errore nella generazione dei dati di test: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Stampa tutti i dati attualmente in memoria
     */
    public static void printTestData() {
        System.out.println("\n===== DATI DI TEST =====");
        
        // Stampa utente corrente
        System.out.println("\nUTENTE CORRENTE:");
        if (currentUser != null) {
            System.out.println("ID: " + currentUser.getId());
            System.out.println("Nome: " + currentUser.getUsername());
        } else {
            System.out.println("Nessun utente corrente");
        }
        
        // Stampa chat
        System.out.println("\nCHAT:");
        if (chats != null && chats.length > 0) {
            for (Chat chat : chats) {
                System.out.println("ID: " + chat.getId() + ", Nome: " + chat.getName());
            }
        } else {
            System.out.println("Nessuna chat disponibile");
        }
        
        // Stampa messaggi
        System.out.println("\nMESSAGGI:");
        if (messages != null && messages.length > 0) {
            for (Message message : messages) {
                System.out.println(", Chat ID: " + message.getChatId() + 
                                  ", Mittente: " + message.getUsername() + 
                                  ", Testo: " + message.getText());
            }
        } else {
            System.out.println("Nessun messaggio disponibile");
        }
        
        System.out.println("\n=========================");
    }

    /**
     * Returns the current timestamp in milliseconds
     * @return long Current timestamp
     */
    public static long getCurrentTimestamp() {
        return new Date().getTime();
    }


}

package com.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.example.Classes.Message;
import com.example.Classes.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Server {
    private static final int PORT = 42069;
    private static Map<String, PrintWriter> clients = new HashMap<>();
    private static final String URL = "jdbc:postgresql://db:5432/chatdb";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    
    // testing
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        connect();
        System.out.println("Acceso");

        try (ServerSocket serverSocket = new ServerSocket(PORT);) {
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connessione riuscita!");
            return conn;
        } catch (SQLException e) {
            System.out.println("❌ Errore di connessione: " + e.getMessage());
            return null;
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private User user;
        private String accesso;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                // socket
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                String msg;

                // username check
                while (true) {
                    accesso = in.readLine(); // ora è un json
                    // conversione
                    user = objectMapper.readValue(accesso, User.class);
                    
                    if (clients.containsKey(user.getUsername())) {
                        out.println(">> Username già connesso");
                        continue;
                    }
                    if (checkCredentials(user.getUsername(), user.getPassword())) {
                        break;
                    } else {
                        createUser(user.getUsername(), user.getPassword());
                        //out.println(">> User created");
                        break;
                    }
                    // out.println("Username already taken");
                }

                //message on user connection
                synchronized (clients) {
                    clients.put(user.getUsername(), out);
                    out.println(objectMapper.writeValueAsString(user)); // mi aspetto che il client sia in ascolto
                    // write connection message on the chat
                    System.out.println(">> Server: " + user.getUsername() + " connected to the server");
                    broadcastMessage(">> Server: " + user.getUsername() + " joined the chat");
                    // send to clients the name of the connected users
                    updateUserList();
                }

                // handling the message
                Message message;
                while ((message = objectMapper.readValue(in.readLine(), Message.class)) != null){
                    System.out.println("Ricevuto: " + message);
                    /*
                     * Message new_message = objectMapper.readValue(message);
                     * 
                     * if (new_message.text.startsWith("/")){
                     *      ...
                     * } else {
                     *      broadcastMessage(new_message);
                     * }
                     */
                    
                    if (message.getText().startsWith("/")){
                        commandList(message.getText().split("/")[1]);
                    }
                    else {
                        broadcastMessage(user.getUsername() + ": " + message.getText());
                        saveMessageToDatabase(message);
                        //getMessagesFromUser(user.getUsername());
                    }
                }
                
            } catch(IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    socket.close();
                    clients.remove(user.getUsername());
                    updateUserList();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        socket.close();
                        clients.remove(user.getUsername());
                        updateUserList();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        clients.remove(user.getUsername());
                    }
                }
        }
    }

    /* 
    private void sendChatId(){

    } */


    private void createUser(String username, String password) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, username);
        pstmt.setString(2, password); // Attenzione: assicurati che la password nel DB sia in chiaro o hashata nel
                                          // modo corretto

        ResultSet rs = pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    private boolean checkCredentials(String username, String password) throws JsonProcessingException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, username);
                pstmt.setString(2, password);  // Attenzione: assicurati che la password nel DB sia in chiaro o hashata nel modo corretto
                
                ResultSet rs = pstmt.executeQuery();

                if(rs.next()){
                    user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("link_profile_photo"), "user");
                }
                
                return rs.next();  // ritorna true se esiste una riga (credenziali valide)

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    


    private void updateUserList() {
        String list = "/users " + String.join(", ", clients.keySet());
        broadcastMessage(list);
    }

    private static void getAllMessagesFromDatabase(PrintWriter out) {
        String query = "SELECT m.text, m.created_at, u.username, c.group_name " +
                       "FROM messages m " +
                       "JOIN users u ON m.sender_user_id = u.id " +
                       "JOIN chats c ON m.reciever_chat_id = c.id " +
                       "ORDER BY m.created_at ASC";
        
        try (Connection conn = connect();
             java.sql.PreparedStatement stmt = conn.prepareStatement(query)) {
            
            java.sql.ResultSet rs = stmt.executeQuery();
            
            out.println(">> Tutti i messaggi presenti nel database:");
            
            boolean found = false;
            while (rs.next()) {
                found = true;
                String user = rs.getString("username");
                String group = rs.getString("group_name");
                String content = rs.getString("content");
                String timestamp = rs.getTimestamp("timestamp").toString();
                out.println("[" + timestamp + "] " + user + " (" + group + "): " + content);
            }
            
            if (!found) {
                out.println(">> Nessun messaggio presente nel database.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            out.println(">> Errore nella query dei messaggi.");
        }
    }

    private void commandList(String command) {
        String[] formattedMessage = command.split(" ", 2);
        // all'interno di commandList:
        switch (formattedMessage[0]) {
            case "listUsers" -> {
                getUsersFromDatabase();
                out.println(">> Users listed from database.");
            }
            case "messages" -> {
                if (formattedMessage.length > 1) {
                    getMessagesFromUser(formattedMessage[1]);
                } else {
                    out.println(">> Usage: /messages <username>");
                }
            }
            case "allChats" -> {
                getAllMessagesFromDatabase(out);
            }
            case "p" -> {
                String[] privateMessage = formattedMessage[1].split(" ", 2);
                privateMessage(privateMessage[0], user.getUsername() + ": (whisper) " + privateMessage[1]);
            }
            case "?" -> {
                out.println("Commands available:");
                out.println("/listUsers - Show users in the database");
                out.println("/messages <username> - Show messages from user");
                out.println("/allChats - Show all messages in the database");
                out.println("/p <username> <message> - Private message");
            }
            default -> {
                out.println(">> Server: comando non esistente");
            }
        }

    }

    private static void broadcastMessage(String message) {
        synchronized (clients) {
            for (PrintWriter printWriter : clients.values()) {
                printWriter.println(message);
            }
        }
    }

    private static void getUsersFromDatabase() {
        try (Connection conn = connect();
                java.sql.Statement stmt = conn.createStatement()) {
            String query = "SELECT username FROM users";
            java.sql.ResultSet rs = stmt.executeQuery(query);

            StringBuilder usersList = new StringBuilder("Users in DB: ");
            while (rs.next()) {
                usersList.append(rs.getString("username")).append(", ");
            }

            // Rimuove l'ultima virgola e spazio
            if (usersList.length() > 0) {
                usersList.setLength(usersList.length() - 2);
            }

            // Stampa nella console
            System.out.println(usersList.toString());

            // Invia la lista a tutti i client connessi
            broadcastMessage(usersList.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getMessagesFromUser(String username) {
            // Query to get the user_id based on the username
            String userIdQuery = "SELECT id FROM users WHERE username = ?";
            
            try (Connection conn = connect();
                java.sql.PreparedStatement pstmt = conn.prepareStatement(userIdQuery)) {
                
                pstmt.setString(1, username);  // Set the username in the query
                java.sql.ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    int userId = rs.getInt("id");
                    
                    // Now query the messages for this user_id
                    String query = "SELECT text, created_at FROM messages WHERE sender_user_id = ?";
                    try (PreparedStatement msgStmt = conn.prepareStatement(query)) {
                        msgStmt.setInt(1, userId);  // Use the user_id in the messages query
                        ResultSet msgRs = msgStmt.executeQuery();
                        
                        boolean hasMessages = false;
                        while (msgRs.next()) {
                            hasMessages = true;
                            String msg = msgRs.getString("text");
                            String timestamp = msgRs.getString("created_at");
                            out.println("[" + timestamp + "] " + username + ": " + msg);
                        }
                
                        if (!hasMessages) {
                            out.println(">> Nessun messaggio trovato per l'utente: " + username);
                        }
                    }
                
                } else {
                    out.println(">> Utente non trovato: " + username);
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
                out.println(">> Errore durante il recupero dei messaggi.");
            }
        }

    public static void saveMessageToDatabase(Message message) {
        String query = "INSERT INTO messages (text, sender_user_id, reciever_chat_id) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, message.getText());
            preparedStatement.setInt(2, message.getUser_id());
            preparedStatement.setInt(3, message.getChatId());

            preparedStatement.executeUpdate();
            System.out.println("Message saved to database: " + message.getText());

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to save message to database: " + e.getMessage());
        }
    }

    private void privateMessage(String user, String message) {
        synchronized (clients) {
            if (clients.containsKey(user)) {
                out.println(message + " ==> " + user);
                clients.get(user).println(message);
            } else {
                out.println("User doesn't exit");
            }
        }
    }
    }
}
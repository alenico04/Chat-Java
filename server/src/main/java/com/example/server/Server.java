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
import java.sql.Statement;
import java.sql.ResultSet;

public class Server {
    private static final int PORT = 42069;
    private static Map<String, PrintWriter> clients = new HashMap<>();
    private static final String URL = "jdbc:postgresql://localhost:5432/chatdb";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

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
        private String username;
        private String accesso;
        private String password;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                
                //username check
                while (true) {
                    accesso = in.readLine();
                    String[] parts = accesso.split(":", 2);
                    if (parts.length < 2) {
                        out.println(">> Formato non valido. Usa: username:password");
                        continue;
                    }
                    username = parts[0];
                    password = parts[1];

                    if (clients.containsKey(username)) {
                        out.println(">> Username già connesso");
                        continue;
                    }

                    if (checkCredentials(username, password)) {
                        out.println("ok");
                        break;
                    } else {
                        password = in.readLine();
                        createUser(username, password);
                        //out.println(">> User created");
                        out.println("ok");
                        break;
                    }
                    // out.println("Username already taken");

                }

                //message on user connection
                synchronized (clients) {
                    clients.put(username, out);
                    // write connection message on the chat
                    System.out.println(">> Server: " + username + " connected to the server");
                    broadcastMessage(">> Server: " + username + " joined the chat");
                    // send to clients the name of the connected users
                    updateUserList();
                }

                // handling the message
                String message;
                while ((message = in.readLine()) != null){
                    System.out.println("Ricevuto: " + message);
                    if (message.startsWith("/")){
                        commandList(message.split("/")[1]);
                    }
                    else
                        broadcastMessage(username + ": " + message);
                        saveMessageToDatabase(message, username, 1);
                    }
                
            }catch(IOException ex){
                ex.printStackTrace();
        }finally{
            try {
                socket.close();
                clients.remove(username);
                updateUserList();
            } catch (IOException ex) {
                ex.printStackTrace();
                clients.remove(username);
                updateUserList();
            }
        }
    }

    // private boolean isUsernameInvalid(String username) {
    // boolean result = false;
    // synchronized (clients) {
    // if (username == null || username == "" || clients.containsKey(username)) {
    // result = true;
    // out.println("invalid");
    // //out.println("You can't put an empty username");
    // }
    // else if (username.contains(" ")){
    // String formattedName = username.replaceAll("\\s+", "");
    // username = formattedName;
    // System.out.println(username);
    // //System.out.println(username);
    // //result = true;
    // //out.println("spazio");
    // }

    // // if (clients.containsKey(username)){
    // // result = true;
    // // //out.println("Username already taken");
    // // }
    // }
    // return result;
    // }

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

    private boolean checkCredentials(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password); // Attenzione: assicurati che la password nel DB sia in chiaro o hashata nel
                                          // modo corretto

            ResultSet rs = pstmt.executeQuery();

            return rs.next(); // ritorna true se esiste una riga (credenziali valide)

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
                privateMessage(privateMessage[0], username + ": (whisper) " + privateMessage[1]);
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

    public static void saveMessageToDatabase(String text, String username, int receiverChatId) {
        int senderUserId = 1;
        String query = "INSERT INTO messages (text, sender_user_id, reciever_chat_id) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, text);
            preparedStatement.setInt(2, senderUserId);
            preparedStatement.setInt(3, receiverChatId);

            preparedStatement.executeUpdate();
            System.out.println("Message saved to database: " + text);

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
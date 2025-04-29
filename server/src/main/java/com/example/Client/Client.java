package com.example.Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.example.Classes.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Client {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int PORT = 42069;

    // testing
    private User user;

    private PrintWriter out;
    private BufferedReader in;

    private JFrame frame;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JList<String> userList;
    private DefaultListModel userListModel;
    private JButton darkModeButton;
    private JOptionPane alertWindow = new JOptionPane();
    
    private String username;
    private String password;

    // testing
    ObjectMapper objectMapper = new ObjectMapper();

    public Client() {
        frame = new JFrame("Spaiciat");
        chatArea = new JTextArea(20, 40);
        chatArea.setFont(new Font("Consolas", Font.PLAIN, 15));
        chatArea.setEditable(false);
        chatArea.setBackground(Color.WHITE);
        messageField = new JTextField("Enter message", 50);
        messageField.setFont(new Font("Consolas", Font.PLAIN, 15));
        messageField.setForeground(Color.GRAY);
        sendButton = new JButton("Send");
        darkModeButton = new JButton("Switch mode");

        JPanel panel = new JPanel();
        panel.add(messageField);
        panel.add(sendButton);
        panel.add(darkModeButton);

        panel.setBackground(Color.LIGHT_GRAY);
        
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        
        JPanel userListPanel = new JPanel();
        userListPanel.add(userList);
        userListPanel.setPreferredSize(new Dimension(225, 0));
        userListPanel.setBackground(Color.LIGHT_GRAY);

        userListPanel.setFont(new Font("Consolas", Font.PLAIN, 12));

        frame.getContentPane().add(chatArea, BorderLayout.CENTER);
        frame.getContentPane().add(userListPanel, BorderLayout.EAST);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        connectToServer();

        messageField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (messageField.getForeground() == Color.GRAY) {
                    messageField.setForeground(Color.BLACK);
                    messageField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (messageField.getText().equals("")){
                    messageField.setForeground(Color.GRAY);
                    messageField.setText("Enter message");
                }
            }
        });

        sendButton.addActionListener(event -> sendMessage());
        messageField.addActionListener(event -> sendMessage());
        darkModeButton.addActionListener(event -> toggleDarkMode());
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        //Message new_message = new Message(1, message, new Date(System.currentTimeMillis()), username, "1");
        //System.out.println(new_message);
        //String json = objectMapper.writeValueAsString(new_message);
        //out.println(json);
        if (!message.isEmpty() && !(messageField.getForeground() == Color.GRAY)){
            out.println(message);
            messageField.setText("");
        }
    }

    private void toggleDarkMode() {
        if (chatArea.getBackground() == Color.WHITE) {
            chatArea.setBackground(Color.BLACK);
            chatArea.setForeground(Color.WHITE);
            userList.setBackground(Color.DARK_GRAY);
            userList.setForeground(Color.WHITE);
        } else {
            chatArea.setBackground(Color.WHITE);
            chatArea.setForeground(Color.BLACK);
            userList.setBackground(Color.LIGHT_GRAY);
            userList.setForeground(Color.BLACK);
        }
    }

    private void connectToServer() {
        try {
            // creazione socket
            Socket socket = new Socket(SERVER_IP, PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            JButton registerButton = new JButton("Register");
            registerButton.addActionListener(event -> registerUser());
            JPanel registerPanel = new JPanel();
            registerPanel.add(registerButton);
            frame.getContentPane().add(registerPanel, BorderLayout.NORTH);

            String msg = "";

            JPanel jp = new JPanel();
            JLabel uLabel = new JLabel("Enter username:");
            JTextField uField = new JTextField();
            JLabel pLabel = new JLabel("Enter password:");
            jp.add(uLabel);
            jp.add(uField);
            jp.add(pLabel);
            jp.setLayout(new GridLayout(4, 1));
            uField.setEditable(true);


            do {
                password = JOptionPane.showInputDialog(frame, jp);
                username = uField.getText();

                if (username == null || password == null){
                    System.exit(0);
                }
                else if(!controlUsername()){
                    out.println(username + ":" + password);
                    msg = in.readLine();
                    if (msg == null) {
                        JOptionPane.showMessageDialog(frame, "Server disconnected or did not respond.", "Error", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    }
                }
            } while (!msg.equals("ok"));

            // aggiornare lo user con le info dello user presenti nel db

            // Gestione della chat
            new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        if (serverMessage.startsWith("/users ")) {
                            updateUserList(serverMessage.replace("/users ", ""));
                        } else {
                            chatArea.append(serverMessage + "\n");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerUser() {
        while (true) {
            String newUsername = JOptionPane.showInputDialog(frame, "Choose a username:");
            if (newUsername == null) return;
            String password = JOptionPane.showInputDialog(frame, "Choose a password:");
            if (password == null) return;
            String confirmPassword = JOptionPane.showInputDialog(frame, "Confirm password:");
            if (confirmPassword == null) return;
    
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(frame, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
    
            out.println("/register" + " " + newUsername + " " + password);
            String response = null;
            try {
                response = in.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (response != null && response.equals("ok")) {
                JOptionPane.showMessageDialog(frame, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                break;
            } else {
                JOptionPane.showMessageDialog(frame, response != null ? response : "Unknown error", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean controlUsername() {
        boolean result = false;
        if (username.equals("")) {
            result = true;
            alertWindow.showMessageDialog(frame, "You can't write an empty username", "Error", 0);
        } else if (username.contains(" ")) {
            username = username.replaceAll("\\s+", "");
        }

        return result;
    }

    private void updateUserList(String message) {
        userListModel.clear();
        for (String username : message.split(", ")) {
            userListModel.addElement(username);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Client::new);
    }
}
package com.example.Classes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private int id;
    private String username;
    private String password;
    private String profilePicture;
    public String type = "user";

    public User() {
        // Default constructor needed for Jackson
    }

    @JsonCreator
    public User(
        @JsonProperty("id") int id,
        @JsonProperty("username") String username,
        @JsonProperty("password") String password,
        @JsonProperty("profilePicture") String profilePicture,
        @JsonProperty("type") String type
    ){
        this.id = id;
        this.username = username;
        this.password = password;
        this.profilePicture = profilePicture;
        this.type = type;
    }

    public User(String username, String password){
        this.id = 0;
        this.username = username;
        this.password = password;
        this.profilePicture = "https://media.tenor.com/-GLxXYXKdlAAAAAe/cinema-absolute.png";
    }

    /* 
    public User(int id, String username, String profilePicture){
        this.id = id;
        this.username = username;
        this.profilePicture = profilePicture;
    }
    */

    // ----------------- SETTER AND GETTER
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getProfilePicture() {
        return profilePicture;
    }
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
    public String getType() { return type; }
}

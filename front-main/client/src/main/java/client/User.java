package client;

public class User {
    private int id;
    private String username;
    private String profilePicture;
    public final String type = "user";

    public User(int id, String username){
        this(id, username, null);
    }

    public User(int id, String username, String profilePicture){
        this.id = id;
        this.username = username;
        this.profilePicture = profilePicture;
    }

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
    public String getProfilePicture() {
        return profilePicture;
    }
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}

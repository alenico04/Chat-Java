package client;

public class Chat {
    private int id; 
    private String name; 
    private String photo;
    private User[] users;
    private String type = "chat";
    // ---------------------- COSTRUTTORI  

    public Chat(int id, String name, String photo) {
        this(id, name, photo, null);
    }

    public Chat(int id, String name, String photo, User[] users) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.users = users;
    }
    // ---------------------- SETTER e GETTER

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public User[] getUsers() {
        return users;
    }
    public void setUsers(User[] users) {
        this.users = users;
    }
}

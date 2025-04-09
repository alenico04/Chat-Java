package client;

public class Chat {
    int id; 
    String name; 
    String photo;

    public Chat(int id, String name, String photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
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
}

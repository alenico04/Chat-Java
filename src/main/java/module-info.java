module client {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires java.sql;

    opens com.example.client to javafx.fxml;
    exports com.example.client;
}



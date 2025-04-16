module client {
    requires javafx.controls;
    requires javafx.graphics;
    requires transitive javafx.base;
    
    
    exports client;
    exports client.loginPage;
    exports client.homepage;
}

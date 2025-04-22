module client {
    requires javafx.controls;
    requires javafx.graphics;
    requires transitive javafx.base;
    requires com.fasterxml.jackson.databind;
    
    
    exports client;
    exports client.loginPage;
    exports client.homePage;
}

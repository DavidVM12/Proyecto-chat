module com.example.proyecto_chat {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.proyecto_chat to javafx.fxml;
    exports com.example.proyecto_chat;
}
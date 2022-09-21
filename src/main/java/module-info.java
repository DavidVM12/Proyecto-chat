module com.example.proyecto_chat {
    requires javafx.controls;
    requires javafx.fxml;

    exports controlador_vista;
    opens controlador_vista to javafx.fxml;

}
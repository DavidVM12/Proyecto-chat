module com.example.proyecto_chat {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    exports controlador_vista.client;
    opens controlador_vista.client to javafx.fxml;
    exports controlador_vista.server;
    opens controlador_vista.server to javafx.fxml;
    exports controlador_vista;
    opens controlador_vista to javafx.fxml;

//    Para el main
//    exports controlador_vista;
//    opens controlador_vista to javafx.fxml;

}
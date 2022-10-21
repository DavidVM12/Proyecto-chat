package controlador_vista;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatController {

    Stage stage;

    @FXML
    private Button btnEnviar;
    @FXML
    private TextField fieldEnviar;

    @FXML
    protected void enviarMensaje() {



    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
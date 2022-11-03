package controlador_vista;

import controlador_vista.client.ChatController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    Stage stage;

    @FXML
    private Button btnLogin;
    @FXML
    private TextField fieldUserName;
    @FXML
    private TextField fielDomain;
    @FXML
    private TextField fielPassword;
    @FXML
    private CheckBox checkPassword;
    @FXML
    private CheckBox checkLogin;

    @FXML
    protected void iniciarSeccion() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ChatController.class.getResource("/controlador_vista/client/chat-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setScene(scene);
        stage.show();

//        ChatController chatController = fxmlLoader.getController();
//        chatController.setStage(stage);

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


}
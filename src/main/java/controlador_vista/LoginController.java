package controlador_vista;

import controlador_vista.client.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class LoginController {

    Stage stage;
    private static Client client;

    @FXML
    private Button btnLogin;
    @FXML
    private TextField fieldUserName;
    @FXML
    private TextField fieldPassword;
    @FXML
    private CheckBox checkPassword;
    @FXML
    private CheckBox checkLogin;

    @FXML
    protected void iniciarSeccion() throws IOException, InterruptedException {

        try {

            client = new Client(new Socket("localhost", 5000), fieldUserName.getText());
            System.out.println("Connected to Server");

        } catch (IOException e) {
            System.out.println("Error creating Client ... ");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("/controlador_vista/client/chat-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        client.sendMessageToServer("@" + fieldUserName.getText() + ";" + fieldPassword.getText());
        Thread.sleep(100);
        if(client.getExistencia().equals("true")){
            client.sendMessageToServer("$");
            stage.setScene(scene);
            stage.show();
        }

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public static Client getClient() {
        return client;
    }

}
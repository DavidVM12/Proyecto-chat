package controlador_vista;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Messenger humilde");
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imagenes/icono.png")));
        stage.getIcons().add(image);
        stage.setScene(scene);
        stage.show();

//        Creamos una nueva instancia para la nueva vista
        LoginController loginController = fxmlLoader.getController();
        loginController.setStage(stage);

    }

    public static void main(String[] args) {
        launch();
    }
}
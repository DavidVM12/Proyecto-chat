package controlador_vista.client;

import controlador_vista.LoginController;
import controlador_vista.Main;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    boolean bandera;

    Stage stage;

    private static ObservableList<String> list = FXCollections.observableArrayList();
    private static Client client;

    @FXML
    private Button btnEnviar;
    @FXML
    private TextField fieldEnviar;
    @FXML
    VBox vbox_messages;
    @FXML
    private ScrollPane sp_main;
    @FXML
    private ListView<String> myListView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        client = LoginController.getClient();

        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double) newValue);

            }
        });

        // Scroll infinito- supongo...
        double posInf = Double.POSITIVE_INFINITY;
        sp_main.setMaxHeight(posInf);


        client.receiveMessageFromServer(vbox_messages);

        loadList( client.getObervable());

        btnEnviar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String messageToSend = fieldEnviar.getText();
                if (!messageToSend.isBlank()) {
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_RIGHT);

                    hBox.setPadding(new Insets(5, 5, 5, 10));
                    Text text = new Text(messageToSend);
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.setStyle(
                            "-fx-color: rgb(239, 242, 255);" +
                                    "-fx-background-color: rgb(15, 125, 242);" +
                                    "-fx-background-radius: 20px;");

                    textFlow.setPadding(new Insets(5, 10, 5, 10));
                    text.setFill(Color.color(0.934, 0.925, 0.996));

                    hBox.getChildren().add(textFlow);
                    vbox_messages.getChildren().add(hBox);

                    client.sendMessageToServer(messageToSend);
                    fieldEnviar.clear();

                }
            }
        });
    }

    public static void addLabel(String messageFromServer, VBox vBox){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(messageFromServer);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle(
                "-fx-background-color: rgb(233, 233, 235);" +
                        "-fx-background-radius: 20px;");

        textFlow.setPadding(new Insets(5, 10, 5, 10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }

    //Transforma el array a list que deja manipular el listview
    void loadList(ObservableList<String> arrayNombres) {

        myListView.setItems(arrayNombres);

    }

    public void pintarHistorial(){
        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double) newValue);
//                vbox_messages.getChildren().removeAll();
            }
        });

        String [] historial = client.getPartsHistorial();

        for (int i = 0; i < historial.length; i++) {

            if (client.esMultiplo(i,4)) addLabel(historial[i], vbox_messages);
        }

    }


    // funcion del click del contenido del listview
    @FXML public void handleMouseClick(MouseEvent arg0) throws IOException {

        String nombre = myListView.getSelectionModel().getSelectedItem();

        String id = client.identificarUsuario(nombre);

        client.setRemitente(id);

        client.sendMessageToServer("%");

        pintarHistorial();

        vbox_messages.getChildren().clear();

    }
}

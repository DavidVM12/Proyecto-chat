package controlador_vista.client;

import controlador_vista.LoginController;
import controlador_vista.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

    private Socket socket;
    private ObjectOutputStream Output;
    private ObjectInputStream Input;
    static String usuarios;
    private String existencia;
    private ArrayList<String> arrayNombres;
    private String nombreUsuario;


    public Client(Socket socket, String nombreUsuario) {
        try{
            arrayNombres = new ArrayList<>();
            this.nombreUsuario = nombreUsuario;
            this.socket = socket;
            Output = new ObjectOutputStream(socket.getOutputStream());
            Input  = new ObjectInputStream(socket.getInputStream());
        }catch(IOException e){
            System.out.println("Error creando el cliente!");
            e.printStackTrace();
            closeEverything(socket, Output, Input);
        }
    }

    public void sendMessageToServer(String messageToServer) {
        try{
            if(messageToServer.charAt(0) != '@' && messageToServer.charAt(0) != '$') {
                messageToServer = "#" + identificarUsuario() + ";" + messageToServer + ";" + 12 ;
            }
            Output.writeObject(messageToServer);
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Error sending message to the Server!");
            closeEverything(socket, Output, Input);
        }
    }

    public void receiveMessageFromServer(VBox vbox_messages) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()){
                    try{

                        String messageFromServer = Input.readObject().toString();

                        switch (messageFromServer.charAt(0)){

                            case '@':
//                              Login
                                existencia = messageFromServer;
                                existencia = existencia.replace("@", "");
                                break;

                            case '#':
//                              recibir mensaje
                                ChatController.addLabel(messageFromServer, vbox_messages);
                                break;

                            case '$':
//                              recibir usuarios
                                usuarios = messageFromServer;
                                usuarios = usuarios.replace(":", "").replace("$","");
                                String[] parts = usuarios.split(";");
                                System.out.println(usuarios);

                                for (int i = 0; i < parts.length; i ++){

                                    arrayNombres.add(parts[0] + " " + parts[1] + " "  + parts[2] + " "  + parts[3] + " "  + parts[4]);

                                }

                                break;

                            case '%':
//                              recibir historial de chats

                                break;

                            case '*':
//                              Parar
                                messageFromServer = "stop";
                                break;

                        }

                    }catch (IOException e){
                        e.printStackTrace();
                        System.out.println("Error receiving message from the Server!");
                        closeEverything(socket, Output, Input);
                        break;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public String getExistencia() {
        return existencia;
    }

    public String identificarUsuario(){

        String id = "";

        for (String usuario : arrayNombres){
            String[] parts = usuario.split(";");
            if(parts[0].equals(nombreUsuario)){
                id = parts[2];
            }
        }

        return id;
    }

    public ArrayList<String> getArrayNombres() {
        return arrayNombres;
    }

    private void closeEverything(Socket socket, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream){
        try{
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}

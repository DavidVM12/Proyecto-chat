package controlador_vista.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client {


    private String remitente;
    private Socket socket;
    private ObjectOutputStream Output;
    private ObjectInputStream Input;
    static String usuarios;
    private String existencia;
    private ObservableList<String> ObservableArray;
    private ArrayList<String> arrayNombres;
    private String nombreUsuario;
    static String[] parts;
    private String historial;

    private String[] partsHistorial;



    public Client(Socket socket, String nombreUsuario) {
        try{
            ObservableArray = FXCollections.observableArrayList();
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
//            No tocar, creo que esta bien
            if(messageToServer.charAt(0) != '@' && messageToServer.charAt(0) != '$' && messageToServer.charAt(0) != '%') {
                messageToServer = "#" + identificarUsuario(nombreUsuario) + ";" + messageToServer + ";" + remitente ;
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
//                              Login, no tocar
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
                                parts = usuarios.split(";");
                                System.out.println(usuarios);


                                for (int i = 0; i < parts.length; i ++){

                                    // identifico el indice del nombre el cual siempre sera multiplo de 5
                                    // debido al mismo protocolo

                                    if(esMultiplo(i,5)) {
                                        ObservableArray.add(parts[i]);

                                    }

                                }

                                break;

                            case '%':
//                              recibir historial de chats
//                                TODO: Identificar del historial cuales chats sirven por el ID del que recibe (Se puede modificar para cuadrar la forma del historial)
                                historial = messageFromServer;
                                historial = historial.replace(":", "").replace("%","");
                                partsHistorial = historial.split(";");

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

    public String identificarUsuario(String nombre){

        String id = "";

        for (int i = 0; i < parts.length; i++){

                if(parts[i].equals(nombre)){
                    // como el id esta dos casillas adelante
                    id = parts[i+2];
                }

        }

        return id;
    }

    public boolean esMultiplo(int n1,int n2){
        if (n1%n2==0)
            return true;
        else
            return false;
    }

    public ObservableList<String> getObervable() {
        return ObservableArray;
    }

    public String[] getPartsHistorial() {
        return partsHistorial;
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


    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }
}

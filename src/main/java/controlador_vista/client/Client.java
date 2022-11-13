package controlador_vista.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;

    private ObjectOutputStream Output;
    private ObjectInputStream Input;
    static String usuarios;

    private static String [] arrayNombres;

//    private BufferedReader bufferedReader;
//    private BufferedWriter bufferedWriter;

    public Client(Socket socket) {
        try{
            this.socket = socket;

            Output = new ObjectOutputStream(socket.getOutputStream());
            Input = new ObjectInputStream(socket.getInputStream());

            try {
                Output.writeObject("$");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

//            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch(IOException e){
            System.out.println("Error creating Client!");
            e.printStackTrace();
            closeEverything(socket, Output, Input);
        }
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


    public void sendMessageToServer(String messageToServer) {
        try{
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
                        ChatController.addLabel(messageFromServer, vbox_messages);

                        switch (messageFromServer.charAt(0)){

                            case '@':
//                        Login
                                break;

                            case '#':
//                        enviar mensaje
                                break;

                            case '$':
//                        recibir usuarios
                                usuarios = messageFromServer;
                                usuarios.replace(':', ';');
                                System.out.println(usuarios);
                                String[] parts = usuarios.split(";");
                                crearArrayUsuarios(parts[0]);

                                break;

                            case '%':
//                        recibir historial de chats

                                break;

                            case '*':
//                        Parar
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

    public void crearArrayUsuarios(String usuarios){

        arrayNombres = new String[usuarios.length()];

        for (int i = 0; i < usuarios.length(); i++){
            arrayNombres[i] = usuarios;
        }

    }

    public String[] getArrayNombres() {
        return arrayNombres;
    }
}

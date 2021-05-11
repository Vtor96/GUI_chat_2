package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Controller{
    Socket socket;
    DataOutputStream out;
    Thread thread;

    @FXML
    TextArea textArea;
    @FXML
    TextField textField;
    @FXML
    TextArea textAreaUserList;

    @FXML
    private void onSubmit(){
        String text = textField.getText();
        textArea.appendText(text + "\n");
        textField.clear();
        try {
            out.writeUTF(text);
        } catch (Exception e){
            textArea.appendText("Произошла ошибка");
            e.printStackTrace();
        }
    }

    @FXML
    private void connect(){
        try {
            socket = new Socket("192.168.0.106", 8188); //45.80.70.161 // 192.168.0.106

            DataInputStream in = new DataInputStream(socket.getInputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream()); //инициализация out

            try {
                String response = ois.readObject().toString(); //ждем сообщение от сервера
                textArea.appendText(response + "\n"); //введите имя
            } catch (Exception e) {
                e.printStackTrace();
            }

            thread = new Thread(new Runnable(){
                @Override
                public void run(){
                    while (true){
                        try{
                            Object object = ois.readObject();
                            if(String.class == object.getClass()){ //Если нам прислали текстовое сообщение
                                textArea.appendText(object.toString() + "\n"); // То печатаем его на TextArea
                            }else if(ArrayList.class ==  object.getClass()){ //  Если нам прислали объект класса ArrayList
                                ArrayList<String> usersName = new ArrayList<String>(); // то это список пользователей
                                usersName = (ArrayList<String>) object; // Преобразуем объект в ArrayList
                                textAreaUserList.clear(); // Очищаем поле для списка имён
                                for (String userName : usersName) { // Перебираем список
                                    textAreaUserList.appendText(userName + "\n"); // И пишем их на экран
                                }
                            }else{
                                System.out.println("Класс не определен");
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            });
            thread.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

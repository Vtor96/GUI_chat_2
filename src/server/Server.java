package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    static ArrayList<User> users = new ArrayList<>();

    public static void main(String[] args){
        System.out.println("Сервер запущен");

        try {
            ServerSocket serverSocket = new ServerSocket(8188);
            while (true){
                Socket socket = serverSocket.accept(); //Ожидаем клиента
                System.out.println("Клиент подключился");

                User currentUser = new User(socket);
                users.add(currentUser);

                Thread thread = new Thread(new Runnable(){ //Создаем поток для подключившигося клиента
                    @Override
                    public void run() {
                        try{
                            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); //поток вывода для Сериализации
                            DataInputStream in = new DataInputStream(socket.getInputStream());

                            currentUser.setOos(out); ////сохраняем поток в объект (пользователя)
                            currentUser.getOos().writeObject("Введите имя: "); //отправляем данные

                            String userName = in.readUTF();
                            currentUser.setUserName(userName);
                            currentUser.getOos().writeObject(currentUser.getUserName() + " добро пожаловать на сервер!");
                            sendUserList();

                            while (true) {
                                System.out.println("Ожидаем сообщение от пользователя");
                                String request = in.readUTF(); //Ожидаем сообщение от клиента
                                System.out.println(currentUser.getUserName() + ": " + request);

                                for (User user:users){
                                    if(currentUser.equals(user)) continue;
                                    user.getOos().writeObject(userName + ": " + request);
                                }
                            }
                        }catch (IOException exception) {
                            users.remove(currentUser);
                            for (User user:users) {
                                try {
                                    user.getOos().writeObject(currentUser.getUserName() + " покинул чат");
                                    sendUserList();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
                thread.start();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void sendUserList(){
        try {
            ArrayList<String> usersName = new ArrayList<String>();
            for (User user:users){
                usersName.add(user.getUserName()); //Перебираем пользователей и записываем их имена в список
            }

            for (User user:users){
                user.getOos().writeObject(usersName); // Отправляем ArrayList клиенту
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
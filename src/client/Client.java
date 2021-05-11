package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args){
        try {
            Socket socket = new Socket("192.168.0.106", 8188); //45.80.70.161

            System.out.println("Подключился");

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            String response = in.readUTF(); //Ждем сообщение от сервера
            System.out.println("Ответ от сервера: " + response);
            Scanner scanner = new Scanner(System.in);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            String response = in.readUTF();
                            System.out.println(response);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            });
            thread.start();

            while (true) {
                String request = scanner.nextLine();
                out.writeUTF(request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

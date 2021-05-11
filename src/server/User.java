package server;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.UUID;

public class User implements Serializable{
    private Socket socket;
    private String userName;
    private UUID uuid;
    private ObjectOutputStream oos;

    public User(Socket socket) {
        this.socket = socket;
        this.userName = "Гость";
        this.uuid = UUID.randomUUID();
    }

    public ObjectOutputStream getOos() {return oos;}
    public void setOos(ObjectOutputStream oos) {this.oos = oos;}
    public void setUserName(String userName) {this.userName = userName;}
    public String getUserName() {return userName;}
    public Socket getSocket() {return socket;}
    public UUID getUuid() {return uuid;}
    public boolean equals(User user) {return (this.uuid.toString().equals(user.getUuid().toString()));}
}

package main.java.network;

import main.java.domain.Player;
import main.java.shared.protocol.Msg;
import main.java.shared.protocol.MsgType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ConnectionThread extends Thread {
    private int id;
    private Player player;
    private ObjectOutputStream toClient;
    private ObjectInputStream fromClient;
    private Socket clientSocket;
    private ArrayBlockingQueue<Msg> gameMessages;


    ConnectionThread(int id, Socket clientSocket, ArrayBlockingQueue<Msg> gameMessages) {
        this.id = id;
        this.gameMessages = gameMessages;
        this.clientSocket = clientSocket;

        try {
            toClient = new ObjectOutputStream(clientSocket.getOutputStream());
            fromClient = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    void write(Msg msg) {
        try {
            toClient.writeObject(msg);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    void closeSocket() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        System.out.println(id);
        write(new Msg(MsgType.INIT_SET_ID, id));

        try {
            Msg msgFromClient;
            while ((msgFromClient = (Msg) fromClient.readObject()) != null) {
                gameMessages.add(msgFromClient);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

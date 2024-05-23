package main.java.network;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer extends Thread {
    private int port;
    private ConnectionsHandler connectionsHandler;
    private int numOfConnected = 0;
    private ServerSocket serverSocket;

    public GameServer(int portNumber) {
        port = portNumber;
        connectionsHandler = new ConnectionsHandler();

        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        this.setName("GameServer Thread");

        connectionsHandler.start();
        startServer();
    }


    public void startServer() {

        try {
            Socket clientSocket;
            while (numOfConnected<2) {
                clientSocket = serverSocket.accept();
                ConnectionThread connectionThread = new ConnectionThread(numOfConnected, clientSocket, connectionsHandler.getGameMessages());
                connectionThread.start();

                connectionsHandler.addConnection(numOfConnected, connectionThread);
                ++numOfConnected;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void closeServer() {
        try {
            connectionsHandler.stopConnectionsThreads();
            connectionsHandler.interrupt();
            serverSocket.close();
            numOfConnected=0;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

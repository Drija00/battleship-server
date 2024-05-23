package main.java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import main.java.controller.Controller;
import main.java.dbb.DBRepository;
import main.java.dbb.impl.RepositoryDBGeneric;
import main.java.domain.GenericEntity;
import main.java.domain.Player;
import main.java.network.GameServer;
import main.java.repository.Repository;

import java.sql.SQLException;
import java.util.List;


public class ServerController {
    private GameServer gs;

    public ServerController() {
    }

    @FXML
    private Text serverStatus;

    @FXML
    private Button stopServerButton;

    @FXML
    private Button startServerButton;

    @FXML
    private void initialize() {
        startServerButton.setDisable(false);
        stopServerButton.setDisable(true);
        serverStatus.setText("Server is down");
        serverStatus.setFill(Paint.valueOf("Red"));
    }

    @FXML
    private void handleStartServerButtonClicked(ActionEvent event) throws SQLException {
        startServerButton.setDisable(true);
        stopServerButton.setDisable(false);
        gs = new GameServer(8189);
        gs.start();

        serverStatus.setText("Server is running");
        serverStatus.setFill(Paint.valueOf("Green"));
        RepositoryDBGeneric db = new RepositoryDBGeneric();
        Player p = new Player();
        p.setUsername("Drija00");
        p.setPassword("123");
        Player newP = (Player)db.findRecord(p);
        List<Player> players = db.findRecords(new Player());
        for (GenericEntity p1: players){
            //Player x = (Player) p1;
            System.out.println(p1.toString() + "na pocetku");
        }
        System.out.println(newP);
    }

    @FXML
    private void handleStopServerButtonClicked(ActionEvent event){
        startServerButton.setDisable(false);
        stopServerButton.setDisable(true);
        serverStatus.setText("Server is down");
        serverStatus.setFill(Paint.valueOf("Red"));
        gs.closeServer();
    }

    public void closeGameServer() {
        try {
            gs.closeServer();
            gs.interrupt();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

package main.java.network;

import main.java.controller.Controller;
import main.java.dbb.impl.RepositoryDBGeneric;
import main.java.domain.GenericEntity;
import main.java.domain.Player;
import main.java.shared.model.Coordinates;
import main.java.shared.model.FieldState;
import main.java.shared.model.Map;
import main.java.shared.model.PlayerMsg;
import main.java.shared.protocol.Msg;
import main.java.shared.protocol.MsgType;

import java.lang.reflect.Method;
import java.nio.channels.Pipe;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public final class ConnectionsHandler extends Thread {
    private ArrayBlockingQueue<Msg> gameMessages;
    private ConcurrentHashMap<Integer, ConnectionThread> connections;
    private GameServerState gameServerState;
    private HashMap<Integer, Map> playersMaps;

    ArrayBlockingQueue<Msg> getGameMessages() {
        return gameMessages;
    }

    ConnectionsHandler() {
        this.setName("ConnectionsHandler");
        this.gameMessages = new ArrayBlockingQueue<Msg>(10);
        this.connections = new ConcurrentHashMap<>();
        this.gameServerState = GameServerState.INIT_STATE;
        this.playersMaps = new HashMap<>();
    }


    void addConnection(int id, ConnectionThread connectionThread) {
        if (!connections.contains(id))
            connections.put(id, connectionThread);
    }

    void stopConnectionsThreads() {
        connections.forEach((id, connection) -> {
            connection.closeSocket();
            connection.interrupt();
        });
    }

    @Override
    public void run() {

        try {
            Msg msg;
            while ((msg = gameMessages.take()) != null) {
                handleMessage(msg);
                gameMessages.poll();
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handles message received from server
     *
     * @param /*clientMsg WAITING message doesn't need to be handled. It is here to completeness of all messages received by the server
     */

    public <T, R> R invokeGenericMethod(String methodName, Class<T> argType, T argument) {
        try {
            Method method = getClass().getDeclaredMethod(methodName, argType);
            return (R) method.invoke(this, argument);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void handleMessage(Msg clientMsg) {
        switch (clientMsg.getMsgType()) {
            case ID_IS_SET:
                System.out.println("MJAU");
                invokeGenericMethod("handle_id_is_set", Msg.class, clientMsg);
                break;

            case START_GAME:
                System.out.println("JOUaA");
                invokeGenericMethod("handle_start_game", Msg.class, clientMsg);
                break;

            case SHIPS_PLACED:
                invokeGenericMethod("handle_ships_placed", Msg.class, clientMsg);
                break;

            case SHIP_SUNK:
                invokeGenericMethod("handle_ship_sunk", Msg.class, clientMsg);
                break;

            case WAITING:
                break;

            case SHOT_PERFORMED:
                invokeGenericMethod("handle_shot_performed", Msg.class, clientMsg);
                break;
            case LOGIN:
                invokeGenericMethod("handle_login", Msg.class, clientMsg);
                break;
            case REGISTER:
                invokeGenericMethod("handle_register", Msg.class, clientMsg);
                break;
            case GET_PLAYERS:
                invokeGenericMethod("handle_get_players", Msg.class, clientMsg);
                break;
            case RESET:
                invokeGenericMethod("handle_reset", Msg.class, clientMsg);
                break;
        }
    }

    private void handle_reset(Msg msg){
        this.gameServerState = GameServerState.INIT_STATE;
        this.playersMaps = new HashMap<>();
        Msg answer = new Msg(MsgType.ID_IS_SET, msg.getPlayerID());
        send(answer);
    }

    private void handle_get_players(Msg msg){
        RepositoryDBGeneric db = new RepositoryDBGeneric();
        try {
            List<Player> players = db.findRecords(new Player());
            List<PlayerMsg> players1 = new ArrayList<>();
            System.out.println(players.size());
            for (Player p: players){
                System.out.println(p.toString()+ "convert");
                PlayerMsg p1 = new PlayerMsg(0,p.getUsername(),p.getWins());
                players1.add(p1);
            }
            Msg answer = new Msg(MsgType.GET_PLAYERS, msg.getPlayerID(),players1);
            send(answer);
            System.out.println("GET ALL HANDLE");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handle_ship_sunk(Msg clientMsg) {
        System.out.println("Ship sunk entered");
        int activePlayerId = clientMsg.getPlayerID();
        int waitingPlayerId = (activePlayerId + 1) % 2;
        List<Coordinates> coordinates;
        coordinates = (List<Coordinates>) clientMsg.getDataObj();
        for (Coordinates c: coordinates){
            System.out.println(c.toString());
        }
        Msg answer = new Msg(MsgType.ENEMY_SHIP_SUNK,waitingPlayerId, coordinates);
        send(answer);
    }

    private void send(Msg answer) {
        connections.get(answer.getPlayerID()).write(answer);
    }

    private void sendBroadcast(Msg answer) {
        for (int i = 0; i < connections.size(); ++i)
            connections.get(i).write(answer);
    }

    void updateWins(){

    }

    void handle_register(Msg msg){
        Msg answer;
        System.out.println(msg.getPlayerID()+" jjjjj");
        RepositoryDBGeneric db = new RepositoryDBGeneric();
        Player p = new Player();
        p.setFirstname(msg.getFirstname());
        p.setLastname(msg.getLastname());
        p.setUsername(msg.getUsername());
        p.setPassword(msg.getPassword());
        p.setWins(0);
        try{
            Player p1 = (Player) Controller.getInstance().signup(p);
            System.out.println(p1.toString());
            //Long pId = db.insertRecord(p);
            if(p1.getId()!=0L){
                answer = new Msg(MsgType.REGISTER, msg.getPlayerID(),p.getFirstname(),p.getLastname(), p.getUsername(), p.getPassword(),p.getWins());
                send(answer);
                System.out.println("posle");
            }else{
                answer = new Msg(MsgType.REGISTER, msg.getPlayerID(),null,null, null, null,0);
                send(answer);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void handle_login(Msg message){
        Msg answer;
        System.out.println(message.getPlayerID()+" jjjjj");
        RepositoryDBGeneric db = new RepositoryDBGeneric();
        Player p = new Player();
        p.setUsername(message.getUsername());
        p.setPassword(message.getPassword());
        Player newP = null;
        try {
            newP = (Player)db.findRecord(p);
            if(newP!=null) {
                System.out.println("ID iz baze:"+newP.getId());
                System.out.println(newP.getPassword());
                connections.get(message.getPlayerID()).setPlayer(newP);
                answer = new Msg(MsgType.LOGIN, message.getPlayerID(),p.getFirstname(),p.getLastname(), newP.getUsername(), newP.getPassword(),newP.getWins());
                send(answer);
                System.out.println("posle");
            }else{
                answer = new Msg(MsgType.LOGIN, message.getPlayerID(), null, null);
                send(answer);
                System.out.println("posle");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(newP);
    }
    private void handle_start_game(Msg msg){
        for (int i = 0; i < connections.size(); ++i) {
            System.out.println("start game ids: " + connections.get(i));
            System.out.println("Set ids");
            connections.get(i).write(new Msg(MsgType.SET_ID, i));
        }
    }

    private void handle_id_is_set(Msg clientMsg) {
        Msg answer = new Msg();
        int id = clientMsg.getPlayerID();

        if (gameServerState == GameServerState.INIT_STATE) {
            gameServerState = GameServerState.WAIT_FOR_SECOND_PLAYER;
        } else {
            gameServerState = GameServerState.WAIT_FOR_FIRST_READY;
            System.out.println("Place ships");
            answer.setMsgType(MsgType.PLACE_SHIPS);
            sendBroadcast(answer);
        }
        System.out.println(gameServerState.toString());
    }

    private void handle_ships_placed(Msg clientMsg) {
        Msg answer = new Msg();

        int id = clientMsg.getPlayerID();
        Map clientMap = (Map) clientMsg.getDataObj();
        playersMaps.put(id, clientMap);

        if (gameServerState == GameServerState.WAIT_FOR_FIRST_READY) {
            gameServerState = GameServerState.WAIT_FOR_SECOND_READY;
        } else {
            gameServerState = GameServerState.WAIT_FOR_MOVE;

            // Random choose of first player
            int firstPlayerId = new Random().nextInt(2);
            int secondPlayerId = (firstPlayerId + 1) % 2;

            answer.setPlayerID(firstPlayerId);
            answer.setMsgType(MsgType.MAKE_MOVE);
            send(answer);

            answer.setPlayerID(secondPlayerId);
            answer.setMsgType(MsgType.WAIT_FOR_MOVE);
            send(answer);
        }
    }

    private void handle_shot_performed(Msg clientMsg) {
        Msg answer;

        int activePlayerId = clientMsg.getPlayerID();
        int waitingPlayerId = (activePlayerId + 1) % 2;
        Coordinates coordinates = new Coordinates((Coordinates) clientMsg.getDataObj());

        Boolean isHit = playersMaps.get(waitingPlayerId).updateMapWithShot(coordinates);

        boolean isLoser = (playersMaps.get(waitingPlayerId).countFields(FieldState.SHIP) == 0);
        if (isLoser) {
            gameServerState = GameServerState.END;
            Player winner = connections.get(activePlayerId).getPlayer();
            int wins = winner.getWins();
            winner.setWins(++wins);
            System.out.println(winner.toString()+" winner");
            try {
                Controller.getInstance().changeCredentials(winner);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            answer = new Msg(MsgType.WIN, activePlayerId, coordinates);
            send(answer);

            answer = new Msg(MsgType.LOSE, waitingPlayerId, coordinates);
            send(answer);

            return;
        }

        // GameServerState is WAIT_FOR_MOVE and it stays that way
        if (isHit) {
            answer = new Msg(MsgType.HIT_WAIT_FOR_MOVE, activePlayerId, coordinates);
            send(answer);

            answer = new Msg(MsgType.HIT_MAKE_MOVE, waitingPlayerId, coordinates);
            send(answer);

            return;
        }

        answer = new Msg(MsgType.MISS_WAIT_FOR_MOVE, activePlayerId, coordinates);
        send(answer);

        answer = new Msg(MsgType.MISS_MAKE_MOVE, waitingPlayerId, coordinates);
        send(answer);
    }

}

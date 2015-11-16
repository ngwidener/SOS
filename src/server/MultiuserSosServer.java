package server;

import common.MessageListener;
import common.MessageSource;
import common.NetworkInterface;
import game.GameException;
import game.SosGame;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class MultiuserSosServer implements MessageListener {

    private SosGame game;
    private int boardSize;
    private ServerSocket serverSocket;
    private ArrayList<NetworkInterface> clients;

    public MultiuserSosServer(int port) throws IOException {
        game = new SosGame();
        boardSize = 3;
        serverSocket = new ServerSocket(port);
        clients = new ArrayList<NetworkInterface>();
    }

    public MultiuserSosServer(int port, int boardSize) throws IOException {
        game = new SosGame(boardSize);
        this.boardSize = boardSize;
        serverSocket = new ServerSocket(port);
        clients = new ArrayList<NetworkInterface>();
    }

    public void connect(String username, NetworkInterface client) throws IOException {
        try {
            game.addPlayer(username, clients.indexOf(client));
        } catch (GameException e) {
            if (e.getType() == GameException.INVALID_NAME) {
                client.write("Error: That username is taken.\nChoose another with /connect <username>");
            }
            else {
                client.write("Error: A game is already in progress.");
            }
        }
    }

    public void play(NetworkInterface client) throws IOException {
        try {
            game.play();
        } catch (GameException e) {
            if (e.getType() == GameException.NOT_ENOUGH_PLAYERS) {
                client.write("Error: At least two players are needed to start a game");
            }
            else {
                client.write("A game is already in progress.");
            }
        }
    }

    public void move(String move, NetworkInterface client) throws IOException {
        try {
            int nextPlayer = game.move(move, clients.indexOf(client));
            clients.get(nextPlayer).write("It is your turn");
        } catch (GameException e) {
            if (e.getType() == GameException.OUT_OF_TURN) {
                client.write("Error: It is not your turn");
            }
            else {
                client.write("Error: Game not started");
            }
        }
    }

    public void quit(NetworkInterface client) {
        clients.remove(client);
        client.removeMessageListener(this);
        game = new SosGame(boardSize);
    }

    @Override
    public void messageReceived(String message, MessageSource source) {
        try {
            if (source instanceof NetworkInterface) {
                NetworkInterface client = (NetworkInterface) source;
                String[] msgArray = message.split("\\s+");
                if (msgArray[0].equals("/connect") && msgArray.length > 1) {
                    connect(msgArray[1], client);
                }
                if (msgArray[0].equals("/play")) {
                    play(client);
                }
                if (msgArray[0].equals("/move") && msgArray.length == 4) {
                    move(message, client);
                }
                if (msgArray[0].equals("/quit")) {
                    quit(client);
                }
            }
        } catch (IOException e) {}
    }

    @Override
    public void sourceClosed(MessageSource source) {

    }

    public void listen() throws IOException {
        while (!serverSocket.isClosed()) {
            NetworkInterface client = new NetworkInterface(serverSocket.accept());
            client.addMessageListener(this);
            clients.add(client);
            Thread thread = new Thread(client);
            thread.start();
            client.write("connected to server");
        }
    }
}

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
        this (port, SosGame.DEFAULT_BOARD_SIZE);
    }

    public MultiuserSosServer(int port, int boardSize) throws IOException {
        game = new SosGame(boardSize);
        this.boardSize = boardSize;
        serverSocket = new ServerSocket(port);
        clients = new ArrayList<NetworkInterface>();
    }

    public void listen() throws IOException {
        while (!serverSocket.isClosed()) {
            NetworkInterface client = new NetworkInterface(serverSocket.accept());
            client.addMessageListener(this);
            clients.add(client);
            (new Thread(client)).start();
        }
    }

    public void privateMessage(String message, NetworkInterface client) {
        client.write(message);
    }

    public void broadcastMessage(String message) {
        for (NetworkInterface client : clients) {
            client.write(message);
        }
    }

    public void connect(String command, NetworkInterface sender) {
        try {
            game.addPlayer(command.split("\\s+")[1], clients.indexOf(sender));
        }
        catch (GameException e) {
            if (e.getType() == GameException.INVALID_NAME) {
                privateMessage("Error: That username is taken.\n" +
                        "       Choose another with /connect <username>", sender);
            }
            else {
                privateMessage("Error: A game is already in progress.", sender);
            }
        }
    }

    public void play(NetworkInterface sender) {
        try {
            int firstPlayer = game.play();
            broadcastMessage(game.getBoard());
            broadcastMessage("It is " + game.getPlayerName(firstPlayer) + "'s turn.");
        } catch (GameException e) {
            if (e.getType() == GameException.NOT_ENOUGH_PLAYERS) {
                privateMessage("Error: At least two players are needed to start a game.", sender);
            }
            else {
                privateMessage("Error: A game is already in progress.", sender);
            }
        }
    }

    public void move(String command, NetworkInterface sender) {
        try {
            int nextPlayer = game.move(command, clients.indexOf(sender));
            if (nextPlayer >= 0) {
                broadcastMessage(game.getBoard());
                broadcastMessage("It is " + game.getPlayerName(nextPlayer) + "'s turn.");
            }
            else {
                broadcastMessage(game.toString());
                broadcastMessage("Game Over");
                game.reset();
            }
        } catch (GameException e) {
            if (e.getType() == GameException.GAME_STATE) {
                privateMessage("Error: No game in progress.", sender);
            }
            else if (e.getType() == GameException.OUT_OF_TURN) {
                privateMessage("Error: It is not your turn.", sender);
            } else {
                broadcastMessage("Error: Invalid move");
            }
        }
    }

    public void quit(NetworkInterface client) {
        game.removePlayer(clients.indexOf(client));
        game.reset();
        clients.remove(client);
        client.close();
    }

    @Override
    public void messageReceived(String message, MessageSource source) {
        NetworkInterface client = (NetworkInterface) source;
        if (message.startsWith("/connect") && !message.endsWith("/connect")) {
            connect(message, client);
        }
        else if (message.startsWith("/play")) {
            play(client);
        }
        else if (message.startsWith("/move") && ! message.endsWith("/move")) {
            move(message, client);
        }
        else if (message.startsWith("/quit")) {
            quit(client);
        }
        else {
            privateMessage("Error: Not a recognized command.", client);
        }
    }

    @Override
    public void sourceClosed(MessageSource source) {
        clients.remove(source);
        source.removeMessageListener(this);
    }
}

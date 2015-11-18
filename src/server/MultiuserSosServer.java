package server;

import common.MessageListener;
import common.MessageSource;
import common.NetworkInterface;
import game.GameException;
import game.SosGame;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * Provides the TCP server implementation for our server
 * for the SOS game.
 *
 * @author Jameson Burchette
 * @author Nicholas Widener
 * @version November 2015
 */
public class MultiuserSosServer implements MessageListener {

    /**The SOS game*/
    private SosGame game;

    /**Our server socket*/
    private ServerSocket serverSocket;

    /**List of clients*/
    private ArrayList<NetworkInterface> clients;

    /**
     * Constructor; creates a new MultiuserSosServer.
     * @param port the port number we want to talk to.
     * @throws IOException if the port is invalid.
     */
    public MultiuserSosServer(int port) throws IOException {
        this (port, SosGame.DEFAULT_BOARD_SIZE);
    }

    /**
     * Constructor; creates a new MultiuserSosServer.
     * @param port the port number we want to talk to.
     * @param boardSize the size of the board.
     * @throws IOException if we cannot make a connection with
     * our server socket or if the port is invalid.
     */
    public MultiuserSosServer(int port, int boardSize) throws IOException {
        game = new SosGame(boardSize);
        serverSocket = new ServerSocket(port);
        clients = new ArrayList<NetworkInterface>();
    }

    /**
     * Listens for messages from a client.
     * @throws IOException if the server socket is interrupted.
     */
    public void listen() throws IOException {
        while (!serverSocket.isClosed()) {
            NetworkInterface client = new NetworkInterface(serverSocket.accept());
            client.addMessageListener(this);
            clients.add(client);
            (new Thread(client)).start();
        }
    }

    /**
     * Writes private messages to a client.
     * @param message the message to be sent.
     * @param client the client that the message is sent to.
     */
    public void privateMessage(String message, NetworkInterface client) {
        client.write(message);
    }

    /**
     * Broadcast the message to a client.
     * @param message the message that is broadcasted.
     */
    public void broadcastMessage(String message) {
        for (NetworkInterface client : clients) {
            client.write(message);
        }
    }

    /**
     * Make a connection with a client.
     * @param command the command received from the client.
     * @param sender the sender of the data.
     */
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

    /**
     * Play a game that is initiated by a client.
     * @param sender the sender that wants to play a game.
     */
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

    /**
     * Tell a player when its their turn, when a game is over, when a move is invalid,
     * when a game is not in progress and when a game is over.
     * @param command the command from the sender.
     * @param sender the sender of the command.
     */
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

    /**
     * Quit a game.
     * @param client the client that wants to quit.
     */
    public void quit(NetworkInterface client) {
        game.removePlayer(clients.indexOf(client));
        game.reset();
        clients.remove(client);
        client.close();
    }

    /**
     * Handles messages received from a client.
     * @param message the message that is received.
     * @param source the source the message is received from.
     */
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

    /**
     * Close the source of the messages.
     * @param source the source that the message is received from.
     */
    @Override
    public void sourceClosed(MessageSource source) {
        clients.remove(source);
        source.removeMessageListener(this);
    }
}

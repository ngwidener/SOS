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
    private ServerSocket serverSocket;
    private ArrayList<NetworkInterface> clients;

    public MultiuserSosServer(int port) throws IOException {
        game = new SosGame();
        serverSocket = new ServerSocket(port);
        clients = new ArrayList<NetworkInterface>();
    }

    public MultiuserSosServer(int port, int boardSize) throws IOException {
        game = new SosGame(boardSize);
        serverSocket = new ServerSocket(port);
        clients = new ArrayList<NetworkInterface>();
    }

    public void connect(String username, NetworkInterface client) {
        try {
            game.addPlayer(username, clients.indexOf(client));
        } catch (GameException e) {
            if (e.getType() == GameException.INVALID_NAME) {
                try {
                    client.write("That username is taken.\nChoose another with /connect <username>");
                }
                catch (IOException ioe) {}
            }
        }
    }

    @Override
    public void messageReceived(String message, MessageSource source) {
        if (source instanceof NetworkInterface) {
            NetworkInterface client = (NetworkInterface)source;
            String[] msgArray = message.split("\\s+");
            if (msgArray[0].equals("/connect") && msgArray.length > 1) {
                connect(msgArray[1], client);
            }
        }
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

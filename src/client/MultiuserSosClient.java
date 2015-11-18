package client;

import common.MessageListener;
import common.MessageSource;
import common.NetworkInterface;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * A TCP client implementation for our SOS Game
 *
 * @author Jameson Burchette
 * @author Nicholas Widener
 * @version November 2015
 */
public class MultiuserSosClient implements MessageListener {
    /**A network interface for our client*/
    private NetworkInterface netInterface;

    /**Scanner to receive input from the user*/
    private Scanner inputScanner;


    /**
     * Constructor; sets the network interface and creates
     * a new thread with a scanner.
     * @param host the host to connect to.
     * @param port the port we want to talk to.
     * @throws IOException if the host cannot be connected to
     * or the port is invalid.
     */
    public MultiuserSosClient(InetAddress host, int port) throws IOException {
        netInterface = new NetworkInterface(host, port);
        netInterface.addMessageListener(this);
        (new Thread(netInterface)).start();
        inputScanner = new Scanner(System.in);
    }


    /**
     * Connects the user to a game.
     * @param username the username of the user in the game.
     * @throws IOException if data cannot be written to the server.
     */
    public void connect(String username) throws IOException {
        String command = "/connect " + username;
        netInterface.write(command);
        while (!command.startsWith("/quit")) {
            if (inputScanner.hasNextLine()) {
                command = inputScanner.nextLine();
                netInterface.write(command);
            }
        }
        netInterface.close();
    }

    /**
     * Prints what message is received.
     * @param message the message received.
     * @param source the source the message is received from.
     */
    @Override
    public void messageReceived(String message, MessageSource source) {
        System.out.print(message + "\n");
    }

    /**
     * Closes the source for the message.
     * @param source the source we wish to close.
     */
    @Override
    public void sourceClosed(MessageSource source) {
        source.removeMessageListener(this);
    }
}

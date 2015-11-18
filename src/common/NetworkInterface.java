package common;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Sends messages to and receives messages from hosts.
 * Also contains the logic for threads.
 *
 * @author Jameson Burchette
 * @author Nicholas Widener
 * @version November 2015
 */
public class NetworkInterface extends MessageSource implements Runnable {

    /**Network interface socker*/
    private Socket socket;

    /**Reader for the data coming in*/
    private BufferedReader in;

    /**Stream for the data going out*/
    private DataOutputStream out;

    /**
     * Constructor; creates a new socket with host and port.
     * @param host the hostname we want to connect to.
     * @param port the port that want to talk to.
     * @throws IOException if the host cannot be connected to
     * or the port is invalid.
     */
    public NetworkInterface(InetAddress host, int port) throws IOException {
        this(new Socket(host, port));
    }

    /**
     * Constructor; calls super and creates a new socket and streams
     * for data that is sent and received.
     * @param socket the socket that we want to use to connect with.
     * @throws IOException if the socket cannot make a connection.
     */
    public NetworkInterface(Socket socket) throws IOException {
        super();
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new DataOutputStream(socket.getOutputStream());
    }

    /**
     * Writes data to the server.
     * @param message the message that we want to send.
     */
    public void write(String message) {
        if (!message.endsWith("\n")) {
            message += "\n";
        }
        if (!socket.isClosed() && !socket.isOutputShutdown()) {
            try {
                out.writeBytes(message);
            } catch (IOException e) {
                close();
            }
        }
    }

    /**
     * Spins off a new thread for each process that
     * connects to send messages.
     */
    @Override
    public void run() {
        String message = null;
        while (!socket.isClosed() && !socket.isInputShutdown()) {
            try {
                message = in.readLine();
            } catch (IOException e) {
                close();
            }
            if (message != null) {
                notifyReceipt(message);
            }
        }
    }

    /**
     * Closes the message source as well as our socket and
     * input/output streams.
     */
    public void close() {
        closeMessageSource();
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

package common;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class NetworkInterface extends MessageSource implements Runnable {

    private static final int TIME_OUT = 5000;

    private Socket socket;
    private Scanner in;
    private DataOutputStream out;

    public NetworkInterface(InetAddress host, int port) throws IOException {
        this(new Socket(host, port));
    }

    public NetworkInterface(Socket socket) throws IOException {
        super();
        this.socket = socket;
        socket.setSoTimeout(TIME_OUT);
        in = new Scanner(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public void write(String message) throws IOException {
        /**
        if (!message.endsWith("\n"))
            message += "\n";
        out.flush();
         */
        out.writeBytes(" " + message + " \n");
    }

    /**
    public void read() throws IOException {
        String message = "";
        String line;
        while ((line = in.readLine()) != null)
            message += line;
        if (!message.equals(""))
            notifyReceipt(message);
    }
     */

    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
        closeMessageSource();
    }

    @Override
    public void run() {
        try {
            while (true) {
                try {
                    System.out.print(in.next());
                }
                catch (NoSuchElementException noe) {

                }
            }
        } catch (Exception e) {
            closeMessageSource();
        }
    }
}

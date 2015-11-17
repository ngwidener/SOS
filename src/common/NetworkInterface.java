package common;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class NetworkInterface extends MessageSource implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private DataOutputStream out;

    public NetworkInterface(InetAddress host, int port) throws IOException {
        this(new Socket(host, port));
    }

    public NetworkInterface(Socket socket) throws IOException {
        super();
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new DataOutputStream(socket.getOutputStream());
    }

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

    public void close() {
        closeMessageSource();
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {

        }
    }
}

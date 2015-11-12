package common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class NetworkInterface extends MessageSource implements Runnable {

    private static final int TIME_OUT = 5000;

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public NetworkInterface(InetAddress host, int port) throws IOException {
        this(new Socket(host, port));
    }

    public NetworkInterface(Socket socket) throws IOException {
        super();
        this.socket = socket;
        socket.setSoTimeout(TIME_OUT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void write(String message) throws IOException {
        if (!message.endsWith("\n"))
            message += "\n";
        out.flush();
        out.write(message);
    }

    public void read() throws IOException {
        String message = "";
        String line;
        while ((line = in.readLine()) != null)
            message += line;
        if (!message.equals(""))
            notifyReceipt(message);
    }

    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
        closeMessageSource();
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed() && !socket.isInputShutdown()) {
                read();
            }
            close();
        } catch (IOException e) {

        }
    }
}

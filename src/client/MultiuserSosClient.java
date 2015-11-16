package client;

import common.MessageListener;
import common.MessageSource;
import common.NetworkInterface;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Nicholas on 11/12/2015.
 */
public class MultiuserSosClient extends MessageSource implements MessageListener {
    private InetAddress host;
    private int port;
    private String nickname;
    private Scanner input;


    public MultiuserSosClient(InetAddress host, int port, String nickname) {
        super();
        this.host = host;
        this.port = port;
        this.nickname = nickname;
        input = new Scanner(System.in);
    }


    public void play() {
        try {
            Socket socket  = new Socket(host, port);
            NetworkInterface networkInterface = new NetworkInterface(socket);
            networkInterface.addMessageListener(this);
            Thread thread = new Thread(networkInterface);
            thread.start();
            networkInterface.write("/connect " + nickname);
            while (true) {
                if (input.hasNext()) {
                    networkInterface.write(input.next());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageReceived(String message, MessageSource source) {
        System.out.print(message);
    }

    @Override
    public void sourceClosed(MessageSource source) {

    }
}

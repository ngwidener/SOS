package client;

import common.MessageListener;
import common.MessageSource;
import common.NetworkInterface;

import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Nicholas on 11/12/2015.
 */
public class MultiuserSosClient extends MessageSource implements MessageListener {


    private InetAddress host;

    private int port;

    private String nickname;


    public MultiuserSosClient(InetAddress host, int port, String nickname) {
        super();
        this.host = host;
        this.port = port;
        this.nickname = nickname;
    }


    public void play() {
        try {
            Socket socket  = new Socket();
            NetworkInterface networkInterface = new NetworkInterface(socket);

            Thread thread = new Thread(networkInterface);
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageReceived(String message, MessageSource source) {

    }

    @Override
    public void sourceClosed(MessageSource source) {

    }
}

package client;

import common.MessageListener;
import common.MessageSource;
import common.NetworkInterface;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

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
            Socket socket  = new Socket(host, port);
            NetworkInterface networkInterface = new NetworkInterface(socket);
            networkInterface.addMessageListener(this);
            Thread thread = new Thread(networkInterface);
            thread.start();

            networkInterface.write(nickname);
        } catch (SocketException se) {
            System.out.println(se.getMessage());
        } catch (UnknownHostException uhe) {
            System.out.println(uhe.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    @Override
    public void messageReceived(String message, MessageSource source) {
        message = nickname + ": ";
        System.out.println(message);
    }

    @Override
    public void sourceClosed(MessageSource source) {

    }
}

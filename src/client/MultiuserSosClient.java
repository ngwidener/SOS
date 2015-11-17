package client;

import common.MessageListener;
import common.MessageSource;
import common.NetworkInterface;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Nicholas on 11/12/2015.
 */
public class MultiuserSosClient implements MessageListener {
    private NetworkInterface netInterface;
    private Scanner inputScanner;


    public MultiuserSosClient(InetAddress host, int port) throws IOException {
        netInterface = new NetworkInterface(host, port);
        netInterface.addMessageListener(this);
        (new Thread(netInterface)).start();
        inputScanner = new Scanner(System.in);
    }


    public void connect(String username) throws IOException {
        netInterface.write("/connect " + username + "\n");
        while (true) {
            if (inputScanner.hasNextLine()) {
                netInterface.write(inputScanner.nextLine() + "\n");
            }
        }
    }

    @Override
    public void messageReceived(String message, MessageSource source) {
        System.out.print(message + "\n");
    }

    @Override
    public void sourceClosed(MessageSource source) {

    }
}

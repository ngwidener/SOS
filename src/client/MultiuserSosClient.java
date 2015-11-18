package client;

import common.MessageListener;
import common.MessageSource;
import common.NetworkInterface;

import java.io.IOException;
import java.net.InetAddress;
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

    @Override
    public void messageReceived(String message, MessageSource source) {
        System.out.print(message + "\n");
    }

    @Override
    public void sourceClosed(MessageSource source) {
        source.removeMessageListener(this);
    }
}

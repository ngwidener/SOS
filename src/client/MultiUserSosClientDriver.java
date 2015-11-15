package client;

import java.net.InetAddress;

/**
 * Created by Nicholas on 11/13/2015.
 */
public class MultiUserSosClientDriver {



    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("usage: client.MultiuserSosClient <host> <port> <nickname>");
            System.exit(1);
        }
        try {
            MultiuserSosClient client;
            InetAddress host = InetAddress.getByName(args[0]);
            int port = Integer.parseInt(args[1]);
            String userName = args[2];
            client = new MultiuserSosClient(host, port, userName);
        } catch (Exception e) {

        }

    }
}
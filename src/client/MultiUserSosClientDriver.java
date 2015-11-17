package client;

import java.net.InetAddress;

/**
 * Created by Nicholas on 11/13/2015.
 */
public class MultiUserSosClientDriver {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("usage: client.MultiUserSosClientDriver <host> <port> <username>");
            System.exit(1);
        }
        try {
            MultiuserSosClient client = new MultiuserSosClient(InetAddress.getByName(args[0]),
                                                               Integer.parseInt(args[1]));
            client.connect(args[2]);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Runs our MultiuserSosClient and checks for valid input
 * as well as exception handling.
 *
 * @author Jameson Burchette
 * @author Nicholas Widener
 * @version November 2015
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
        }catch (NumberFormatException e) {
            System.out.println("Error: Your second argument doesn't seem to be a number.");
            System.out.println("usage: client.MultiUserSosClientDriver <host> <port> <username>");
            System.exit(1);
        } catch (UnknownHostException e) {
            System.out.println("Error: The IP address of " + args[0] + " could not be determined.");
            System.exit(1);
        } catch (SocketException se) {
            System.out.println("Error: Connection refused.");
            System.exit(1);
        } catch (IOException ioe) {
            System.out.println("Error: Something went wrong while sending or receiving data.");
            System.exit(1);
        }
    }
}
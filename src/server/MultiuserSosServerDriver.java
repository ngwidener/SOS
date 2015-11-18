package server;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.rmi.ServerError;
import java.rmi.ServerException;
import java.rmi.server.ServerNotActiveException;

/**
 * Runs our MultiuserSosServer and checks for valid input
 * as well as exception handling.
 */
public class MultiuserSosServerDriver {
    public static void main(String[] args) {
        if (args.length < 1 || args.length > 2) {
            System.out.println("usage: server.MultiuserSosServerDrive <port #> [<board size>]");
            System.exit(1);
        }
        try {
            MultiuserSosServer server;
            if (args.length == 2) {
                server = new MultiuserSosServer(Integer.parseInt(args[0]),
                                                Integer.parseInt(args[1]));
            }
            else {
                server = new MultiuserSosServer(Integer.parseInt(args[0]));
            }
            server.listen();
        }
        catch (NumberFormatException e) {
            System.out.println("Error: Both arguments must be numbers");
            System.out.println("usage: server.MultiuserSosServerDrive <port #> [<board size>]");
            System.exit(1);
        }
        catch (ServerException se) {
            System.out.println("Error: The server could not connect.");
            System.exit(1);
        } catch (UnknownHostException uhe) {
            System.out.println("Error: The IP address could not be determined.");
            System.exit(1);
        } catch (ServerError serverError) {
            System.out.println("Error: The server could not be invoked.");
            System.exit(1);
        } catch (ConnectException ce) {
            System.out.println("Error: The socket could not make a connection.");
            System.exit(1);
        } catch (IOException ioe) {
            System.out.println("An IO error has occurred.");
            System.exit(1);
        }
    }
}

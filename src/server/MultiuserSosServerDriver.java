package server;

public class MultiuserSosServerDriver {
    public static void main(String[] args) {
        if (args.length < 1 || args.length > 2) {
            System.out.println("usage: server.MultiuserSosServerDrive <port #> [<board size>]");
            System.exit(1);
        }
        try {
            MultiuserSosServer server;
            int port = Integer.parseInt(args[0]);
            if (args.length == 2) {
                int boardSize = Integer.parseInt(args[1]);
                server = new MultiuserSosServer(port, boardSize);
            }
            else {
                server = new MultiuserSosServer(port);
            }
            server.listen();
        }
        catch (NumberFormatException e) {
            System.out.println("Error: Both arguments must be numbers");
            System.out.println("usage: server.MultiuserSosServerDrive <port #> [<board size>]");
            System.exit(1);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}

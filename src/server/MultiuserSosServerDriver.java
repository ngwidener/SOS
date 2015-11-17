package server;

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
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}

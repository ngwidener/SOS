package game;

/**
 * Exception class to handle exceptions specific to our game.
 *
 * @author Jameson Burchette
 * @author Nicholas Widener
 * @version November 2015
 */
public class GameException extends Exception {
    /**Invalid name for a username within a game*/
    public static final int INVALID_NAME = 0;

    /**Not enough players to play a game*/
    public static final int NOT_ENOUGH_PLAYERS = 1;

    /**No more turns for a player*/
    public static final int OUT_OF_TURN = 2;

    /**The state of the game*/
    public static final int GAME_STATE = 3;

    /**Invalid move in the game*/
    public static final int INVALID_MOVE = 4;

    /**Exception type*/
    private int type;

    /**
     * Constructor; creates a new game exception.
     * @param type the exception type.
     */
    public GameException(int type) {
        super();
        this.type = type;
    }

    /**
     * Get the exception type.
     * @return the exception type.
     */
    public int getType() {
        return type;
    }
}

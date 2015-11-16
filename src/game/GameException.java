package game;

public class GameException extends Exception {
    public static final int INVALID_NAME = 0;
    public static final int NOT_ENOUGH_PLAYERS = 1;
    public static final int OUT_OF_TURN = 2;
    public static final int GAME_STATE = 3;
    public static final int INVALID_MOVE = 4;

    private int type;

    public GameException(int type) {
        super();
        this.type = type;
    }

    public int getType() {
        return type;
    }
}

package game;

import java.util.Collections;
import java.util.Iterator;
import java.util.ArrayList;

public class SosGame {
    private static final int DEFAULT_BOARD_SIZE = 3;
    private static final int MIN_PLAYERS = 2;

    private SosBoard board;
    private ArrayList<Player> players;
    private Player currentPlayer;
    private Iterator<Player> iterator;

    public SosGame() {
        this(DEFAULT_BOARD_SIZE);
    }

    public SosGame(int boardSize) {
        board = new SosBoard(boardSize);
        players = new ArrayList<Player>();
        currentPlayer = null;
        iterator = null;
    }

    public void addPlayer(String name, int id) throws GameException {
        if (playing()) {
            throw new GameException(GameException.GAME_STATE);
        }
        for (Player player : players) {
            if (player.getName().equals(name))
                throw new GameException(GameException.INVALID_NAME);
        }
        players.add(new Player(name, id));
    }

    public void play() throws GameException {
        if (players.size() < MIN_PLAYERS) {
            throw new GameException(GameException.NOT_ENOUGH_PLAYERS);
        }
        if (playing()) {
            throw new GameException(GameException.GAME_STATE);
        }
        Collections.shuffle(players);
        iterator = players.iterator();
        currentPlayer = iterator.next();
    }

    public int move(String move, int playerId) throws GameException {
        if (!playing()) {
            throw new GameException(GameException.GAME_STATE);
        }
        if (playerId != currentPlayer.getId()) {
            throw new GameException(GameException.OUT_OF_TURN);
        }
        int pointsEarned = parseMove(move);
        if (pointsEarned < 0) {
            throw new GameException(GameException.INVALID_MOVE);
        }
        else {
            currentPlayer.addPoints(pointsEarned);
            next();
            int nextId = -1;
            if (currentPlayer != null) {
                nextId = currentPlayer.getId();
            }
            return nextId;
        }
    }

    private int parseMove(String move) throws GameException {
        try {
            String[] moveArr = move.split("\\s+");
            return board.move(Character.toUpperCase((moveArr[1].toCharArray())[0]),
                                Integer.parseInt(moveArr[2]),
                                Integer.parseInt(moveArr[3]));
        } catch (NumberFormatException e) {
            throw new GameException(GameException.INVALID_MOVE);
        }
    }

    private void next() {
        if (board.isFull()) {
            currentPlayer = null;
            iterator = null;
        }
        else {
            if (iterator.hasNext()) {
                currentPlayer = iterator.next();
            } else {
                iterator = players.iterator();
                currentPlayer = iterator.next();
            }
        }
    }

    public boolean playing() {
        boolean playing = false;
        if (currentPlayer != null) {
            playing = true;
        }
        return playing;
    }

    public String getBoard() {
        return board.toString();
    }

    public String toString() {
        String game = board.toString();
        for (Player player: players) {
            game += player.toString();
        }
        return game;
    }
}

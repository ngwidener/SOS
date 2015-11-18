package game;

import java.util.Collections;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Contains the logic for our SOS game.
 *
 * @author Jameson Burchette
 * @author Nicholas Widener
 * @version November 2015
 */
public class SosGame {
    /**The default board size*/
    public static final int DEFAULT_BOARD_SIZE = 3;

    /**The minimum number of players*/
    public static final int MIN_PLAYERS = 2;

    /**The game board*/
    private SosBoard board;

    /**The board size*/
    private int boardSize;

    /**List of players*/
    private ArrayList<Player> players;

    /**The current player*/
    private Player currentPlayer;

    /**Iterate through the players*/
    private Iterator<Player> iterator;

    /**
     * Constructor; creates a new game.
     * @param boardSize the size of the game board (NxN).
     */
    public SosGame(int boardSize) {
        board = new SosBoard(boardSize);
        this.boardSize = boardSize;
        players = new ArrayList<Player>();
        currentPlayer = null;
        iterator = null;
    }

    /**
     * Add a player to the game.
     * @param name the player's name.
     * @param id the player's id.
     * @throws GameException if there is a game in progress or
     * if the player enters an invalid name.
     */
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

    /**
     * Remove a player from the game.
     * @param id the player's id.
     */
    public void removePlayer(int id) {
        players.remove(getPlayer(id));
    }

    /**
     * Play a game.
     * @return current player id.
     * @throws GameException if a game is in progress or
     * not enough players are playing the game.
     */
    public int play() throws GameException {
        if (players.size() < MIN_PLAYERS) {
            throw new GameException(GameException.NOT_ENOUGH_PLAYERS);
        }
        if (playing()) {
            throw new GameException(GameException.GAME_STATE);
        }
        Collections.shuffle(players);
        iterator = players.iterator();
        currentPlayer = iterator.next();
        return currentPlayer.getId();
    }

    /**
     * Make a move in the game.
     * @param move the move to make.
     * @param playerId the player's id that is making the move.
     * @return the next id of the player that moves next.
     * @throws GameException if a game is in progress, the player is out of
     * turns, or the player makes an invalid move.
     */
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

    /**
     * Private helper method to parse the moves.
     * @param move the move.
     * @return the move.
     * @throws GameException if an invalid move is made.
     */
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

    /**
     * Checks to see if a game is already in progress.
     * @return true if a game is in progress,
     * false otherwise.
     */
    public boolean playing() {
        boolean playing = false;
        if (currentPlayer != null) {
            playing = true;
        }
        return playing;
    }

    /**
     * Reset the game.
     */
    public void reset() {
        board = new SosBoard(boardSize);
        currentPlayer = null;
        iterator = null;
    }

    /**
     * Get the player's name.
     * @param id the player's id.
     * @return the player's name.
     */
    public String getPlayerName(int id) {
        return getPlayer(id).getName();
    }

    /**
     * Private helper method to get the player.
     * @param id the player's id.
     * @return the player.
     */
    private Player getPlayer(int id) {
        Player player = null;
        for (Player p : players) {
            if (p.getId() == id)
                player = p;
        }
        return player;
    }

    /**
     * Private helper method for next move.
     */
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

    /**
     * Get the game board.
     * @return the game board.
     */
    public String getBoard() {
        return board.toString();
    }

    /**
     * Format the game with board.
     * @return the game with board.
     */
    public String toString() {
        String game = board.toString();
        for (Player player: players) {
            game += player.toString();
        }
        return game;
    }
}

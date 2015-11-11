package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Game {
    private SosBoard board;
    private List<Player> players;
    private Player currentPlayer;
    private Iterator<Player> iterator;

    public Game(int boardSize, List<String> names) {
        board = new SosBoard(boardSize);
        players = new ArrayList<Player>(names.size());
        for (String name : names) {
            players.add(new Player(name));
        }
        Collections.shuffle(players);
        iterator = players.iterator();
        currentPlayer = iterator.next();
    }

    public String move(String input) {
        String[] move = input.split("\\s+");
        char letter = Character.toUpperCase(move[1].toCharArray()[0]);
        int row = Integer.parseInt(move[2]);
        int column = Integer.parseInt(move[3]);
        return move(letter, row, column);
    }

    public String move(char letter, int row, int column) {
        int pointsEarned = board.move(letter, row, column);
        String nextPlayer;
        if (pointsEarned >= 0) {
            currentPlayer.addPoints(pointsEarned);
            nextPlayer = nextPlayer();
        }
        else {
            nextPlayer = "invalid move";
        }
        if (board.isFull()) {
            nextPlayer = "game over";
        }
        return nextPlayer;
    }

    private String nextPlayer() {
        if (iterator.hasNext()) {
            currentPlayer = iterator.next();
        } else {
            iterator = players.iterator();
            currentPlayer = iterator.next();
        }
        return currentPlayer.getName();
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
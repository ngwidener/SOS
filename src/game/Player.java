package game;

/**
 * Models a player within a game.
 *
 * @author Jameson Burchette
 * @author Nicholas Widener
 * @version November 2015
 */
public class Player {
    /**Player name*/
    private String name;

    /**Player ID*/
    private int id;

    /**Player points*/
    private int points;

    /**
     * Constructor; creates a new player with player name and id.
     * @param name the player's name.
     * @param id the player's id.
     */
    public Player (String name, int id) {
        this.name = name;
        this.id = id;
        this.points = 0;
    }

    /**
     * Get the player's name.
     * @return the player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the player's id.
     * @return the player's id.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the player's points.
     * @return the player's points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Add points.
     * @param points points to add.
     */
    public void addPoints(int points) {
        this.points += points;
    }

    /**
     * Format how many points a player has.
     * @return how many points a player has.
     */
    public String toString() {
        return name + " has " + points + " points\n";
    }
}

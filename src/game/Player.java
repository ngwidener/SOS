package game;

public class Player {

    private String name;
    private int id;
    private int points;

    public Player (String name, int id) {
        this.name = name;
        this.id = id;
        this.points = 0;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public String toString() {
        return "Player " + name + " has " + points + " points\n";
    }
}

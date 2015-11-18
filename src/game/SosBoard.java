package game;

/**
 * Creates the board for our SOS game.
 *
 * @author Jameson Burchette
 * @author Nicholas Widener
 * @version November 2015
 */
public class SosBoard {
    private char[][] grid;
    private int size;
    private int squaresLeft;

    /**
     * Constructor; creates a new board.
     * @param size the size of the board (NxN).
     */
    public SosBoard (int size) {
        grid = new char[size][size];
        this.size = size;
        squaresLeft = size * size;
    }

    /**
     * Make a move on the board.
     * @param letter the letter we want to make a move with (S or O).
     * @param row the row to make a move in.
     * @param column the column to make a move in.
     * @return the move with the letter in the row/column we want to
     * make a move in.
     */
    public int move(char letter, int row, int column) {
        int pointsEarned;
        try {
            if (letter == 'S' && grid[row][column] == '\0') {
                grid[row][column] = 'S';
                squaresLeft--;
                pointsEarned = pointCheck(2, row, column);
            } else if (letter == 'O' && grid[row][column] == '\0') {
                grid[row][column] = 'O';
                squaresLeft--;
                pointsEarned = pointCheck(1, row, column);
            }
            else pointsEarned = -1;
        } catch (ArrayIndexOutOfBoundsException e) {
            pointsEarned = -1;
        }
        return pointsEarned;
    }

    /**
     * Check the points in all directions on the board.
     * @param depth the depth of the board.
     * @param row the row in the board.
     * @param column the column in the board.
     * @return the points that have been accumulated.
     */
    private int pointCheck(int depth, int row, int column) {
        int pointsEarned = 0;
        pointsEarned += checkHorizontal(depth, row, column);
        pointsEarned += checkVertical(depth, row, column);
        pointsEarned += checkDiagonalOne(depth, row, column);
        pointsEarned += checkDiagonalTwo(depth, row, column);
        return pointsEarned;
    }

    /**
     * Check the points vertically.
     * @param distance the distance vertically.
     * @param row the row.
     * @param column the column.
     * @return points accumulated vertically.
     */
    private int checkVertical(int distance, int row, int column) {
        int pointsEarned = 0;
        for (int i = row - distance; i <= row; i++) {
            try {
                if (grid[i][column] == 'S' && grid[i + 1][column] == 'O' && grid[i + 2][column] == 'S')
                    pointsEarned++;
            } catch (ArrayIndexOutOfBoundsException e) {}
        }
        return pointsEarned;
    }

    /**
     * Check the points horizontally.
     * @param distance the distance horizontally.
     * @param row the row.
     * @param column the column.
     * @return the points accumulated horizontally.
     */
    private int checkHorizontal(int distance, int row, int column) {
        int pointsEarned = 0;
        for (int i = column - distance; i <= column; i++) {
            try {
                if (grid[row][i] == 'S' && grid[row][i + 1] == 'O' && grid[row][i + 2] == 'S')
                    pointsEarned++;
            } catch (ArrayIndexOutOfBoundsException e) {}
        }
        return pointsEarned;
    }

    /**
     * Check the points one direction diagonally.
     * @param distance the distance diagonally.
     * @param row the row.
     * @param column the column.
     * @return the points earned diagonally.
     */
    private int checkDiagonalOne(int distance, int row, int column) {
        int pointsEarned = 0;
        int curRow = row - distance;
        int curColumn = column - distance;
        while (curRow <= row && curColumn <= column) {
            try {
                if (grid[curRow][curColumn] == 'S' && grid[curRow + 1][curColumn + 1] == 'O' &&
                        grid[curRow + 2][curColumn + 2] == 'S') {
                    pointsEarned++;
                }
            } catch (ArrayIndexOutOfBoundsException e) {}
            curRow++;
            curColumn++;
        }
        return pointsEarned;
    }

    /**
     * Check the points earned diagonally the opposite direction.
     * @param distance the distance diagonally.
     * @param row the row.
     * @param column the column.
     * @return the points earned diagonally.
     */
    private int checkDiagonalTwo(int distance, int row, int column) {
        int pointsEarned = 0;
        int curRow = row + distance;
        int curColumn = column - distance;
        while (curRow >= row&& curColumn <= column) {
            try {
                if (grid[curRow][curColumn] == 'S' && grid[curRow - 1][curColumn + 1] == 'O' &&
                        grid[curRow - 2][curColumn + 2] == 'S') {
                    pointsEarned++;
                }
            } catch (ArrayIndexOutOfBoundsException e) {}
            curRow--;
            curColumn++;
        }
        return pointsEarned;
    }

    /**
     * Checks to see if the board is full.
     * @return true if the board is full, false otherwise.
     */
    public boolean isFull() {
        boolean full = false;
        if (squaresLeft < 1)
            full = true;
        return full;
    }

    /**
     * Formats the game board.
     * @return the game board.
     */
    public String toString() {
        String border = "\n\t+";
        for (int i = 0; i < size; i++) {
            border += "---+";
        }
        border += "\n";
        String board = "\t";
        for (int i = 0; i < size; i++) {
            board += "  " + i + " ";
        }
        board += "\t" + border;
        for (int i = 0; i < size; i++) {
            board += i + "\t|";
            for (int j = 0; j < size; j++) {
                board += " " + grid[i][j] + " |";
            }
            board += border;
        }
        return board;
    }
}

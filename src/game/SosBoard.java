package game;

public class SosBoard {
    private char[][] grid;
    private int size;
    private int squaresLeft;

    public SosBoard (int size) {
        grid = new char[size][size];
        this.size = size;
        squaresLeft = size * size;
    }

    public int move(char letter, int row, int column) {
        int pointsEarned;
        try {
            if (letter == 'S' && grid[row][column] == '\0') {
                grid[row][column] = 'S';
                pointsEarned = pointCheck(2, row, column);
            } else if (letter == 'O' && grid[row][column] == '\0') {
                grid[row][column] = 'O';
                pointsEarned = pointCheck(1, row, column);
            }
            else pointsEarned = -1;
        } catch (ArrayIndexOutOfBoundsException e) {
            pointsEarned = -1;
        }
        return pointsEarned;
    }

    private int pointCheck(int depth, int row, int column) {
        int pointsEarned = 0;
        pointsEarned += checkHorizontal(depth, row, column);
        pointsEarned += checkVertical(depth, row, column);
        pointsEarned += checkDiagonalOne(depth, row, column);
        pointsEarned += checkDiagonalTwo(depth, row, column);
        return pointsEarned;
    }

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

    private int checkDiagonalOne(int distance, int row, int column) {
        int pointsEarned = 0;
        int curRow = row - distance;
        int curColumn = column - distance;
        while (curRow <= row && curColumn <= column) {
            try {
                if (grid[curRow][curColumn] == 'S' && grid[curRow + 1][curColumn + 1] == 'O' && grid[curRow + 2][curColumn + 2] == 'S')
                    pointsEarned++;
            } catch (ArrayIndexOutOfBoundsException e) {}
            curRow++;
            curColumn++;
        }
        return pointsEarned;
    }

    private int checkDiagonalTwo(int distance, int row, int column) {
        int pointsEarned = 0;
        int curRow = row + distance;
        int curColumn = column - distance;
        while (curRow >= row&& curColumn <= column) {
            try {
                if (grid[curRow][curColumn] == 'S' && grid[curRow - 1][curColumn + 1] == 'O' && grid[curRow - 2][curColumn + 2] == 'S')
                    pointsEarned++;
            } catch (ArrayIndexOutOfBoundsException e) {}
            curRow--;
            curColumn++;
        }
        return pointsEarned;
    }

    public boolean isFull() {
        boolean full = false;
        if (squaresLeft < 1)
            full = true;
        return full;
    }

    public String toString() {
        String board = "";
        for (int i = 0; i < size; i++) {
            board += "\t" + i;
        }
        board += "\n";
        for (int i = 0; i < size; i++) {
            board += i + "\t";
            for (int j = 0; j < size; j++) {
                board += "[" + grid[i][j] + "]\t";
            }
            board += "\n";
        }
        return board;
    }
}

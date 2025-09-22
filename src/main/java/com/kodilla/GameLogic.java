package com.kodilla;

import java.util.ArrayList;
import java.util.List;

public class GameLogic {
    private final int size;
    private final int winLength;
    private char[][] board;

    public GameLogic() {
        this(3, 3);
    }

    public GameLogic(int size, int winLength) {
        this.size = size;
        this.winLength = winLength;
        this.board = new char[size][size];
    }

    public void setBoard(char[][] newBoard) {
        for (int r = 0; r < size; r++) {
            System.arraycopy(newBoard[r], 0, board[r], 0, size);
        }
    }

    public boolean makeMove(int row, int col, char player) {
        if (board[row][col] != '\0') {
            throw new IllegalArgumentException("Pole jest już zajęte!");
        }
        board[row][col] = player;
        return true;
    }

    public boolean checkWin(char player) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (checkDirection(row, col, 1, 0, player) ||  // poziomo
                        checkDirection(row, col, 0, 1, player) ||  // pionowo
                        checkDirection(row, col, 1, 1, player) ||  // ukośnie ↘
                        checkDirection(row, col, 1, -1, player)) { // ukośnie ↙
                    return true;
                }
            }
        }
        return false;
    }

    public List<int[]> getWinningLine(char player) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (checkDirection(row, col, 1, 0, player)) return buildLine(row, col, 1, 0);
                if (checkDirection(row, col, 0, 1, player)) return buildLine(row, col, 0, 1);
                if (checkDirection(row, col, 1, 1, player)) return buildLine(row, col, 1, 1);
                if (checkDirection(row, col, 1, -1, player)) return buildLine(row, col, 1, -1);
            }
        }
        return null;
    }

    private List<int[]> buildLine(int row, int col, int dRow, int dCol) {
        List<int[]> line = new ArrayList<>();
        for (int i = 0; i < winLength; i++) {
            line.add(new int[]{row + i * dRow, col + i * dCol});
        }
        return line;
    }

    private boolean checkDirection(int row, int col, int dRow, int dCol, char player) {
        int count = 0;
        for (int i = 0; i < winLength; i++) {
            int r = row + i * dRow;
            int c = col + i * dCol;
            if (r >= 0 && r < size && c >= 0 && c < size && board[r][c] == player) {
                count++;
            } else {
                break;
            }
        }
        return count == winLength;
    }

    public boolean isBoardFull() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == '\0') return false;
            }
        }
        return true;
    }

    public char[][] getBoard() {
        return board;
    }

    public void reset() {
        board = new char[size][size];
    }
}

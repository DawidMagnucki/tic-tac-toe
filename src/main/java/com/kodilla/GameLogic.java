package com.kodilla;

public class GameLogic {
    private char[][] board = new char[3][3];

    public boolean makeMove(int row, int col, char player) {
        if (board[row][col] != '\0') {
            throw new IllegalArgumentException("Pole jest już zajęte!");
        }
        board[row][col] = player;
        return true;
    }

    public boolean checkWin(char player) {
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == player && board[i][1] == player && board[i][2] == player) ||
                    (board[0][i] == player && board[1][i] == player && board[2][i] == player)) {
                return true;
            }
        }
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
                (board[0][2] == player && board[1][1] == player && board[2][0] == player);
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
        board = new char[3][3];
    }
}
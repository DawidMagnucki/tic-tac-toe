package com.kodilla;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BotPlayer {
    private final String difficulty;
    private final int boardSize;
    private final int winLength;
    private final char[][] board;

    public BotPlayer(String difficulty, int boardSize, int winLength) {
        this.difficulty = difficulty;
        this.boardSize = boardSize;
        this.winLength = winLength;
        this.board = new char[boardSize][boardSize];
    }

    public void setBoard(char[][] newBoard) {
        for (int r = 0; r < boardSize; r++) {
            System.arraycopy(newBoard[r], 0, board[r], 0, boardSize);
        }
    }

    public int[] chooseMove(char[][] board, char botSymbol, char playerSymbol) {
        switch (difficulty) {
            case "Łatwy":
                return randomMove(board);
            case "Normalny":
                return defensiveMove(board, botSymbol, playerSymbol);
            case "Trudny":
                return smartMove(board, botSymbol, playerSymbol);
            default:
                return randomMove(board);
        }
    }

    private int[] randomMove(char[][] board) {
        List<int[]> moves = new ArrayList<>();
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (board[r][c] == '\0') moves.add(new int[]{r, c});
            }
        }
        return moves.isEmpty() ? null : moves.get(new Random().nextInt(moves.size()));
    }

    private int[] defensiveMove(char[][] board, char bot, char player) {
        GameLogic logic = new GameLogic(boardSize, winLength);

        // 1. Spróbuj wygrać
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (board[r][c] == '\0') {
                    char[][] tempBoard = copyBoard(board);
                    tempBoard[r][c] = bot;
                    logic.setBoard(tempBoard);
                    if (logic.checkWin(bot)) {
                        board[r][c] = '\0';
                        return new int[]{r, c};
                    }
                    board[r][c] = '\0';
                }
            }
        }

        // 2. Spróbuj zablokować gracza
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (board[r][c] == '\0') {
                    board[r][c] = player;
                    logic.setBoard(board);
                    if (logic.checkWin(player)) {
                        board[r][c] = '\0';
                        return new int[]{r, c};
                    }
                    board[r][c] = '\0';
                }
            }
        }

        // 3. Wykonaj losowy ruch
        return randomMove(board);
    }

    private char[][] copyBoard(char[][] original) {
        char[][] copy = new char[boardSize][boardSize];
        for (int r = 0; r < boardSize; r++) {
            System.arraycopy(original[r], 0, copy[r], 0, boardSize);
        }
        return copy;
    }


    private int[] smartMove(char[][] board, char bot, char player) {
        GameLogic logic = new GameLogic(boardSize, winLength);

        // 1. Spróbuj wygrać
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (board[r][c] == '\0') {
                    board[r][c] = bot;
                    logic.setBoard(board);
                    if (logic.checkWin(bot)) {
                        board[r][c] = '\0';
                        return new int[]{r, c};
                    }
                    board[r][c] = '\0';
                }
            }
        }

        // 2. Spróbuj zablokować gracza
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (board[r][c] == '\0') {
                    board[r][c] = player;
                    logic.setBoard(board);
                    if (logic.checkWin(player)) {
                        board[r][c] = '\0';
                        return new int[]{r, c};
                    }
                    board[r][c] = '\0';
                }
            }
        }

        // 3. Szukaj miejsc, gdzie gracz ma 3 lub 4 symbole w linii
        int[] threatMove = findThreat(board, player, winLength - 1);
        if (threatMove != null) return threatMove;

        // 4. Wybierz ruch strategiczny (np. środek planszy)
        if (boardSize % 2 == 1 && board[boardSize / 2][boardSize / 2] == '\0') {
            return new int[]{boardSize / 2, boardSize / 2};
        }

        // 5. Wykonaj losowy ruch
        return randomMove(board);
    }

    private int[] findThreat(char[][] board, char player, int threatLength) {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                // Poziomo →
                if (checkThreat(board, row, col, 0, 1, player, threatLength)) {
                    return findEmptyInLine(board, row, col, 0, 1, threatLength);
                }
                // Pionowo ↓
                if (checkThreat(board, row, col, 1, 0, player, threatLength)) {
                    return findEmptyInLine(board, row, col, 1, 0, threatLength);
                }
                // Ukośnie ↘
                if (checkThreat(board, row, col, 1, 1, player, threatLength)) {
                    return findEmptyInLine(board, row, col, 1, 1, threatLength);
                }
                // Ukośnie ↙
                if (checkThreat(board, row, col, 1, -1, player, threatLength)) {
                    return findEmptyInLine(board, row, col, 1, -1, threatLength);
                }
            }
        }
        return null;
    }

    private boolean checkThreat(char[][] board, int row, int col, int dRow, int dCol, char player, int threatLength) {
        int count = 0;
        int empty = 0;

        for (int i = 0; i < winLength; i++) {
            int r = row + i * dRow;
            int c = col + i * dCol;
            if (r < 0 || r >= boardSize || c < 0 || c >= boardSize) return false;

            if (board[r][c] == player) count++;
            else if (board[r][c] == '\0') empty++;
            else return false;
        }

        return count == threatLength && empty == (winLength - threatLength);
    }

    private int[] findEmptyInLine(char[][] board, int row, int col, int dRow, int dCol, int threatLength) {
        for (int i = 0; i < winLength; i++) {
            int r = row + i * dRow;
            int c = col + i * dCol;
            if (r >= 0 && r < boardSize && c >= 0 && c < boardSize && board[r][c] == '\0') {
                return new int[]{r, c};
            }
        }
        return null;
    }

}

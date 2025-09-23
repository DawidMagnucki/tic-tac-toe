package com.kodilla;

import java.io.Serializable;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private char[][] board;
    private char currentPlayer;
    private int scoreX;
    private int scoreO;
    private String playerXName;
    private String playerOName;
    private boolean botEnabled;
    private String difficultyLevel;
    private int boardSize;
    private int winLength;

    public GameState(char[][] board, char currentPlayer, int scoreX, int scoreO,
                     String playerXName, String playerOName, boolean botEnabled,
                     String difficultyLevel, int boardSize, int winLength) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.scoreX = scoreX;
        this.scoreO = scoreO;
        this.playerXName = playerXName;
        this.playerOName = playerOName;
        this.botEnabled = botEnabled;
        this.difficultyLevel = difficultyLevel;
        this.boardSize = boardSize;
        this.winLength = winLength;
    }

    public char[][] getBoard() {
        return board;
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public int getScoreX() {
        return scoreX;
    }

    public int getScoreO() {
        return scoreO;
    }

    public String getPlayerXName() {
        return playerXName;
    }

    public String getPlayerOName() {
        return playerOName;
    }

    public boolean isBotEnabled() {
        return botEnabled;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getWinLength() {
        return winLength;
    }
}

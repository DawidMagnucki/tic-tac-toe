package com.kodilla;

import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.List;

public class GameController {
    private final int boardSize;
    private final int winLength;
    private final GameLogic gameLogic;
    private final GameBoard gameBoard;
    private final GameOverlay gameOverlay;
    private final SoundManager soundManager;
    private final String difficultyLevel;
    private final BotPlayer botPlayer;
    private final boolean botEnabled;
    private final Label statusLabel;
    private final Label scoreXLabel;
    private final Label scoreOLabel;
    private final String playerXName;
    private final String playerOName;
    private char currentPlayer = 'X';
    private int scoreX = 0;
    private int scoreO = 0;
    private Runnable scoreUpdateCallback;

    public GameController(int boardSize, int winLength, boolean botEnabled, String difficultyLevel,
                          String playerXName, String playerOName, StackPane gridWrapper,
                          Label statusLabel, Label scoreXLabel, Label scoreOLabel) {
        this.boardSize = boardSize;
        this.winLength = winLength;
        this.botEnabled = botEnabled;
        this.difficultyLevel = difficultyLevel != null ? difficultyLevel : "PvP";
        this.playerXName = playerXName;
        this.playerOName = playerOName;
        this.statusLabel = statusLabel;
        this.scoreXLabel = scoreXLabel;
        this.scoreOLabel = scoreOLabel;
        this.gameLogic = new GameLogic(boardSize, winLength);
        this.botPlayer = botEnabled ? new BotPlayer(this.difficultyLevel, boardSize, winLength) : null;
        this.soundManager = new SoundManager();
        this.gameBoard = new GameBoard(boardSize, 500 / boardSize, this::handlePlayerMove);
        this.gameOverlay = new GameOverlay(gridWrapper);
        gridWrapper.getChildren().add(gameOverlay.getOverlay());
    }

    public void setScoreUpdateCallback(Runnable callback) {
        this.scoreUpdateCallback = callback;
    }

    private String getCurrentPlayerName() {
        return currentPlayer == 'X' ? playerXName : playerOName;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public GameOverlay getGameOverlay() {
        return gameOverlay;
    }

    public StackPane getFullBoardView() {
        return new StackPane(gameBoard.getGrid(), gameOverlay.getOverlay());
    }

    public int getScoreX() {
        return scoreX;
    }

    public int getScoreO() {
        return scoreO;
    }

    public void resetGame() {
        gameLogic.reset();
        currentPlayer = 'X';
        gameOverlay.clearOverlay();
        statusLabel.setText("Ruch gracza: " + getCurrentPlayerName());
        gameBoard.resetBoard();
        scoreXLabel.setText(playerXName + ": " + scoreX);
        scoreOLabel.setText(playerOName + ": " + scoreO);
    }

    private boolean isGameOver() {
        return gameBoard.isDisabled();
    }

    private void handlePlayerMove(int row, int col) {
        if (isGameOver()) return;

        try {
            gameLogic.makeMove(row, col, currentPlayer);
            gameBoard.setSymbol(row, col, currentPlayer, currentPlayer == 'X' ? "red" : "blue", boardSize >= 10);
            soundManager.playClick();

            if (gameLogic.checkWin(currentPlayer)) {
                List<int[]> winLine = gameLogic.getWinningLine(currentPlayer);
                gameOverlay.drawWinningLine(winLine, gameBoard);
                soundManager.playWin();
                statusLabel.setText("Gracz " + getCurrentPlayerName() + " wygrał!");
                if (currentPlayer == 'X') scoreX++;
                else scoreO++;
                scoreXLabel.setText(playerXName + ": " + scoreX);
                scoreOLabel.setText(playerOName + ": " + scoreO);
                RankingManager rankingManager = new RankingManager();
                GameResult result = new GameResult(getCurrentPlayerName(), scoreX + scoreO, currentPlayer == 'X' ? scoreX : scoreO, LocalDate.now());
                rankingManager.saveResult(result);
                if (scoreUpdateCallback != null) scoreUpdateCallback.run();
                gameBoard.disableBoard();
            } else if (gameLogic.isBoardFull()) {
                statusLabel.setText("Remis!");
                soundManager.playDraw();
                gameBoard.disableBoard();
            } else {
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                statusLabel.setText("Ruch gracza: " + getCurrentPlayerName());

                if (botEnabled && currentPlayer == 'O') {
                    PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    pause.setOnFinished(e -> makeComputerMove());
                    pause.play();
                }
            }
        } catch (IllegalArgumentException ex) {
            statusLabel.setText("Nieprawidłowy ruch!");
        }
    }

    private void makeComputerMove() {
        if (botPlayer == null) return;

        char[][] board = gameLogic.getBoard();
        int[] move = botPlayer.chooseMove(board, 'O', 'X');

        if (move == null) return;

        int row = move[0];
        int col = move[1];

        try {
            gameLogic.makeMove(row, col, 'O');
            gameBoard.setSymbol(row, col, 'O', "blue", boardSize >= 10);
            soundManager.playClick();

            if (gameLogic.checkWin('O')) {
                List<int[]> winLine = gameLogic.getWinningLine('O');
                gameOverlay.drawWinningLine(winLine, gameBoard);
                soundManager.playWin();
                statusLabel.setText("Gracz " + getCurrentPlayerName() + " wygrał!");
                scoreO++;
                scoreXLabel.setText(playerXName + ": " + scoreX);
                scoreOLabel.setText(playerOName + ": " + scoreO);
                statusLabel.getScene().getWindow().requestFocus();
                RankingManager rankingManager = new RankingManager();
                GameResult result = new GameResult(playerOName, scoreX + scoreO, scoreO, LocalDate.now());
                rankingManager.saveResult(result);
                if (scoreUpdateCallback != null) scoreUpdateCallback.run();
                gameBoard.disableBoard();
            } else if (gameLogic.isBoardFull()) {
                statusLabel.setText("Remis!");
                soundManager.playDraw();
                gameBoard.disableBoard();
            } else {
                currentPlayer = 'X';
                statusLabel.setText("Ruch gracza: " + getCurrentPlayerName());
            }
        } catch (IllegalArgumentException ignored) {
        }
    }

    public void updateScoreLabels() {
        scoreXLabel.setText(playerXName + ": " + scoreX);
        scoreOLabel.setText(playerOName + ": " + scoreO);
    }


    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(char player) {
        this.currentPlayer = player;
        statusLabel.setText("Ruch gracza: " + getCurrentPlayerName());
    }

}

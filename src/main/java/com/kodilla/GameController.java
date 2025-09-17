package com.kodilla;

import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController {
    private final int boardSize;
    private final int winLength;
    private final GameLogic gameLogic;
    private final GameBoard gameBoard;
    private final GameOverlay gameOverlay;
    private final SoundManager soundManager;
    private final Label statusLabel;
    private char currentPlayer = 'X';
    private int scoreX = 0;
    private int scoreO = 0;

    public GameController(int boardSize, int winLength, StackPane gridWrapper, Label statusLabel) {
        this.boardSize = boardSize;
        this.winLength = winLength;
        this.statusLabel = statusLabel;
        this.gameLogic = new GameLogic(boardSize, winLength);
        this.soundManager = new SoundManager();
        this.gameBoard = new GameBoard(boardSize, 500 / boardSize, this::handlePlayerMove);
        this.gameOverlay = new GameOverlay(gridWrapper);
        gridWrapper.getChildren().add(gameOverlay.getOverlay());
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void resetGame() {
        gameLogic.reset();
        currentPlayer = 'X';
        gameOverlay.clearOverlay();
        statusLabel.setText("Ruch gracza: X");
    }

    private void handlePlayerMove(int row, int col) {
        if (currentPlayer != 'X') return;

        try {
            gameLogic.makeMove(row, col, currentPlayer);
            gameBoard.setSymbol(row, col, currentPlayer, "red", boardSize >= 10);
            soundManager.playClick();

            if (gameLogic.checkWin(currentPlayer)) {
                List<int[]> winLine = gameLogic.getWinningLine(currentPlayer);
                gameOverlay.drawWinningLine(winLine, gameBoard);
                soundManager.playWin();
                statusLabel.setText("Gracz X wygrał!");
                scoreX++;
                gameBoard.disableBoard();
            } else if (gameLogic.isBoardFull()) {
                statusLabel.setText("Remis!");
                soundManager.playDraw();
                gameBoard.disableBoard();
            } else {
                currentPlayer = 'O';
                statusLabel.setText("Ruch gracza: O");
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(e -> makeComputerMove());
                pause.play();
            }
        } catch (IllegalArgumentException ex) {
            statusLabel.setText("Nieprawidłowy ruch!");
        }
    }

    private void makeComputerMove() {
        List<int[]> freeCells = new ArrayList<>();
        char[][] board = gameLogic.getBoard();
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (board[row][col] == '\0') {
                    freeCells.add(new int[]{row, col});
                }
            }
        }

        if (freeCells.isEmpty()) return;

        int[] move = freeCells.get(new Random().nextInt(freeCells.size()));
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
                statusLabel.setText("Komputer wygrał!");
                scoreO++;
                gameBoard.disableBoard();
            } else if (gameLogic.isBoardFull()) {
                statusLabel.setText("Remis!");
                soundManager.playDraw();
                gameBoard.disableBoard();
            } else {
                currentPlayer = 'X';
                statusLabel.setText("Ruch gracza: X");
            }
        } catch (IllegalArgumentException ignored) {}
    }

    public GameOverlay getGameOverlay() {
        return gameOverlay;
    }

    public StackPane getFullBoardView() {
        return new StackPane(gameBoard.getGrid(), gameOverlay.getOverlay());
    }


}

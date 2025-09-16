package com.kodilla;

import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TicTacToe extends Application {
    private Image gameboard = new Image("file:src/main/resources/Tic-Tac-Toe Board 300x300.png");
    private char currentPlayer = 'X';
    private int scoreX = 0;
    private int scoreO = 0;

    private Label statusLabel = new Label("Ruch gracza: X");
    private Label scoreLabel = new Label("X: 0   O: 0");
    private GridPane grid = new GridPane();

    private AudioClip clickSound;
    private AudioClip winSound;
    private AudioClip drawSound;

    private GameLogic gameLogic = new GameLogic();

    @Override
    public void start(Stage primaryStage) {
        clickSound = new AudioClip(getClass().getResource("/click.mp3").toString());
        winSound = new AudioClip(getClass().getResource("/win.mp3").toString());
        drawSound = new AudioClip(getClass().getResource("/draw.mp3").toString());

        grid.setBackground(new Background(new BackgroundImage(
                gameboard,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
        )));
        grid.setHgap(0);
        grid.setVgap(0);

        createBoard();

        Button resetButton = new Button("Restart");
        resetButton.setOnAction(e -> resetGame());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(statusLabel, scoreLabel, grid, resetButton);
        layout.setStyle("-fx-padding: 10; -fx-alignment: center;");

        Scene scene = new Scene(layout, 320, 400);
        primaryStage.setTitle("Tic-Tac-Toe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createBoard() {
        grid.getChildren().clear();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(100, 100);
                Label symbol = new Label("");
                symbol.setStyle("-fx-font-size: 48px;");
                cell.getChildren().add(symbol);

                final int r = row;
                final int c = col;
                cell.setOnMouseClicked(e -> {
                    if (symbol.getText().isEmpty() && currentPlayer == 'X') {
                        try {
                            gameLogic.makeMove(r, c, currentPlayer);
                            symbol.setText(String.valueOf(currentPlayer));
                            symbol.setStyle("-fx-font-size: 48px; -fx-text-fill: red;");

                            playClickAnimation(symbol);
                            clickSound.play();

                            if (gameLogic.checkWin(currentPlayer)) {
                                statusLabel.setText("Gracz " + currentPlayer + " wygrał!");
                                winSound.play();
                                updateScore(currentPlayer);
                                disableBoard();
                            } else if (gameLogic.isBoardFull()) {
                                statusLabel.setText("Remis!");
                                drawSound.play();
                                disableBoard();
                            } else {
                                currentPlayer = 'O';
                                statusLabel.setText("Ruch gracza: O");
                                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                                pause.setOnFinished(ev -> makeComputerMove());
                                pause.play();
                            }
                        } catch (IllegalArgumentException ex) {
                            statusLabel.setText("Nieprawidłowy ruch!");
                        }
                    }
                });

                grid.add(cell, col, row);
            }
        }
    }

    private void makeComputerMove() {
        List<int[]> freeCells = new ArrayList<>();
        char[][] board = gameLogic.getBoard();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == '\0') {
                    freeCells.add(new int[]{row, col});
                }
            }
        }

        if (freeCells.isEmpty()) return;

        Random rand = new Random();
        int[] move = freeCells.get(rand.nextInt(freeCells.size()));

        grid.getChildren().forEach(node -> {
            Integer colIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);
            if (rowIndex == move[0] && colIndex == move[1]) {
                StackPane cell = (StackPane) node;
                Label symbol = (Label) cell.getChildren().get(0);

                try {
                    gameLogic.makeMove(move[0], move[1], 'O');
                    symbol.setText("O");
                    symbol.setStyle("-fx-font-size: 48px; -fx-text-fill: blue;");
                    playClickAnimation(symbol);
                    clickSound.play();

                    if (gameLogic.checkWin('O')) {
                        statusLabel.setText("Komputer wygrał!");
                        winSound.play();
                        updateScore('O');
                        disableBoard();
                    } else if (gameLogic.isBoardFull()) {
                        statusLabel.setText("Remis!");
                        drawSound.play();
                        disableBoard();
                    } else {
                        currentPlayer = 'X';
                        statusLabel.setText("Ruch gracza: X");
                    }
                } catch (IllegalArgumentException ignored) {}
            }
        });
    }

    private void updateScore(char winner) {
        if (winner == 'X') scoreX++;
        else if (winner == 'O') scoreO++;
        scoreLabel.setText("X: " + scoreX + "   O: " + scoreO);
    }

    private void resetGame() {
        gameLogic.reset();
        currentPlayer = 'X';
        statusLabel.setText("Ruch gracza: X");
        createBoard();
    }

    private void disableBoard() {
        grid.getChildren().forEach(node -> node.setDisable(true));
    }

    private void playClickAnimation(Label symbol) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), symbol);
        st.setFromX(0);
        st.setFromY(0);
        st.setToX(1);
        st.setToY(1);
        st.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

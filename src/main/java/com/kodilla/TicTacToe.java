package com.kodilla;

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

    // 🔹 Nowa logika gry
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
                    if (symbol.getText().isEmpty()) {
                        try {
                            gameLogic.makeMove(r, c, currentPlayer);
                            symbol.setText(String.valueOf(currentPlayer));
                            symbol.setStyle("-fx-font-size: 48px; -fx-text-fill: " +
                                    (currentPlayer == 'X' ? "red" : "blue") + ";");

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
                                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                                statusLabel.setText("Ruch gracza: " + currentPlayer);
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
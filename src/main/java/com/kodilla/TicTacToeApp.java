package com.kodilla;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TicTacToeApp extends Application {
    private Stage mainStage;
    private int boardSize;
    private int winLength;
    private GameController gameController;

    @Override
    public void start(Stage primaryStage) {
        this.mainStage = primaryStage;
        mainStage.getIcons().add(new javafx.scene.image.Image(getClass().getResource("/icon.png").toString()));
        showStartMenu();
    }

    private void showStartMenu() {
        Label title = UIFactory.createTitle("Wybierz wariant gry:");
        Button classicButton = UIFactory.createMenuButton("Klasyczna 3x3");
        Button extendedButton = UIFactory.createMenuButton("Do pięciu 10x10");
        Button exitButton = UIFactory.createMenuButton("Wyjście");

        classicButton.setOnAction(e -> {
            boardSize = 3;
            winLength = 3;
            launchGame();
        });

        extendedButton.setOnAction(e -> {
            boardSize = 10;
            winLength = 5;
            launchGame();
        });

        exitButton.setOnAction(e -> mainStage.close());

        VBox menuLayout = UIFactory.createMenuLayout(title, classicButton, extendedButton, exitButton);
        UIFactory.setScene(mainStage, menuLayout, "Tic-Tac-Toe");
    }

    private void launchGame() {
        Label statusLabel = UIFactory.createStatusLabel("Ruch gracza: X");
        Label scoreX = UIFactory.createScoreLabel("X", 0, "red");
        Label scoreO = UIFactory.createScoreLabel("O", 0, "blue");

        HBox scoreBox = UIFactory.createScoreBox(scoreX, scoreO);
        Button resetButton = UIFactory.createControlButton("Restart");
        Button backButton = UIFactory.createControlButton("Powrót");

        StackPane gridWrapper = new StackPane();
        gameController = new GameController(boardSize, winLength, gridWrapper, statusLabel);

        resetButton.setOnAction(e -> {
            gameController.resetGame();
            scoreX.setText("Gracz X: 0");
            scoreO.setText("Gracz O: 0");
        });

        backButton.setOnAction(e -> showStartMenu());

        VBox layout = new VBox(20,
                statusLabel,
                scoreBox,
                gameController.getFullBoardView(), // plansza + overlay
                UIFactory.createButtonBox(resetButton, backButton) // ← HBox jako dziecko VBoxa
        );
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        UIFactory.setScene(mainStage, layout, "Tic-Tac-Toe");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

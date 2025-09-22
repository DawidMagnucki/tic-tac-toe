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
    private String difficultyLevel;
    private BotPlayer botPlayer;
    private boolean botEnabled = true;
    private Label scoreXLabel;
    private Label scoreOLabel;



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
            showDifficultyMenu();
        });

        extendedButton.setOnAction(e -> {
            boardSize = 10;
            winLength = 5;
            showDifficultyMenu();
        });

        exitButton.setOnAction(e -> mainStage.close());

        VBox menuLayout = UIFactory.createMenuLayout(title, classicButton, extendedButton, exitButton);
        UIFactory.setScene(mainStage, menuLayout, "Tic-Tac-Toe");
    }

    private void showDifficultyMenu() {
        Label title = UIFactory.createTitle("Wybierz poziom trudności:");
        Button easyButton = UIFactory.createMenuButton("Łatwy");
        Button normalButton = UIFactory.createMenuButton("Normalny");
        Button hardButton = UIFactory.createMenuButton("Trudny");
        Button backButton = UIFactory.createMenuButton("Powrót");

        easyButton.setOnAction(e -> {
            difficultyLevel = "Łatwy";
            launchGame();
        });

        normalButton.setOnAction(e -> {
            difficultyLevel = "Normalny";
            launchGame();
        });

        hardButton.setOnAction(e -> {
            difficultyLevel = "Trudny";
            launchGame();
        });

        backButton.setOnAction(e -> showStartMenu());

        VBox layout = UIFactory.createMenuLayout(title, easyButton, normalButton, hardButton, backButton);
        UIFactory.setScene(mainStage, layout, "Poziom trudności");
    }

    private void launchGame() {
        Label statusLabel = UIFactory.createStatusLabel("Ruch gracza: X");
        scoreXLabel = UIFactory.createScoreLabel("X", 0, "red");
        scoreOLabel = UIFactory.createScoreLabel("O", 0, "blue");

        HBox scoreBox = UIFactory.createScoreBox(scoreXLabel, scoreOLabel);
        Button resetButton = UIFactory.createControlButton("Restart");
        Button backButton = UIFactory.createControlButton("Powrót");

        StackPane gridWrapper = new StackPane();
        gameController = new GameController(boardSize, winLength, difficultyLevel, gridWrapper, statusLabel);
        gameController.setScoreUpdateCallback(this::updateScoreLabels);
        BotPlayer botPlayer = new BotPlayer(difficultyLevel, boardSize, winLength);

        resetButton.setOnAction(e -> {
            gameController.resetGame();
//            scoreX.setText("Gracz X: 0");
//            scoreO.setText("Gracz O: 0");
        });

        backButton.setOnAction(e -> showStartMenu());

        VBox layout = new VBox(20,
                statusLabel,
                scoreBox,
                gameController.getFullBoardView(),
                UIFactory.createButtonBox(resetButton, backButton)
        );
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        UIFactory.setScene(mainStage, layout, "Tic-Tac-Toe");
    }

    private void updateScoreLabels() {
        scoreXLabel.setText("Gracz X: " + gameController.getScoreX());
        scoreOLabel.setText("Gracz O: " + gameController.getScoreO());
    }


    public static void main(String[] args) {
        launch(args);
    }
}

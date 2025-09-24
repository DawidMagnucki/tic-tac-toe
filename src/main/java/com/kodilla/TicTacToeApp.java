package com.kodilla;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TicTacToeApp extends Application {
    private Stage mainStage;
    private int boardSize;
    private int winLength;
    private GameController gameController;
    private String difficultyLevel = "Normalny";
    private String playerXName = "";
    private String playerOName = "";
    private boolean botEnabled = false;
    private Label scoreXLabel;
    private Label scoreOLabel;

    @Override
    public void start(Stage primaryStage) {
        this.mainStage = primaryStage;
        mainStage.getIcons().add(new javafx.scene.image.Image(getClass().getResource("/icon.png").toString()));
        showStartMenu();
    }

    private void showStartMenu() {
        Label title = UIFactory.createTitle("Main Menu:");
        Button newGameButton = UIFactory.createMenuButton("Nowa gra");
        Button loadGameButton = UIFactory.createMenuButton("Wczytaj grę");
        Button rankingButton = UIFactory.createMenuButton("Statystyki");
        Button exitButton = UIFactory.createMenuButton("Wyjście");

        newGameButton.setOnAction(e -> showGameModeMenu());
        loadGameButton.setOnAction(e -> {
            try {
                File file = new File("saved_game.dat");
                GameState state = GameStateManager.loadGame(file);
                restoreGame(state);
            } catch (Exception ex) {
                Label error = UIFactory.createStatusLabel("Nie udało się wczytać gry.");
                VBox layout = new VBox(20, error, UIFactory.createButtonBox(UIFactory.createMenuButton("Powrót")));
                layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
                UIFactory.setScene(mainStage, layout, "Błąd");
            }
        });

        rankingButton.setOnAction(e -> showRankingWindow());
        exitButton.setOnAction(e -> mainStage.close());

        VBox layout = UIFactory.createMenuLayout(title, newGameButton, loadGameButton, rankingButton, exitButton);
        UIFactory.setScene(mainStage, layout, "Tic-Tac-Toe");
    }

    private void restoreGame(GameState state) {
        this.boardSize = state.getBoardSize();
        this.winLength = state.getWinLength();
        this.botEnabled = state.isBotEnabled();
        this.difficultyLevel = state.getDifficultyLevel();
        this.playerXName = state.getPlayerXName();
        this.playerOName = state.getPlayerOName();

        Label statusLabel = UIFactory.createStatusLabel("Ruch gracza: " + (state.getCurrentPlayer() == 'X' ? playerXName : playerOName));
        scoreXLabel = UIFactory.createScoreLabel(playerXName, state.getScoreX(), "red");
        scoreOLabel = UIFactory.createScoreLabel(playerOName, state.getScoreO(), "blue");

        HBox scoreBox = UIFactory.createScoreBox(scoreXLabel, scoreOLabel);
        Button resetButton = UIFactory.createControlButton("Restart");
        Button backButton = UIFactory.createControlButton("Powrót");
        Button saveButton = UIFactory.createControlButton("Zapisz");

        StackPane gridWrapper = new StackPane();
        gameController = new GameController(boardSize, winLength, botEnabled, difficultyLevel, playerXName, playerOName, gridWrapper, statusLabel, scoreXLabel, scoreOLabel);
        gameController.setScoreUpdateCallback(gameController::updateScoreLabels);
        gameController.getGameBoard().setGridState(state.getBoard());
        gameController.setCurrentPlayer(state.getCurrentPlayer());

        resetButton.setOnAction(e -> gameController.resetGame());
        backButton.setOnAction(e -> showStartMenu());
        saveButton.setOnAction(e -> {
            try {
                GameStateManager.saveGame(state, new File("saved_game.dat"));
                statusLabel.setText("Gra została zapisana.");
            } catch (IOException ex) {
                statusLabel.setText("Błąd zapisu gry.");
            }
        });

        VBox layout = UIFactory.createGameLayout(statusLabel, scoreBox, gameController.getFullBoardView(), UIFactory.createButtonBox(resetButton, saveButton, backButton));
        UIFactory.setScene(mainStage, layout, "Tic-Tac-Toe");
    }

    private void showGameModeMenu() {
        Label title = UIFactory.createTitle("Wybierz tryb gry:");
        Button pvpButton = UIFactory.createMenuButton("Gra PvP");
        Button pcButton = UIFactory.createMenuButton("Gra vs PC");
        Button backButton = UIFactory.createMenuButton("Powrót");

        pvpButton.setOnAction(e -> {
            botEnabled = false;
            showNameInputMenu();
        });

        pcButton.setOnAction(e -> {
            botEnabled = true;
            showDifficultySelectionBeforeName();
        });

        backButton.setOnAction(e -> showStartMenu());

        VBox layout = UIFactory.createMenuLayout(title, pvpButton, pcButton, backButton);
        UIFactory.setScene(mainStage, layout, "Tryb gry");
    }

    private void showDifficultySelectionBeforeName() {
        Label title = UIFactory.createTitle("Wybierz poziom trudności:");
        Button easyButton = UIFactory.createMenuButton("Łatwy");
        Button normalButton = UIFactory.createMenuButton("Normalny");
        Button hardButton = UIFactory.createMenuButton("Trudny");
        Button backButton = UIFactory.createMenuButton("Powrót");

        easyButton.setOnAction(e -> {
            difficultyLevel = "Łatwy";
            showNameInputMenu();
        });

        normalButton.setOnAction(e -> {
            difficultyLevel = "Normalny";
            showNameInputMenu();
        });

        hardButton.setOnAction(e -> {
            difficultyLevel = "Trudny";
            showNameInputMenu();
        });

        backButton.setOnAction(e -> showGameModeMenu());

        VBox layout = UIFactory.createMenuLayout(title, easyButton, normalButton, hardButton, backButton);
        UIFactory.setScene(mainStage, layout, "Poziom trudności");
    }

    private void showNameInputMenu() {
        Label title = UIFactory.createTitle(botEnabled ? "Podaj nazwę gracza:" : "Podaj nazwy graczy:");

        TextField nameFieldX = new TextField();
        nameFieldX.setPromptText("Gracz X");
        nameFieldX.setMaxWidth(300);

        TextField nameFieldO = new TextField();
        nameFieldO.setPromptText("Gracz O");
        nameFieldO.setMaxWidth(300);

        Button confirmButton = UIFactory.createMenuButton("Dalej");
        Button backButton = UIFactory.createMenuButton("Powrót");

        confirmButton.setOnAction(e -> {
            if (botEnabled) {
                String name = nameFieldX.getText().trim();
                if (name.isEmpty()) {
                    nameFieldX.setStyle("-fx-border-color: red;");
                    return;
                }
                playerXName = name;
                playerOName = "Komputer";
            } else {
                String nameX = nameFieldX.getText().trim();
                String nameO = nameFieldO.getText().trim();
                boolean valid = true;

                if (nameX.isEmpty()) {
                    nameFieldX.setStyle("-fx-border-color: red;");
                    valid = false;
                }
                if (nameO.isEmpty()) {
                    nameFieldO.setStyle("-fx-border-color: red;");
                    valid = false;
                }
                if (!valid) return;

                playerXName = nameX;
                playerOName = nameO;
            }

            showVariantMenu();
        });

        backButton.setOnAction(e -> showGameModeMenu());

        VBox layout = botEnabled
                ? new VBox(20, title, nameFieldX, UIFactory.createButtonBox(confirmButton, backButton))
                : new VBox(20, title, nameFieldX, nameFieldO, UIFactory.createButtonBox(confirmButton, backButton));

        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        UIFactory.setScene(mainStage, layout, "Nazwa gracza");
    }

    private void showVariantMenu() {
        Label title = UIFactory.createTitle("Wybierz wariant gry:");
        Button classicButton = UIFactory.createMenuButton("Gra 3x3");
        Button extendedButton = UIFactory.createMenuButton("Gra 10x10");
        Button backButton = UIFactory.createMenuButton("Powrót");

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

        backButton.setOnAction(e -> showNameInputMenu());

        VBox layout = UIFactory.createMenuLayout(title, classicButton, extendedButton, backButton);
        UIFactory.setScene(mainStage, layout, "Wariant gry");
    }

    private void showDifficultyMenu() {
        Label title = UIFactory.createTitle("Wybierz poziom trudności:");
        Button easyButton = UIFactory.createMenuButton("Łatwy");
        Button normalButton = UIFactory.createMenuButton("Normalny");
        Button hardButton = UIFactory.createMenuButton("Trudny");
        Button backButton = UIFactory.createMenuButton("Powrót");

        TextField nameField = new TextField();
        nameField.setPromptText("Wpisz nazwę gracza");
        nameField.setMaxWidth(300);

        easyButton.setOnAction(e -> {
            playerXName = nameField.getText().isEmpty() ? "Gracz" : nameField.getText();
            difficultyLevel = "Łatwy";
            launchGame();
        });

        normalButton.setOnAction(e -> {
            playerXName = nameField.getText().isEmpty() ? "Gracz" : nameField.getText();
            difficultyLevel = "Normalny";
            launchGame();
        });

        hardButton.setOnAction(e -> {
            playerXName = nameField.getText().isEmpty() ? "Gracz" : nameField.getText();
            difficultyLevel = "Trudny";
            launchGame();
        });

        backButton.setOnAction(e -> showStartMenu());


        VBox layout = new VBox(20, title, nameField, easyButton, normalButton, hardButton, backButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        UIFactory.setScene(mainStage, layout, "Poziom trudności");
    }

    private void showRankingWindow() {
        RankingManager rankingManager = new RankingManager();
        List<GameResult> results = rankingManager.loadResults();
        results.sort((a, b) -> Integer.compare(b.getGamesWon(), a.getGamesWon()));

        Label title = UIFactory.createTitle("Ranking graczy");
        Button backButton = UIFactory.createMenuButton("Powrót");
        Button resetButton = UIFactory.createMenuButton("Resetuj ranking");

        backButton.setOnAction(e -> showStartMenu());
        resetButton.setOnAction(e -> {
            rankingManager.clearResults();
            showRankingWindow();
        });

        if (results.isEmpty()) {
            Label emptyLabel = new Label("Brak zapisanych wyników.");
            VBox layout = new VBox(20, title, emptyLabel, UIFactory.createButtonBox(resetButton, backButton));
            layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
            UIFactory.setScene(mainStage, layout, "Ranking");
            return;
        }

        TableView<GameResult> table = new TableView<>();
        table.setPrefWidth(500);

        TableColumn<GameResult, String> dateCol = new TableColumn<>("Data");
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getDate().format(DateTimeFormatter.ofPattern("dd-MM-yy"))
        ));

        TableColumn<GameResult, String> nameCol = new TableColumn<>("Gracz");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));

        TableColumn<GameResult, Integer> playedCol = new TableColumn<>("Rozegrane");
        playedCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getGamesPlayed()).asObject());

        TableColumn<GameResult, Integer> wonCol = new TableColumn<>("Wygrane");
        wonCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getGamesWon()).asObject());

        table.getColumns().addAll(dateCol, nameCol, playedCol, wonCol);
        table.getItems().addAll(results);

        VBox layout = new VBox(20, title, table, UIFactory.createButtonBox(resetButton, backButton));
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        UIFactory.setScene(mainStage, layout, "Ranking");
    }

    private void launchGame() {
        Label statusLabel = UIFactory.createStatusLabel("Ruch gracza: " + playerXName);
        scoreXLabel = UIFactory.createScoreLabel(playerXName, 0, "red");
        scoreOLabel = UIFactory.createScoreLabel(playerOName, 0, "blue");

        HBox scoreBox = UIFactory.createScoreBox(scoreXLabel, scoreOLabel);
        Button saveButton = UIFactory.createControlButton("Zapisz");
        Button resetButton = UIFactory.createControlButton("Restart");
        Button backButton = UIFactory.createControlButton("Powrót");

        StackPane gridWrapper = new StackPane();
        gameController = new GameController(boardSize, winLength, botEnabled, difficultyLevel, playerXName, playerOName, gridWrapper, statusLabel, scoreXLabel, scoreOLabel);
        gameController.setScoreUpdateCallback(gameController::updateScoreLabels);

        saveButton.setOnAction(e -> {
            try {
                File file = new File("saved_game.dat");
                GameState state = new GameState(
                        gameController.getGameBoard().getGridState(),
                        gameController.getCurrentPlayer(),
                        gameController.getScoreX(),
                        gameController.getScoreO(),
                        playerXName,
                        playerOName,
                        botEnabled,
                        difficultyLevel,
                        boardSize,
                        winLength
                );
                GameStateManager.saveGame(state, file);
                statusLabel.setText("Gra została zapisana.");
            } catch (IOException ex) {
                statusLabel.setText("Błąd zapisu gry.");
            }
        });
        resetButton.setOnAction(e -> gameController.resetGame());
        backButton.setOnAction(e -> showStartMenu());

        VBox layout = UIFactory.createGameLayout(
                statusLabel,
                scoreBox,
                gameController.getFullBoardView(),
                UIFactory.createButtonBox(resetButton, saveButton, backButton));
        UIFactory.setScene(mainStage, layout, "Tic-Tac-Toe");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

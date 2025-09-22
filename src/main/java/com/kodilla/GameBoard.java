package com.kodilla;

import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.function.BiConsumer;

public class GameBoard {
    private final GridPane grid;
    private final int boardSize;
    private final int cellSize;
    private final BiConsumer<Integer, Integer> onPlayerMove;

    public GameBoard(int boardSize, int cellSize, BiConsumer<Integer, Integer> onPlayerMove) {
        this.boardSize = boardSize;
        this.cellSize = cellSize;
        this.onPlayerMove = onPlayerMove;
        this.grid = new GridPane();
        grid.setStyle("-fx-alignment: center;");
        createBoard();
    }

    private void createBoard() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(cellSize, cellSize);
                cell.setStyle("-fx-border-color: #666666; -fx-border-width: 2;");
                Label symbol = new Label("");
                symbol.setStyle("-fx-font-size: " + (cellSize / 2) + "px;");
                cell.getChildren().add(symbol);

                final int r = row;
                final int c = col;
                cell.setOnMouseClicked(e -> {
                    if (symbol.getText().isEmpty()) {
                        onPlayerMove.accept(r, c);
                    }
                });

                grid.add(cell, col, row);
            }
        }
    }

    public GridPane getGrid() {
        return grid;
    }

    public StackPane getCell(int row, int col) {
        for (javafx.scene.Node node : grid.getChildren()) {
            Integer r = GridPane.getRowIndex(node);
            Integer c = GridPane.getColumnIndex(node);
            if (r != null && c != null && r == row && c == col) {
                return (StackPane) node;
            }
        }
        return null;
    }

    public void setSymbol(int row, int col, char player, String color, boolean bold) {
        StackPane cell = getCell(row, col);
        if (cell != null) {
            Label symbol = (Label) cell.getChildren().get(0);
            symbol.setText(player == '\0' ? "" : String.valueOf(player));
            String weight = bold ? "bold" : "normal";
            symbol.setStyle("-fx-font-size: " + (cellSize / 2) + "px; -fx-text-fill: " + color + "; -fx-font-weight: " + weight + ";");
            playClickAnimation(symbol);
        }
    }

    public void disableBoard() {
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

    public void resetBoard() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                setSymbol(row, col, '\0', "", false);
                getCell(row, col).setDisable(false);
            }
        }
    }
}

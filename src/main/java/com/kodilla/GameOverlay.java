package com.kodilla;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.util.List;

public class GameOverlay {
    private final Pane overlay;
    private final StackPane grid;

    public GameOverlay(StackPane grid) {
        this.grid = grid;
        this.overlay = new Pane();
        overlay.setMouseTransparent(true);
    }

    public Pane getOverlay() {
        return overlay;
    }

    public void drawWinningLine(List<int[]> line, GameBoard board) {
        if (line == null || line.isEmpty()) return;

        StackPane startCell = board.getCell(line.get(0)[0], line.get(0)[1]);
        StackPane endCell = board.getCell(line.get(line.size() - 1)[0], line.get(line.size() - 1)[1]);

        Bounds startBounds = startCell.localToScene(startCell.getBoundsInLocal());
        Bounds endBounds = endCell.localToScene(endCell.getBoundsInLocal());
        Bounds overlayBounds = overlay.localToScene(overlay.getBoundsInLocal());

        double offsetX = overlayBounds.getMinX();
        double offsetY = overlayBounds.getMinY();

        double startX = startBounds.getMinX() + startBounds.getWidth() / 2 - offsetX;
        double startY = startBounds.getMinY() + startBounds.getHeight() / 2 - offsetY;
        double endX = endBounds.getMinX() + endBounds.getWidth() / 2 - offsetX;
        double endY = endBounds.getMinY() + endBounds.getHeight() / 2 - offsetY;

        Line winLine = new Line(startX, startY, endX, endY);
        winLine.setStrokeWidth(3);
        winLine.setStroke(Color.web("#0077cc"));
        winLine.setStrokeLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
        winLine.setEffect(new DropShadow(5, Color.web("#0077cc")));

        double length = Math.hypot(endX - startX, endY - startY);
        winLine.getStrokeDashArray().addAll(length);
        winLine.setStrokeDashOffset(length);

        overlay.getChildren().add(winLine);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.6),
                        new KeyValue(winLine.strokeDashOffsetProperty(), 0))
        );
        timeline.play();
    }

    public void clearOverlay() {
        overlay.getChildren().clear();
    }
}

package com.kodilla;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class UIFactory {

    public static Label createTitle(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        return label;
    }

    public static Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(300, 60);
        button.setStyle("-fx-font-size: 18px;");
        return button;
    }

    public static Label createStatusLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 26px; -fx-text-fill: #0077cc; -fx-font-weight: bold;");
        return label;
    }

    public static Label createScoreLabel(String player, int score, String color) {
        Label label = new Label("Gracz " + player + ": " + score);
        label.setStyle("-fx-font-size: 18px; -fx-text-fill: " + color + "; -fx-font-weight: bold;");
        return label;
    }

    public static Button createControlButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(120, 40);
        button.setStyle("-fx-font-size: 16px;");
        return button;
    }

    public static VBox createMenuLayout(Label title, Button... buttons) {
        VBox layout = new VBox(30);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 50;");
        layout.getChildren().add(title);
        layout.getChildren().addAll(buttons);
        return layout;
    }

    public static HBox createScoreBox(Label... scores) {
        HBox box = new HBox(50);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-padding: 10;");
        box.getChildren().addAll(scores);
        return box;
    }

    public static HBox createButtonBox(Button... buttons) {
        HBox box = new HBox(20);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(buttons);
        return box;
    }

    public static void setScene(Stage stage, VBox layout, String title) {
        Scene scene = new Scene(layout, 600, 700);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}

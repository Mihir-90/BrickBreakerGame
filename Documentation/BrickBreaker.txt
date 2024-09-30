package com.example.game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class BrickBreaker extends Application {
    private Game game;

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        game = new Game(gc);

        // Create a layout for buttons and the pause label
        VBox buttonLayout = new VBox(10);
        buttonLayout.getChildren().addAll(game.getRetryButton(), game.getPauseButton());
        buttonLayout.setVisible(false); // Initially hidden
        buttonLayout.setAlignment(javafx.geometry.Pos.CENTER);

        StackPane root = new StackPane(canvas, buttonLayout); // Add button layout to root
        root.getChildren().add(game.getPauseLabel()); // Add pause label to the root
        Scene scene = new Scene(root);

        // Set up key event handling in the scene
        scene.setOnKeyPressed(event -> {
            game.handleKeyPress(event);
            if (game.isGameOver() || game.isGameWon()) {
                buttonLayout.setVisible(true); // Show buttons on game over or win
            } else {
                buttonLayout.setVisible(false); // Hide buttons otherwise
            }
        });

        primaryStage.setTitle("Brick Breaker");
        primaryStage.setScene(scene);
        primaryStage.show();

        game.startGameLoop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

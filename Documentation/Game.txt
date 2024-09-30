package com.example.game;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;

import java.net.URL;

public class Game {
    private GraphicsContext gc;
    private Paddle paddle;
    private Ball ball;
    private Brick[][] bricks;
    private int score;
    private int level;
    private boolean gameOver;
    private boolean gameWon;
    private Button retryButton;
    private Button pauseButton;
    private AudioClip backgroundMusic;
    private AudioClip hitSound;
    private boolean isPaused = false;
    private Label pauseLabel; // Label for displaying paused state

    public Game(GraphicsContext gc) {
        this.gc = gc;
        initializeGame();
        setupButtons();
        setupPauseLabel(); // Initialize the pause label
        loadBackgroundMusic(); // Load background music
        loadSounds(); // Load sound effects
    }

    private void setupButtons() {
        retryButton = new Button("Retry");
        retryButton.setOnAction(e -> resetGame());
        retryButton.setVisible(false); // Initially hidden

        pauseButton = new Button("Pause");
        pauseButton.setOnAction(e -> togglePause());
    }


    public Button getRetryButton() {
        return retryButton; // Getter for retry button
    }

    public Button getPauseButton() {
        return pauseButton; // Getter for pause button
    }

    public Label getPauseLabel() {
        return pauseLabel; // Getter for pause label
    }

    public boolean isGameOver() {
        return gameOver; // Getter for game over state
    }

    public boolean isGameWon() {
        return gameWon; // Getter for game won state
    }

    private void setupPauseLabel() {
        pauseLabel = new Label("Paused");
        pauseLabel.setStyle("-fx-font-size: 20; -fx-text-fill: red;"); // Style for the label
        pauseLabel.setVisible(false); // Initially hidden
    }

    private void loadBackgroundMusic() {
        URL musicFile = getClass().getResource("/background.mp3");
        if (musicFile == null) {
            throw new RuntimeException("Background music file not found.");
        }
        backgroundMusic = new AudioClip(musicFile.toString());
        backgroundMusic.setCycleCount(AudioClip.INDEFINITE); // Loop the music
        backgroundMusic.setVolume(0.3); // Set volume to 30%
        backgroundMusic.play();
    }

    private void initializeGame() {
        paddle = new Paddle();
        ball = new Ball();
        bricks = createBricks(level);
        score = 0;
        gameOver = false;
        gameWon = false;
    }

    public void startGameLoop() {
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameOver && !gameWon && !isPaused) {
                    updateGame();
                    render();
                }
            }
        };
        gameLoop.start();
    }

    private void updateGame() {
        ball.update();
        checkCollisions();

        if (ball.getY() > 600) {
            gameOver = true;
            retryButton.setVisible(true);
            backgroundMusic.stop(); // Stop music on game over
        }

        if (allBricksBroken()) {
            gameWon = true;
            level++; // Increment level
            if (level <= 3) {
                initializeGame(); // Start next level
            } else {
                retryButton.setVisible(true);
                backgroundMusic.stop(); // Stop music when all levels cleared
            }
        }
    }

    private void render() {
        gc.clearRect(0, 0, 800, 600);
        paddle.draw(gc);
        ball.draw(gc);
        drawBricks();
        drawScore();

        if (gameOver) {
            drawGameOver();
        } else if (gameWon) {
            drawVictory(); // Show victory message when all bricks are broken
        }

        if (isPaused) {
            drawPauseLabel(); // Draw the pause label when paused
        }
    }

    private void drawBricks() {
        for (Brick[] row : bricks) {
            for (Brick brick : row) {
                if (brick.isVisible()) {
                    brick.draw(gc);
                }
            }
        }
    }

    private void drawScore() {
        gc.setFill(Color.BLACK);
        gc.fillText("Score: " + score, 10, 20);
    }

    private void loadSounds() {
        URL hitSoundFile = getClass().getResource("/hit.mp3");
        if (hitSoundFile == null) {
            throw new RuntimeException("Hit sound file not found.");
        }
        hitSound = new AudioClip(hitSoundFile.toString());
    }

    private void drawGameOver() {
        gc.setFill(Color.RED);
        gc.fillText("Game Over!", 370, 250);
        gc.setFill(Color.BLACK);
        gc.fillText("Press 'R' to Retry", 360, 350);
    }


    private void drawVictory() {
        gc.setFill(Color.GREEN);
        gc.fillText("You Win Level " + level + "!", 350, 300);
        gc.setFill(Color.BLACK);
        gc.fillText("Press 'R' for next level", 340, 330);
    }

    public void handleKeyPress(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == KeyCode.LEFT) {
            paddle.moveLeft();
        } else if (event.getCode() == KeyCode.RIGHT) {
            paddle.moveRight();
        } else if (event.getCode() == KeyCode.R) {
            resetGame();
        } else if (event.getCode() == KeyCode.P) { // Toggle pause
            togglePause();
        }
    }

    private void resetGame() {
        if (gameWon) {
            gameWon = false; // Reset game won state
        }
        initializeGame();
        retryButton.setVisible(false);
        pauseLabel.setVisible(false); // Hide pause label when resetting
        if (!isPaused) {
            backgroundMusic.play(); // Restart music if not paused
        }
    }

    private Brick[][] createBricks(int level) {
        int rows = 5;
        int cols = 10;
        Brick[][] bricks = new Brick[rows][cols];
        double brickWidth = 80;
        double brickHeight = 20;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double x = col * brickWidth + 10; // Margin
                double y = row * brickHeight + 30; // Margin
                bricks[row][col] = new Brick(x, y);
            }
        }
        return bricks;
    }

    private void checkCollisions() {
        // Check collision with paddle
        if (ball.getY() + ball.getRadius() >= paddle.getY() &&
                ball.getX() + ball.getRadius() >= paddle.getX() &&
                ball.getX() - ball.getRadius() <= paddle.getX() + paddle.getWidth()) {
            ball.bounce();
            hitSound.play(); // Play sound on paddle collision
        }

        // Check collision with bricks
        for (Brick[] row : bricks) {
            for (Brick brick : row) {
                if (brick.isVisible() && ballCollisionWithBrick(brick)) {
                    brick.breakBrick();
                    ball.bounce();
                    score += 10; // Increment score
                    hitSound.play(); // Play sound on brick break
                }
            }
        }
    }

    private boolean ballCollisionWithBrick(Brick brick) {
        return ball.getX() + ball.getRadius() >= brick.getX() &&
                ball.getX() - ball.getRadius() <= brick.getX() + brick.getWidth() &&
                ball.getY() + ball.getRadius() >= brick.getY() &&
                ball.getY() - ball.getRadius() <= brick.getY() + brick.getHeight();
    }

    private boolean allBricksBroken() {
        for (Brick[] row : bricks) {
            for (Brick brick : row) {
                if (brick.isVisible()) {
                    return false; // At least one brick is still visible
                }
            }
        }
        return true; // All bricks are broken
    }

    private void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            backgroundMusic.stop(); // Stop the music when paused
            pauseLabel.setVisible(true); // Show paused label
            System.out.println("Game Paused");
        } else {
            backgroundMusic.play(); // Resume the music when unpaused
            pauseLabel.setVisible(false); // Hide paused label
            System.out.println("Game Resumed");
        }
    }

    private void drawPauseLabel() {
        gc.setFill(Color.RED);
        gc.fillText("Paused", 680, 30); // Draw the paused text at the top right
    }
}

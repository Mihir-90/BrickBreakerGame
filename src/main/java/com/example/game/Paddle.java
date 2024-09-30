package com.example.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Paddle {
    private double x; // X position of the paddle
    private double y; // Y position of the paddle
    private final double width = 100; // Width of the paddle
    private final double height = 20; // Height of the paddle
    private final double speed = 10; // Speed of the paddle movement

    public Paddle() {
        this.x = 400; // Centered initially
        this.y = 550; // Positioned near the bottom of the screen
    }

    public void moveLeft() {
        if (x > 0) { // Check if the paddle is within the left boundary
            x -= speed; // Move left
        }
    }

    public void moveRight() {
        if (x < 800 - width) { // Check if the paddle is within the right boundary
            x += speed; // Move right
        }
    }

    public double getX() {
        return x; // Return the current X position
    }

    public double getY() {
        return y; // Return the current Y position
    }

    public double getWidth() {
        return width; // Return the paddle width
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLUE); // Set the paddle color
        gc.fillRect(x, y, width, height); // Draw the paddle
    }
}

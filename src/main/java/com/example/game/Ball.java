package com.example.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball {
    private double x, y, radius, dx, dy;

    public Ball() {
        this.x = 400; // Initial horizontal position
        this.y = 300; // Initial vertical position
        this.radius = 10; // Radius of the ball
        this.dx = 3; // Initial horizontal speed
        this.dy = -3; // Initial vertical speed
    }

    public void update() {
        x += dx;
        y += dy;

        // Check for wall collisions (left and right)
        if (x <= 0 || x >= 800 - radius * 2) {
            dx = -dx; // Reverse horizontal direction
        }

        // Check for wall collisions (top)
        if (y <= 0) {
            dy = -dy; // Reverse vertical direction
        }
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillOval(x, y, radius * 2, radius * 2);
    }

    public void bounce() {
        dy = -dy; // Reverse vertical direction
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }
}

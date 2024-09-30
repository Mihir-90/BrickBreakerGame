package com.example.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Brick {
    private double x, y, width, height;
    private boolean visible;

    public Brick(double x, double y) {
        this.x = x;
        this.y = y;
        this.width = 80;
        this.height = 20;
        this.visible = true; // Brick starts as visible
    }

    public void draw(GraphicsContext gc) {
        if (visible) {
            gc.setFill(Color.GREEN);
            gc.fillRect(x, y, width, height);
        }
    }

    public void breakBrick() {
        visible = false; // Set brick to invisible
    }

    public boolean isVisible() {
        return visible;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
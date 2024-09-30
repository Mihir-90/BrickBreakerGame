package com.example.game;

import javafx.scene.media.AudioClip;

public class SoundManager {
    private AudioClip hitSound; // Sound for hitting the paddle or brick
    private AudioClip breakSound; // Sound for breaking a brick
    private AudioClip backgroundMusic; // Background music for the game

    public SoundManager() {
        // Load sound files from resources
        hitSound = new AudioClip(getClass().getResource("hit.wav").toString());
        breakSound = new AudioClip(getClass().getResource("break.wav").toString());
        loadBackgroundMusic(); // Load background music
    }

    private void loadBackgroundMusic() {
        // Load background music and set its properties
        backgroundMusic = new AudioClip(getClass().getResource("background.mp3").toString());
        backgroundMusic.setCycleCount(AudioClip.INDEFINITE); // Loop indefinitely
        backgroundMusic.setVolume(0.3); // Set default volume
    }

    public void playHitSound() {
        hitSound.play(); // Play hit sound
    }

    public void playBreakSound() {
        breakSound.play(); // Play break sound
    }

    public void playBackgroundMusic() {
        backgroundMusic.play(); // Play background music
    }

    public void stopBackgroundMusic() {
        backgroundMusic.stop(); // Stop background music
    }

    public void setVolume(double volume) {
        // Set volume for all sounds
        hitSound.setVolume(volume);
        breakSound.setVolume(volume);
        backgroundMusic.setVolume(volume);
    }

    // Additional methods can be added as needed
}

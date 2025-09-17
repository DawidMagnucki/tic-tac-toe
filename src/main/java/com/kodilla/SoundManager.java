package com.kodilla;

import javafx.scene.media.AudioClip;

public class SoundManager {
    private final AudioClip clickSound;
    private final AudioClip winSound;
    private final AudioClip drawSound;

    public SoundManager() {
        clickSound = new AudioClip(getClass().getResource("/click.mp3").toString());
        winSound = new AudioClip(getClass().getResource("/win.mp3").toString());
        drawSound = new AudioClip(getClass().getResource("/draw.mp3").toString());
    }

    public void playClick() {
        clickSound.play();
    }

    public void playWin() {
        winSound.play();
    }

    public void playDraw() {
        drawSound.play();
    }
}

package com.example.cincuentazo.utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class TimerLabel extends HBox {

    private Label titleLabel;
    private Label timeLabel;
    private Timeline timeline;
    private int secondsElapsed = 0;

    public TimerLabel(String title) {
        // Label  title
        titleLabel = new Label(title + " ");
        titleLabel.setFont(Font.font("Arial", 14));
        titleLabel.setStyle("-fx-text-fill: #2196F3; -fx-font-weight: bold;");

        // Label of time
        timeLabel = new Label(formatTime(secondsElapsed));
        timeLabel.setFont(Font.font("Consolas", 20));
        timeLabel.setStyle(
                "-fx-text-fill: #FF4500; " +
                        "-fx-background-color: #FFFF99; " +
                        "-fx-padding: 5px 10px; " +
                        "-fx-border-color: #333333; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px;"
        );

        // Agregar ambos labels en una sola línea (HBox)
        this.getChildren().addAll(titleLabel, timeLabel);
        this.setSpacing(8); // Espacio entre el título y el tiempo
    }

    /** Inicia el contador */
    public void start() {
        if (timeline != null) timeline.stop();

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsElapsed++;
            timeLabel.setText(formatTime(secondsElapsed));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /** Detiene el contador */
    public void stop() {
        if (timeline != null) timeline.stop();
    }

    /** Reinicia el contador */
    public void resetTime() {
        stop();
        secondsElapsed = 0;
        timeLabel.setText(formatTime(secondsElapsed));
    }

    /** Formatea el tiempo en MM:SS */
    private String formatTime(int seconds) {
        int min = seconds / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d", min, sec);
    }

    /** Retorna los segundos transcurridos */
    public int getSecondsElapsed() {
        return secondsElapsed;
    }
}
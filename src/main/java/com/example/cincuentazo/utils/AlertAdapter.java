package com.example.cincuentazo.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Abstract adapter class that implements {@link GameAlertHandler}.
 * Defines empty methods for game alerts
 * and provides default implementations for alerts.
 * @author Victoria Yuan
 * @version 1.0
 */
abstract class AlertAdapter implements GameAlertHandler {

    // ==== Game Events (overwrite as needed) ====

    /** Runs when the game starts. */
    @Override public void onStart() {}
    /** Runs when the game is paused. */
    @Override public void onPause() {}
    /** Runs when the game resumes. */
    @Override public void onResume() {}
    /** Runs when the game ends. */
    @Override public void onEnd() {}
    /**
     * By default, it displays an error alert with the received message.
     * @param errorMessage the error message that will be displayed in the alert.
     */
    @Override public void onError(String errorMessage) {
        showError("Error", errorMessage);
    }

    // ==== Default implementations of alert methods ====
    /**
     * Displays an information box with the specified title and message.
     * @param title the title of the alert
     * @param message the message displayed to the user
     */
    @Override
    public void showInfo(String title, String message) {
        showAlert(Alert.AlertType.INFORMATION, title, message);
    }
    /**
     * Displays an error box with the specified title and message.
     * @param title the title of the alert
     * @param message the message displayed to the user
     */
    @Override
    public void showError(String title, String message) {
        showAlert(Alert.AlertType.ERROR, title, message);
    }
    /**
     * Displays a warning box with the specified title and message.
     * @param title the title of the alert
     * @param message the message displayed to the user
     */
    @Override
    public void showWarning(String title, String message) {
        showAlert(Alert.AlertType.WARNING, title, message);
    }
    /**
     * Displays a confirmation box and returns {@code true} if the user selects "Aceptar".
     * @param title the alert title
     * @param message the message shown to the user
     * @return {@code true} if the user presses "Aceptar"; {@code false} otherwise
     */
    @Override
    public boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    // ==== Auxiliary internal method ====
    /**
     * Displays a generic alert of the specified type.
     * @param type the alert type (INFORMATION, ERROR, WARNING, etc.)
     * @param title the alert title
     * @param message the message content
     */
    public void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    /**
     * Placeholder method to confirm starting a new game.
     * Currently returns {@code false}, subclasses can override it.
     * @return {@code true} if the user confirms starting a new game, otherwise {@code false}
     */
    boolean showConfirmationNewGame() {
        return false;
    }
}

/**
 * Interface that defines the alert events and methods that an alert handler must implement.
 * Separates the game event logic (start, pause, error, etc.)
 * from the logic that presents alerts to the user.
 * @author Victoria Yuan
 * @version 1.0
 */
interface GameAlertHandler {

    // ==== Game Cycle Events ====

    /** Runs when the game starts. */
    void onStart();
    /** Runs when the game is paused. */
    void onPause();
    /** Runs when the game resumes. */
    void onResume();
    /** Runs when the game ends. */
    void onEnd();
    /**
     * Runs when an error occurs in the game.
     * @param errorMessage the error message to display
     */
    void onError(String errorMessage);

    // ==== Generalized alert methods ====
    /**
     * Displays an information box.
     * @param title the title of the alert
     * @param message the message displayed to the user
     */
    void showInfo(String title, String message);
    /**
     * Displays an error box.
     * @param title the title of the alert
     * @param message the message displayed to the user
     */
    void showError(String title, String message);
    /**
     * Displays a warning box.
     * @param title the title of the alert
     * @param message the message displayed to the user
     */
    void showWarning(String title, String message);
    /**
     * Displays a confirmation box and returns the result.
     * @param title the alert title
     * @param message the message shown to the user
     * @return {@code true} if the user confirms; {@code false} if they cancel
     */
    boolean showConfirmation(String title, String message);
}

package com.example.cincuentazo.utils;
/**
 * Class that extends {@link AlertAdapter} to handle Sudoku game-specific alerts.
 * Provides custom messages related to game actions,
 * such as confirmation to start a new game.
 * This class acts as a concrete adapter that reuses the alert logic
 * defined in {@code AlertAdapter} and adapts it to the Sudoku text and needs.
 * @author Victoria Yuan
 * @version 1.0
 */
public class Alerts50Game extends AlertAdapter {
    /**
     * Displays a confirmation alert when the user attempts to start a new Sudoku game.
     * @return {@code true} if the user confirms the creation of a new game,
     * {@code false} if they cancel the action.
     */
    public boolean showConfirmationNewGame( ) {
        String title= "NUEVO JUEGO";
        String message= "¿QUIERES INICIAR \nUN NUEVO SUDOKU?\nRecuerda que los datos actuales se borrarán";
        return super.showConfirmation(title, message);
    }



}

package com.example.cincuentazo.models;

import com.example.cincuentazo.controllers.GameController;
import javafx.application.Platform;
import java.util.List;
import java.util.Random;

/**
 * Representa un hilo de ejecución dedicado a la lógica de un jugador máquina (bot).
 * Este hilo es responsable de calcular la mejor jugada, simular un tiempo de respuesta.
 * Y luego ejecuta las acciones en el hilo.
 */
public class MachinePlayerThread extends Thread {

    private final GameController gameController;
    private final int machinePlayerId; // Identificador para saber qué máquina juega
    private final List<Card> machineHand; // La mano que debe evaluar

    /**
     * Constructor para iniciar el hilo del jugador máquina.
     * @param controller El controlador del juego.
     * @param id identificador del jugador máquina.
     * @param hand Lista de cartas que tiene el jugador máquina.
     */
    public MachinePlayerThread(GameController controller, int id, List<Card> hand) {
        this.gameController = controller;
        this.machinePlayerId = id;
        this.machineHand = hand;
    }

    /**
     * Contiene las acciones que realizará el jugador máquina en su turno.
     * 1. Decisión: Llama a {@code findBestMove} para elegir la carta.
     * 2. Simulación de Pensamiento: Pausa el hilo (2-4 segundos).
     * 3. Ejecución de Jugada: Llama a {@code handleMachinePlayCard} en el hilo de JavaFX.
     * 4. Simulación de Robo: Pausa el hilo (2-4 segundos).
     * 5. Robo y Pase de Turno: Llama a {@code handleMachineDrawCard} en el hilo de JavaFX.
     */
    @Override
    public void run() {
        try {
            Card cartaAJugar = findBestMove(gameController.getCurrentSum(), machineHand);
            // El tiempo de espera debe ser entre 2 y 4 segundos
            int delayMs = new Random().nextInt(2001) + 2000; // 2000 a 4000 ms
            Thread.sleep(delayMs);
            // Si la carta es null, significa que la máquina es eliminada
            if (cartaAJugar == null) {
                Platform.runLater(() -> {
                    // Llama al método del controlador para manejar la eliminación
                    gameController.handleMachineElimination(machinePlayerId);
                });
                return;
            }
            Platform.runLater(() -> {
                // Llama al método del controlador que maneja la lógica de jugar
                gameController.handleMachinePlayCard(machinePlayerId, cartaAJugar);
            });
            // El tiempo de espera debe ser entre 2 y 4 segundos
            delayMs = new Random().nextInt(2001) + 2000; // 2000 a 4000 ms
            Thread.sleep(delayMs);
            // 4. Tomar carta y actualizar la GUI
            Platform.runLater(() -> {
                // Llama al método del controlador que maneja la lógica de tomar carta y pasar turno
                gameController.handleMachineDrawCard(machinePlayerId);
            });
        } catch (InterruptedException e) {
            System.err.println("El hilo del jugador máquina " + machinePlayerId + " fue interrumpido y detenido.");
            return;
        }
    }

    /**
     * Implementa la lógica de la IA simple para el bot.
     * Itera sobre la mano del bot y busca la mejor opción para jugar.
     * @param currentSum La suma actual de la mesa.
     * @param hand La mano actual de cartas del bot.
     * @return La carta seleccionada como mejor opción de juego.
     */
    private Card findBestMove(int currentSum, List<Card> hand) {
        Card bestCard = null;
        int bestNewSum = Integer.MIN_VALUE;

        for (Card card : hand) {
            // Usa la lógica de evaluación del juego
            int effect = gameController.getGame().evaluateCardEffect(card, currentSum);
            int newSum = currentSum + effect;
            if (newSum <= 50) {
                // Lógica simple: Escoge la carta que deje la suma más alta pero segura.
                if (newSum > bestNewSum) {
                    bestNewSum = newSum;
                    bestCard = card;
                }
            }
        }
        // Si es null, la máquina debe ser eliminada.
        return bestCard;
    }
}
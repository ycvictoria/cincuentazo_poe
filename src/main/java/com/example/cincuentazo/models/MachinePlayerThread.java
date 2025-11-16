package com.example.cincuentazo.models;

import com.example.cincuentazo.controllers.GameController;
import javafx.application.Platform;
import java.util.List;
import java.util.Random;

public class MachinePlayerThread extends Thread {

    private final GameController gameController;
    private final int machinePlayerId; // Identificador para saber qué máquina juega
    private final List<Card> machineHand; // La mano que debe evaluar

    // CONSTRUCTOR MODIFICADO: Ahora recibe la mano
    public MachinePlayerThread(GameController controller, int id, List<Card> hand) {
        this.gameController = controller;
        this.machinePlayerId = id;
        this.machineHand = hand;
    }

    /*@Override
    public void run() {
        try {
            // Lógica de DECISIÓN
            Card cartaAJugar = findBestMove(gameController.getCurrentSum(), machineHand);

            // 1. Tiempo de espera (2 a 4 segundos) para la TOMA DE DECISIÓN
            int delayMs = new Random().nextInt(2001) + 2000; // 2000 a 4000 ms
            Thread.sleep(delayMs);

            // 2. Ejecutar TODAS las acciones del turno en el hilo de JavaFX
            Platform.runLater(() -> {
                if (cartaAJugar == null) {
                    // Llama al método del controlador para manejar la eliminación
                    gameController.handleMachineElimination(machinePlayerId);
                } else {
                    // Acción A: Jugar la carta (actualiza suma, inicia animación)
                    gameController.handleMachinePlayCard(machinePlayerId, cartaAJugar);

                    // Acción B: Tomar carta y pasar turno.
                    // ¡IMPORTANTE! Si handleMachinePlayCard usa animaciones,
                    // handleMachineDrawCard DEBERÍA ser llamado en el callback de esa animación
                    // para garantizar que el turno no pase antes de que la animación termine.

                    // Si handleMachinePlayCard NO USA CALLBACKS para pasar el turno:
                    gameController.handleMachineDrawCard(machinePlayerId);
                }
            });

        } catch (InterruptedException e) {
            System.err.println("El hilo del jugador máquina " + machinePlayerId + " fue interrumpido y detenido.");
            return;
        }
    }*/

    @Override
    public void run() {
        try {
            // Lógica de DECISIÓN (Implementar en Game/Modelo)
            Card cartaAJugar = findBestMove(gameController.getCurrentSum(), machineHand);

            // 1. Lógica para SELECCIONAR CARTA
            // El tiempo de espera debe ser entre 2 y 4 segundos
            int delayMs = new Random().nextInt(2001) + 2000; // 2000 a 4000 ms
            Thread.sleep(delayMs);

            // Si la carta es null, significa que la máquina es eliminada [cite: 109]
            if (cartaAJugar == null) {
                Platform.runLater(() -> {
                    // Llama al método del controlador para manejar la eliminación
                    gameController.handleMachineElimination(machinePlayerId);
                });
                return; // Finaliza el hilo
            }

            // 2. Jugar la carta y actualizar la GUI
            // ¡IMPORTANTE! Las actualizaciones de la GUI deben ser en el hilo de JavaFX
            Platform.runLater(() -> {
                // Llama al método del controlador que maneja la lógica de jugar
                gameController.handleMachinePlayCard(machinePlayerId, cartaAJugar);
            });

            // 3. Lógica para TOMAR CARTA DEL MAZO
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
            //Thread.currentThread().interrupt();
            return; // Detiene la ejecución del hilo.
        }
    }

    /**
     * Lógica de la IA (Debería estar en el modelo, pero por simplicidad la colocamos aquí).
     * Itera sobre la mano para encontrar la mejor carta que no exceda 50.
     */
    private Card findBestMove(int currentSum, List<Card> hand) {
        Card bestCard = null;
        // Esto garantiza que CUALQUIER suma válida (incluyendo -20) sea mayor que este valor inicial.
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
        // Si es null, la máquina debe ser eliminada[cite: 109].
        return bestCard;
    }
}
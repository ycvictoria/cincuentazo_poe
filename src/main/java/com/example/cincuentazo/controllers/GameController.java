package com.example.cincuentazo.controllers;

import com.example.cincuentazo.models.*;
import com.example.cincuentazo.utils.TimerLabel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    @FXML private HBox playerCardsBox;      // Main player (abajo)
    @FXML private VBox playerM1CardsBox;    // izquierda
    @FXML private HBox playerM2CardsBox;    // arriba
    @FXML private VBox playerM3CardsBox;    // derecha

    @FXML private StackPane stackCardsBox;       // Carta visible en mesa
    @FXML private StackPane stackCardsLeftBox;   // Mazo (back)

    @FXML private Button btnAceptar;
    @FXML private Button btnHowToPlay;
    @FXML private Button btnNewGame;

    @FXML private Pane paneLabelTimer;
    @FXML private Label nickNameLabel;
    @FXML private Label nicknameBot1Label;
    @FXML private Label nicknameBot2Label;
    @FXML private Label nicknameBot3Label;
    @FXML private Label currentSumLabel;

    private TimerLabel timerLabel;
    private ImageView selectedCardView = null;

    private Deck deck;
    private List<Card> playerHand;
    private List<Card> m1Hand;
    private List<Card> m2Hand;
    private List<Card> m3Hand;

    private Game game;
    private int currentSum;
    private Card selectedCard;
    //private final int HUMAN_PLAYER_ID = 0; // El jugador humano siempre es el primero en la lista de getAllPlayers()
    private int currentTurnPlayerIndex = 0; // Para llevar el seguimiento de qué jugador tiene el turno

    @FXML
    public void initialize() {
        game = new Game();
        deck = game.getDeck();

        /*
        // Repartir 4 cartas a cada jugador
        playerHand = deck.dealCards(4);
        m1Hand = deck.dealCards(4);
        m2Hand = deck.dealCards(4);
        m3Hand = deck.dealCards(4);

        // Mostrar manos
        showCards(playerCardsBox, playerHand, true);
        showCards(playerM1CardsBox, m1Hand, false);
        showCards(playerM2CardsBox, m2Hand, false);
        showCards(playerM3CardsBox, m3Hand, false);
        */

        // Carta inicial en mesa + suma inicial correcta
        Card tableCard = deck.drawCard();
        showTableCard(tableCard);

        int initialValue = game.evaluateCardEffect(tableCard, 0);
        currentSum = initialValue;
        game.setActualSum(initialValue);
        if (currentSumLabel != null) {
            currentSumLabel.setText("Suma : " + currentSum);
        }

        btnAceptar.setCursor(Cursor.HAND);
        selectedCard = null;

        // Timer
        timerLabel = new TimerLabel("");
        if (paneLabelTimer != null) {
            paneLabelTimer.getChildren().add(timerLabel);
        }
        timerLabel.start();
    }

    private void showCards(Object container, List<Card> cards, boolean faceUp) {
        if (container instanceof Pane pane) pane.getChildren().clear();
        else if (container instanceof Group group) group.getChildren().clear();

        double overlap = -70;
        int index = 0;

        for (Card c : cards) {
            Image image = faceUp
                    ? new Image(getClass().getResourceAsStream(c.getImagePath()))
                    : new Image(getClass().getResourceAsStream("/com/example/cincuentazo/cards/back.png"));

            ImageView imgView = new ImageView(image);
            imgView.setFitWidth(80);
            imgView.setFitHeight(120);
            imgView.setPreserveRatio(true);

            if (faceUp) {
                imgView.setOnMouseClicked(e -> onCardClicked(c, imgView));
            }

            StackPane cardPane = new StackPane(imgView);
            double baseRotation = 0;

            if (container == playerM1CardsBox) baseRotation = 90;
            else if (container == playerM3CardsBox) baseRotation = -90;
            else if (container == playerM2CardsBox) baseRotation = 180;

            cardPane.setRotate(baseRotation);
            cardPane.setManaged(false);

            if (container instanceof HBox) cardPane.setTranslateX(index * overlap);
            else cardPane.setTranslateY(index * overlap);

            if (container instanceof Pane pane) pane.getChildren().add(cardPane);
            else if (container instanceof Group group) group.getChildren().add(cardPane);

            index++;
        }
    }

    private void showTableCard(Card card) {
        // Actualiza carta visible en mesa
        stackCardsBox.getChildren().clear();
        Image image = new Image(getClass().getResourceAsStream(card.getImagePath()));
        ImageView cardView = new ImageView(image);
        cardView.setFitWidth(90);
        cardView.setFitHeight(130);
        cardView.setPreserveRatio(true);
        stackCardsBox.getChildren().add(cardView);

        // Evita acumular backs del mazo a la izquierda
        stackCardsLeftBox.getChildren().clear();
        ImageView cardViewTable = new ImageView(
                new Image(getClass().getResourceAsStream("/com/example/cincuentazo/cards/back.png"))
        );
        cardViewTable.setFitWidth(90);
        cardViewTable.setFitHeight(130);
        cardViewTable.setPreserveRatio(true);
        stackCardsLeftBox.getChildren().add(cardViewTable);
    }

    private void onCardClicked(Card selectedCard, ImageView cardView) {
        // Quitar estilo de selección previo
        if (selectedCardView != null) {
            selectedCardView.setScaleX(1.0);
            selectedCardView.setScaleY(1.0);
            selectedCardView.setStyle("");
        }

        // Si clican la misma, deseleccionar
        if (selectedCardView == cardView) {
            selectedCardView.setScaleX(1.0);
            selectedCardView.setScaleY(1.0);
            selectedCardView = null;
            this.selectedCard = null;
            return;
        }

        // Nuevo estilo de selección
        cardView.setScaleX(1.1);
        cardView.setScaleY(1.1);
        cardView.setStyle(
                "-fx-effect: dropshadow(gaussian, yellow, 25, 0.5, 0, 0);" +
                        "-fx-border-color: yellow;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 5;"
        );

        selectedCardView = cardView;
        this.selectedCard = selectedCard;
    }

    public void setStartGame(Player player, int numMachinePlayer) {
        this.game.setPlayer(player);
        this.game.setNumMachinePlayer(numMachinePlayer);

        if (nickNameLabel != null) {
            if (nickNameLabel.getText().equals("")) {
                nickNameLabel.setText("Player");
            } else {
                nickNameLabel.setText(game.getPlayer().getNickName());
            }
        }

        // AÑADIR NUEVA LÓGICA DE REPARTO Y VISTA
        // 1. Repartir cartas a todos los jugadores creados (Humano + Máquinas)
        game.dealCards(4);

        // 2. Asignar las manos a las variables locales (solo para el humano)
        //    Las manos de las máquinas ya están en game.getMachinePlayers()
        playerHand = game.getPlayer().getHand();

        // 3. Mostrar manos de todos los jugadores creados
        showCards(playerCardsBox, playerHand, true);

        // Mostrar solo las máquinas que existen (numMachinePlayer)
        if (numMachinePlayer >= 1) {
            showCards(playerM1CardsBox, game.getMachinePlayers().get(0).getHand(), false);
        }
        if (numMachinePlayer >= 2) {
            showCards(playerM2CardsBox, game.getMachinePlayers().get(1).getHand(), false);
        }
        if (numMachinePlayer >= 3) {
            showCards(playerM3CardsBox, game.getMachinePlayers().get(2).getHand(), false);
        }

        System.out.println("Jugador: " + game.getPlayer().getNickName() + " num: " + numMachinePlayer);
        startNextTurn(); // 4. Iniciar el juego
    }

    @FXML
    void playCardAction(ActionEvent event) {
        // Juega la carta seleccionada respetando la regla de no pasar de 50
        if (selectedCard == null) return;

        int gameValue = game.evaluateCardEffect(selectedCard, currentSum);
        if (currentSum + gameValue > 50) {
            System.out.println("Jugada inválida: excede 50.");
            return;
        }

        // Actualizar suma y mesa
        currentSum += gameValue;
        game.setActualSum(currentSum);
        if (currentSumLabel != null) {
            currentSumLabel.setText("Suma : " + currentSum);
        }

        // Mover la carta seleccionada a la mesa (visual)
        showTableCard(selectedCard);

        // Remover de la mano del jugador
        playerHand.remove(selectedCard);

        // Robar una carta para mantener 4
        Card newCard = deck.drawCard();
        if (newCard != null) {
            playerHand.add(newCard);
        }

        // Refrescar mano del jugador humano
        showCards(playerCardsBox, playerHand, true);

        // Limpiar selección
        if (selectedCardView != null) {
            selectedCardView.setScaleX(1.0);
            selectedCardView.setScaleY(1.0);
            selectedCardView.setStyle("");
            selectedCardView = null;
        }
        selectedCard = null;

        // Añadir: Pasa el turno al siguiente jugador
        startNextTurn();
    }

    @FXML
    void howToPlayAction(ActionEvent event) {
        // TODO: Mostrar reglas (siguiente fase)
    }

    @FXML
    void newGameAction(ActionEvent event) {
        // TODO: Reiniciar partida (siguiente fase)
    }

    private void startNextTurn() {
        // 1. Avanza al siguiente jugador
        List<Player> allPlayers = game.getAllPlayers();
        currentTurnPlayerIndex = (currentTurnPlayerIndex + 1) % allPlayers.size();
        Player nextPlayer = allPlayers.get(currentTurnPlayerIndex);

        // 2. Determina si es humano o máquina
        if (nextPlayer.isHuman()) {
            System.out.println("Turno del Jugador Humano: " + nextPlayer.getNickName());

            // **NUEVA LÓGICA DE VERIFICACIÓN Y ELIMINACIÓN**
            if (!hasValidMove(playerHand, currentSum)) {
                // Eliminar al humano (HU-5)
                System.out.println("Jugador Humano eliminado: no tiene jugadas válidas.");
                handleHumanElimination(); // **Llamar al nuevo método de eliminación**
                return; // Detiene el flujo para que handleHumanElimination pase el turno
            }

            enableHumanControls(true);
        } else {
            System.out.println("Turno de la Máquina: " + nextPlayer.getNickName());
            enableHumanControls(false);
            // Encuentra el ID de la máquina (ej: 1, 2, 3) basado en la lista de máquinas
            int machineId = game.getMachinePlayers().indexOf(nextPlayer) + 1;
            startMachineTurn(machineId); // Inicia el hilo
        }
    }

    /**
     * Verifica si un jugador tiene alguna carta válida para jugar.
     * Retorna true si hay una carta, false si debe ser eliminado.
     */
    private boolean hasValidMove(List<Card> hand, int currentSum) {
        for (Card card : hand) {
            int effect = game.evaluateCardEffect(card, currentSum);
            if (currentSum + effect <= 50) {
                return true; // Se encontró al menos una jugada válida
            }
        }
        return false; // No hay jugadas válidas, debe ser eliminado
    }

    private void enableHumanControls(boolean enable) {
        // Implementa la lógica para habilitar/deshabilitar los botones y la interacción con las cartas
        btnAceptar.setDisable(!enable);
        // Podrías necesitar un loop para deshabilitar los eventos onMouseClicked de las cartas,
        // pero por ahora, deshabilitar el botón Aceptar es suficiente.
    }

    /**
     * Inicia el turno del jugador máquina.
     */
    public void startMachineTurn(int machineId) {
        enableHumanControls(false); // Deshabilita la interacción del humano

        // El hilo necesita la mano de la máquina para tomar decisiones
        List<Card> machineHand = game.getMachinePlayers().get(machineId - 1).getHand();

        // Pasa la mano de la máquina al hilo para que pueda decidir
        MachinePlayerThread machineThread = new MachinePlayerThread(this, machineId, machineHand);
        machineThread.start();
    }

    /**
     * Método llamado por el hilo para jugar la carta y actualizar la Vista.
     * DEBE ser llamado dentro de Platform.runLater().
     */
    public void handleMachinePlayCard(int machineId, Card card) {
        // 1. Actualiza el Modelo (simula playCardAction, pero para la máquina)
        int gameValue = game.evaluateCardEffect(card, currentSum);
        currentSum += gameValue;
        game.setActualSum(currentSum);
        currentSumLabel.setText("Suma : " + currentSum);

        // 2. Mover la carta seleccionada a la mesa (visual)
        showTableCard(card);

        // 3. Remover de la mano del jugador Máquina (en el Modelo)
        game.getMachinePlayers().get(machineId - 1).removeCard(card);

        // 4. Refrescar la mano de la máquina (visual, mostrando una carta menos)
        // CAMBIO 1: Cambiar VBox a Object para que el switch funcione correctamente con HBox
        Object containerM = switch (machineId) {
            case 1 -> playerM1CardsBox;
            case 2 -> playerM2CardsBox; // ⬅️ CORRECCIÓN: Asigna el HBox
            case 3 -> playerM3CardsBox;
            default -> throw new IllegalStateException("ID de máquina inválido: " + machineId);
        };

        // CAMBIO 2: Verificar que sea un Pane (HBox o VBox lo son)
        if (containerM instanceof Pane) {
            showCards(containerM, game.getMachinePlayers().get(machineId - 1).getHand(), false);
        }
    }

    /**
     * Método llamado por el hilo para tomar carta y pasar el turno (en Platform.runLater).
     */
    public void handleMachineDrawCard(int machineId) {
        // 1. Actualiza el Modelo (toma una carta del mazo)
        Card newCard = deck.drawCard();
        if (newCard != null) {
            game.getMachinePlayers().get(machineId - 1).addCard(newCard);
        }

        // 2. Actualiza la Vista (mostrar la nueva carta en la mano, si es necesario)
        // Refrescar mano de la máquina (mostrar 4 cartas de nuevo)
        Object container = switch (machineId) {
            case 1 -> playerM1CardsBox;
            case 2 -> playerM2CardsBox;
            case 3 -> playerM3CardsBox;
            default -> throw new IllegalStateException("ID de máquina inválido: " + machineId);
        };

        if (container instanceof Pane) {
            showCards(container, game.getMachinePlayers().get(machineId - 1).getHand(), false);
        }

        // 3. Pasa el turno al siguiente jugador (humano o máquina)
        startNextTurn();
    }

    public int getCurrentSum() {
        return currentSum;
    }

    public Game getGame() {
        return game;
    }

    /**
     * Maneja la lógica de la eliminación de un jugador máquina.
     * * Reglas: El jugador eliminado devuelve sus cartas al mazo, y se verifica el fin del juego.
     * @param machineId ID de la máquina a eliminar (1, 2 o 3).
     */
    public void handleMachineElimination(int machineId) {
        // 1. Obtener el jugador máquina y sus cartas
        Player eliminatedPlayer = game.getMachinePlayers().get(machineId - 1);
        List<Card> hand = new ArrayList<>(eliminatedPlayer.getHand()); // Copia para evitar ConcurrentModification

        // 2. Devolver las cartas al mazo y remover al jugador del Modelo
        game.returnCardsToDeck(hand); // Necesita que implementes este método en Game.java
        game.getMachinePlayers().remove(eliminatedPlayer);

        // 3. Actualizar la GUI
        Object container = switch (machineId) {
            case 1 -> playerM1CardsBox;
            case 2 -> playerM2CardsBox;
            case 3 -> playerM3CardsBox;
            default -> null;
        };
        if (container instanceof Pane) {
            // Limpia el contenedor de cartas y lo oculta
            ((Pane) container).getChildren().clear();
            ((Pane) container).setVisible(false);
        }

        Label nicknameLabel = switch (machineId) {
            case 1 -> nicknameBot1Label;
            case 2 -> nicknameBot2Label;
            case 3 -> nicknameBot3Label;
            default -> null;
        };
        if (nicknameLabel != null) nicknameLabel.setText("ELIMINADO");

        // 4. Verificar fin del juego (Solo queda un jugador o menos)
        if (game.getAllPlayers().size() <= 1) {
            // Aquí va la lógica de HU-6 (Fin del juego)

            String winner;
            if (game.getAllPlayers().isEmpty()) {
                // No debería ocurrir.
                winner = "¡ERROR! Nadie quedó.";

                // Si la lista está vacía, es un error o un empate.
                System.err.println("¡Error de conteo! La lista de jugadores quedó vacía.");
                winner = "Empate";
            } else {
                // El ganador es el único jugador restante (humano o máquina)
                winner = game.getAllPlayers().get(0).getNickName();
            }

            showGameEndAlert(winner); // LLAMADA A LA ALERTA
        } else {
            // 5. Pasar al siguiente turno
            // Asegurarse que el índice no exceda el nuevo tamaño de la lista
            List<Player> allPlayers = game.getAllPlayers();
            if (currentTurnPlayerIndex >= allPlayers.size()) {
                currentTurnPlayerIndex = 0; // Vuelve al inicio si el índice se desbordó
            }

            // Llama a startNextTurn()
            startNextTurn();
        }
    }

    /**
     * Maneja la lógica de la eliminación del jugador humano.
     */
    public void handleHumanElimination() {
        // 1. Devolver cartas al mazo y remover al jugador del Modelo
        game.returnCardsToDeck(playerHand);
        game.setPlayer(null); // Quitar al jugador humano del modelo principal

        // 2. Actualizar la GUI del humano
        playerCardsBox.getChildren().clear();
        nickNameLabel.setText("ELIMINADO");
        enableHumanControls(false); // Deshabilitar botones

        // 3. Verificar fin del juego
        if (game.getAllPlayers().size() <= 1) {
            // Lógica de (Fin del juego)
            String winner;
            if (game.getAllPlayers().isEmpty()) {
                // No debería ocurrir.
                winner = "¡ERROR! Nadie quedó."; // Dejamos esto solo para traza de error

                // Si la lista está vacía, es un error o un empate.
                System.err.println("¡Error de conteo! La lista de jugadores quedó vacía.");
                winner = "Empate";
            } else {
                // El ganador es el único jugador restante (la máquina)
                winner = game.getAllPlayers().get(0).getNickName();
            }

            System.out.println("¡FIN DEL JUEGO! Ganador: " + winner);
            showGameEndAlert(winner); // LLAMADA A LA ALERTA
        } else {
            // 4. Pasar al siguiente turno (saltando la posición actual)
            // Como el humano fue removido, el currentTurnPlayerIndex debe ajustarse.

            // Simplemente llama a startNextTurn, que recalculará el índice
            // y pasará a la máquina.
            startNextTurn();
        }
    }

    private void showGameEndAlert(String winner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("¡Fin del Juego!");
        timerLabel.stop(); // Detener el temporizador al final

        if (winner.equals("NADIE")) {
            alert.setHeaderText("¡Juego Terminado!");
            alert.setContentText("Parece que todos los jugadores han sido eliminados. ¡Nadie gana!");
        } else {
            alert.setHeaderText("¡FELICITACIONES!");
            alert.setContentText("¡El ganador es: " + winner + "!");
        }

        alert.showAndWait();
    }
}
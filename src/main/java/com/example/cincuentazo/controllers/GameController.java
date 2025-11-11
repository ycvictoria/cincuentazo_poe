package com.example.cincuentazo.controllers;

import com.example.cincuentazo.models.Card;
import com.example.cincuentazo.models.Deck;
import com.example.cincuentazo.models.Game;
import com.example.cincuentazo.models.Player;
import com.example.cincuentazo.utils.TimerLabel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.List;

public class GameController {

    @FXML private HBox playerCardsBox;      // Main player (abajo)
    @FXML private VBox playerM1CardsBox;    // izquierda
    @FXML private HBox playerM2CardsBox;    // arriba
    @FXML private VBox playerM3CardsBox;    // derecha

    @FXML private StackPane stackCardsBox;      // Carta visible en mesa
    @FXML private StackPane stackCardsLeftBox;  // Mazo (back)

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

    @FXML
    public void initialize() {
        game = new Game();
        deck = game.getDeck();

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

        // Carta inicial en mesa + suma inicial correcta
        Card tableCard = deck.drawCard();
        showTableCard(tableCard);

        int initialValue = game.evaluateCardEffect(tableCard, 0);
        currentSum = initialValue;
        game.setActualSum(initialValue);
        currentSumLabel.setText("Suma : " + currentSum);

        btnAceptar.setCursor(Cursor.HAND);

        selectedCard = null;

        // Timer
        timerLabel = new TimerLabel("");
        paneLabelTimer.getChildren().add(timerLabel);
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
        System.out.println("Jugador: " + game.getPlayer().getNickName() + " num: " + numMachinePlayer);
    }

    @FXML
    void playCardAction(ActionEvent event) {
        // Juega la carta seleccionada respetando la regla de no pasar de 50
        if (selectedCard == null) return;

        int gameValue = game.evaluateCardEffect(selectedCard, currentSum);
        if (currentSum + gameValue > 50) {
            // Jugada inválida: simplemente no la permitimos (versión mínima)
            // (más adelante podemos mostrar alerta con Alerts50Game)
            System.out.println("Jugada inválida: excede 50.");
            return;
        }

        // Actualizar suma y mesa
        currentSum += gameValue;
        game.setActualSum(currentSum);
        currentSumLabel.setText("Suma : " + currentSum);

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

        // (Próximas fases: pasar turno a bots, verificar eliminación, etc.)
    }

    @FXML
    void howToPlayAction(ActionEvent event) {
        // TODO: Mostrar reglas/jugador (en otra fase)
    }

    @FXML
    void newGameAction(ActionEvent event) {
        // TODO: Reiniciar partida (en otra fase)
    }
}

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

    /*@FXML private HBox playerCardsBox;    // abajo (jugador humano)
    @FXML private VBox playerM1CardsBox; // izquierda
    @FXML private VBox playerM2CardsBox;  // arriba
    @FXML private Group playerM3CardsBox;*/ // derecha
    @FXML
    private HBox playerCardsBox;      // Main player

    @FXML
    private VBox playerM1CardsBox;    // left player <-

    @FXML
    private HBox playerM2CardsBox;    // up player

    @FXML
    private VBox playerM3CardsBox;    // right player ->

    @FXML
    private StackPane stackCardsBox;  // Mazo central

    @FXML
    private Button btnAceptar;        // Botón debajo del jugador

    @FXML
    private StackPane stackCardsLeftBox;  //Cards left box

    @FXML
    private Button btnHowToPlay;
    @FXML
    private Button btnNewGame;
    @FXML
    private Pane paneLabelTimer;
    @FXML
    private Label nickNameLabel;
    @FXML
    private Label nicknameBot1Label;

    @FXML
    private Label nicknameBot2Label;

    @FXML
    private Label nicknameBot3Label;
    @FXML
    private Label currentSumLabel;
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
        deck= game.getDeck();

        // Repartir 4 cartas a cada jugador
        playerHand = deck.dealCards(4);
        m1Hand = deck.dealCards(4);
        m2Hand = deck.dealCards(4);
        m3Hand = deck.dealCards(4);


        // Mostrar cartas
        showCards(playerCardsBox, playerHand, true);
        showCards(playerM1CardsBox, m1Hand, false);
        showCards(playerM2CardsBox, m2Hand, false);
        showCards(playerM3CardsBox, m3Hand, false);


        // Mostrar una carta inicial en la mesa
        Card tableCard = deck.drawCard();
        showTableCard(tableCard);
        btnAceptar.setCursor(Cursor.HAND);

        currentSum = 0;
        selectedCard=null;
        timerLabel= new TimerLabel("");
        paneLabelTimer.getChildren().add( timerLabel);
        timerLabel.start();
    }

    private void showCards(Object container, List<Card> cards, boolean faceUp) {
        if (container instanceof Pane pane) pane.getChildren().clear();
        else if (container instanceof Group group) group.getChildren().clear();

        double overlap = -70;
        int index = 0;
        int total = cards.size();

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

            if (container instanceof HBox) {
                cardPane.setTranslateX(index * overlap);
            } else {
                cardPane.setTranslateY(index * overlap);
            }

            if (container instanceof Pane pane) pane.getChildren().add(cardPane);
            else if (container instanceof Group group) group.getChildren().add(cardPane);

            index++;
        }
    }


    private void showTableCard(Card card) {
        stackCardsBox.getChildren().clear();
        Image image = new Image(getClass().getResourceAsStream(card.getImagePath()));
        ImageView cardView = new ImageView(image);
        cardView.setFitWidth(90);
        cardView.setFitHeight(130);
        cardView.setPreserveRatio(true);
        stackCardsBox.getChildren().add(cardView);
        ImageView cardViewTable = new ImageView(new Image(getClass().getResourceAsStream("/com/example/cincuentazo/cards/back.png")));
        cardViewTable.setFitWidth(90);
        cardViewTable.setFitHeight(130);
        cardViewTable.setPreserveRatio(true);
        stackCardsLeftBox.getChildren().add(cardViewTable);
    }


    private void onCardClicked(Card selectedCard, ImageView cardView) {
        System.out.println("You clicked: " + selectedCard.getName() +
                " (value " + selectedCard.getValueNumeric() + ")");

        // Si había una carta seleccionada antes, le quitamos el borde
        if (selectedCardView != null) {
            selectedCardView.setScaleX(1.0);
            selectedCardView.setScaleY(1.0);
            selectedCardView.setStyle(""); // limpia el estilo anterior
        }

        // Si el usuario vuelve a hacer clic en la misma carta, la deselecciona
        if (selectedCardView == cardView) {
            selectedCardView.setScaleX(1.0);
            selectedCardView.setScaleY(1.0);
            selectedCardView = null;
            return;
        }

        cardView.setScaleX(1.1);
        cardView.setScaleY(1.1);
        cardView.setStyle(
                "-fx-effect: dropshadow(gaussian, yellow, 25, 0.5, 0, 0);" +
                        "-fx-border-color: yellow;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 5;"
        );
        // Guardar esta carta como la actualmente seleccionada
        selectedCardView = cardView;
        this.selectedCard = selectedCard;

    }

    public void setStartGame(Player player, int numMachinePlayer) {
        this.game.setPlayer(player);
        this.game.setNumMachinePlayer(numMachinePlayer);
        if (nickNameLabel != null) {
            if(nickNameLabel.getText().equals("")){
                nickNameLabel.setText("Player");
            }else{
            nickNameLabel.setText(game.getPlayer().getNickName());}
        }
        System.out.println("Jugador: "+game.getPlayer().getNickName()+ " num: " +numMachinePlayer);
    }



    @FXML
    void playCardAction(ActionEvent event) {
        //Modificar este metodo para que muestre el valor de la suma actual en el label
        if(selectedCard!=null){
        currentSum+= selectedCard.getValueNumeric();
        currentSumLabel.setText("Suma : "+String.valueOf(currentSum));  }
    }
    @FXML
    void howToPlayAction(ActionEvent event) {

    }

    @FXML
    void newGameAction(ActionEvent event) {

    }


}

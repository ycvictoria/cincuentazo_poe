package com.example.cincuentazo.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase que representa las 52 cartas del mazo.
 */
public class Deck {
    private final List<Card> cards;
    private int currentIndex = 0;

    /**
     * Constructor que inicia el mazo de las 52 cartas.
     * Asigna el valor numérico a cada carta.
     */
    public Deck() {
        cards = new ArrayList<>();
        String[] suits = {"C", "D", "H", "S"}; // Clubs, Diamonds, Hearts, Spades
        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "0", "J", "Q", "K"}; // 0 representa 10

        for (String suit : suits) {
            for (String rank : ranks) {
                int value;
                switch (rank) {
                    case "A" -> value = 1;
                    case "0" -> value = 10;
                    case "J" -> value = 11;
                    case "Q" -> value = 12;
                    case "K" -> value = 13;
                    default -> value = Integer.parseInt(rank);
                }

                String path = "/com/example/cincuentazo/cards/" + rank + suit + ".png";
                System.out.println("path:" + path);
                cards.add(new Card(rank, suit, value, path));
            }
        }
        shuffle();
    }

    /**
     * Barajar aleatoriamente el mazo.
     */
    public void shuffle() {
        Collections.shuffle(cards);
        currentIndex = 0;
    }

    /**
     * Saca la siguiente carta del mazo.
     * @return La carta sacada del mazo.
     */
    public Card drawCard() {
        if (currentIndex >= cards.size()) shuffle();
        return cards.get(currentIndex++);
    }

    /**
     * Reparte un número específico de cartas iniciales.
     * @param amount La cantidad de cartas a repartir.
     * @return Una nueva lista de cartas con la cantidad de cartas indicadas.
     */
    public List<Card> dealCards(int amount) {
        List<Card> hand = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            hand.add(drawCard());
        }
        return hand;
    }

    /**
     * Calcula la cantidad de cartas que quedan en el mazo.
     * @return El número restante de cartas.
     */
    public int remainingCards() {
        return cards.size() - currentIndex;
    }

    /**
     * Obtine una referencia a la lista completa del mazo.
     * @return La lista de cartas.
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Añade una carta al mazo para que no quede vació al momento de retomar las cartas.
     * @param c La carta que queda.
     */
    public void addToBottom(Card c) {
        cards.add(c);
    }

}

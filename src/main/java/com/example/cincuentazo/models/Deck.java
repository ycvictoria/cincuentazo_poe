package com.example.cincuentazo.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;

    public void shuffle() {
        // 1. Usa la utilidad de Java para barajar la lista de cartas.
        Collections.shuffle(cards);
    }

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

                // Asegúrate que las imágenes existan en: /resources/com/example/cincuentazo/cards/
                String path = "/com/example/cincuentazo/cards/" + rank + suit + ".png";
                System.out.println("path:" + path);
                cards.add(new Card(rank, suit, value, path));
            }
        }

        shuffle();
    }

    public void addCard(Card card) {
        if (card != null) {
            this.cards.add(card);
        }
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(cards.size() - 1);
    }

    public List<Card> getCards() {
        return cards;
    }

    public int remainingCards() {
        return cards.size();
    }

    public void addToBottom(Card c) {
        cards.add(c);
    }
}

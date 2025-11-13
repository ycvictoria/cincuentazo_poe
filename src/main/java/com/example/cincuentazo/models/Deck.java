package com.example.cincuentazo.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;
    private int currentIndex = 0;

    public Deck() {
        cards = new ArrayList<>();
        String[] suits = {"C", "D", "H", "S"}; // Clubs, Diamonds, Hearts, Spades
        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "0", "J", "Q", "K"}; // 0 representa 10

        for (String suit : suits) {
            for (String rank : ranks) {
                int value;
                switch (rank) {
                    case "A" -> value = 1;
                    case "0" -> value = 10; // este era el cambio de tu rama jdsn
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

    public void shuffle() {
        Collections.shuffle(cards);
        currentIndex = 0;
    }

    public Card drawCard() {
        if (currentIndex >= cards.size()) shuffle();
        return cards.get(currentIndex++);
    }

    public List<Card> dealCards(int amount) {
        List<Card> hand = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            hand.add(drawCard());
        }
        return hand;
    }

    public int remainingCards() {
        return cards.size() - currentIndex;
    }

    public List<Card> getCards() {
        return cards;
    }
}

package com.example.cincuentazo.models;

import javafx.scene.image.Image;

import java.util.Objects;

public class Card {
    private final String name;
    private final String suit;
    private final int valueNumeric;
    private final String imagePath;

    public Card(String name, String suit, int valueNumeric, String imagePath) {
        this.name = name;
        this.suit = suit;
        this.valueNumeric = valueNumeric;
        this.imagePath = imagePath;
    }

    public String getName() { return name; }
    public String getSuit() { return suit; }
    public int getValueNumeric() { return valueNumeric; }
    public String getImagePath() { return imagePath; }

    @Override
    public String toString() {
        return name + " of " + suit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Son el mismo objeto en memoria
        if (o == null || getClass() != o.getClass()) return false; // No es una Carta o es nulo

        Card card = (Card) o;

        // Criterio de Igualdad: Dos cartas son iguales si tienen el mismo nombre Y el mismo palo.
        // También puedes incluir valueNumeric y imagePath si lo necesitas.
        return Objects.equals(name, card.name) && Objects.equals(suit, card.suit);
    }

    @Override
    public int hashCode() {
        // Los campos usados en equals() también deben usarse para generar el hash.
        return Objects.hash(name, suit);
    }

    public Image getImage() {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
    }
}
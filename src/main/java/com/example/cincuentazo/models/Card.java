package com.example.cincuentazo.models;

import javafx.scene.image.Image;

import java.util.Objects;

/**
 * Representa una carta individual del mazo.
 */
public class Card {
    private final String name;
    private final String suit;
    private final int valueNumeric;
    private final String imagePath;

    /**
     * Constructor para crear una instancia de Card.
     * @param name Nombre de la carta
     * @param suit El palo de la carta.
     * @param valueNumeric El valor de la carta.
     * @param imagePath La imagen de la carta.
     */
    public Card(String name, String suit, int valueNumeric, String imagePath) {
        this.name = name;
        this.suit = suit;
        this.valueNumeric = valueNumeric;
        this.imagePath = imagePath;
    }

    /**
     * Obtiene el nombre de la carta.
     * @return El nombre de la carta.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene el palo de la carta.
     * @return El palo de la carta.
     */
    public String getSuit() {
        return suit;
    }

    /**
     * Obtiene el valor de la carta.
     * @return El valor de la carta.
     */
    public int getValueNumeric() {
        return valueNumeric;
    }

    /**
     * Obtiene la ruta de la imagen.
     * @return La ruta de la imagen como string.
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Genera una representación en String de la carta.
     * @return Una representación legible de la carta.
     */
    @Override
    public String toString() {
        return name + " of " + suit;
    }

    /**
     * Comparan la carta con otra para verificar si son iguales.
     * @param o El objeto a comparar.
     * @return Si tiene el mismo palo y nombre.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Son el mismo objeto en memoria
        if (o == null || getClass() != o.getClass()) return false; // No es una Carta o es nulo

        Card card = (Card) o;

        //Dos cartas son iguales si tienen el mismo nombre Y el mismo palo.
        return Objects.equals(name, card.name) && Objects.equals(suit, card.suit);
    }

    /**
     * Calcula y devuelve el código Hash para la carta
     * @return El código hash
     */
    @Override
    public int hashCode() {
        // Los campos usados en equals() también deben usarse para generar el hash.
        return Objects.hash(name, suit);
    }

    /**
     * Obtiene el objeto image utilizando la ruta del recurso.
     * @return Un objeto Image
     */
    public Image getImage() {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
    }
}
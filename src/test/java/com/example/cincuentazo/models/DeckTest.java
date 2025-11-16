package com.example.cincuentazo.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.example.cincuentazo.models.Card;
import com.example.cincuentazo.models.Deck;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeckTest {

    @Test
    void testInitializationDeck() {
        //iniciamos un mazo
        Deck deck = new Deck();

        //Obtener todas las cartas después de la inicialización (y barajado)
        List<Card> allCards = deck.getCards();

        //El tamaño del mazo debe ser 52.
        assertEquals(52, allCards.size(), "El mazo debe inicializarse con 52 cartas.");

        //Todas las cartas deben ser únicas (no hay duplicados).
        Set<String> uniqueCardNames = new HashSet<>();
        for (Card card : allCards) {
            // Usamos la combinación de Nombre y Palo para identificar cada carta única.
            uniqueCardNames.add(card.getName() + card.getSuit());
        }

        assertEquals(52, uniqueCardNames.size(), "Todas las cartas en el mazo deben ser únicas.");
    }
}
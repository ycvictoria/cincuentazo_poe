package com.example.cincuentazo.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class DeckTest {

    /**
     * Prueba 1: Verifica que el constructor inicialice el mazo con 52 cartas
     * únicas (4 palos x 13 rangos) y que la funcionalidad de
     * cartas restantes sea correcta antes de repartir.
     */
    @Test
    void testDeckInitialization() {
        Deck deck = new Deck();
        final int EXPECTED_TOTAL_CARDS = 52;

        // 1. Verificar el tamaño total del mazo
        assertEquals(EXPECTED_TOTAL_CARDS, deck.getCards().size(),
                "El mazo debe inicializarse con 52 cartas.");

        // 2. Verificar las cartas restantes inmediatamente después de la inicialización
        // y la mezcla (shuffle), que pone currentIndex a 0.
        assertEquals(EXPECTED_TOTAL_CARDS, deck.remainingCards(),
                "Al inicio, el número de cartas restantes debe ser 52.");

        // 3. Opcional: Verificar que las 52 cartas sean únicas
        long uniqueCardsCount = deck.getCards().stream().distinct().count();
        assertEquals(EXPECTED_TOTAL_CARDS, uniqueCardsCount,
                "Todas las cartas en el mazo deben ser únicas.");
    }

    /**
     * Prueba 2: Verifica la funcionalidad de drawCard y dealCards,
     * y cómo afecta al conteo de cartas restantes.
     */
    @Test
    void testDealCards() {
        Deck deck = new Deck();
        final int CARDS_TO_DEAL = 5;
        final int INITIAL_REMAINING = 52;

        assertEquals(INITIAL_REMAINING, deck.remainingCards(),
                "El mazo debe comenzar con 52 cartas restantes.");

        // ACT 1: Repartir 5 cartas
        List<Card> hand = deck.dealCards(CARDS_TO_DEAL);

        //después del reparto
        assertEquals(CARDS_TO_DEAL, hand.size(),
                "El método dealCards debe repartir el número exacto de cartas solicitado.");

        int expectedRemainingAfterDeal = INITIAL_REMAINING - CARDS_TO_DEAL;
        assertEquals(expectedRemainingAfterDeal, deck.remainingCards(),
                "Después de repartir 5 cartas, deben quedar 47 en el mazo.");

        //Dibujar una carta adicional
        deck.drawCard();

        //después de drawCard
        int expectedRemainingAfterDraw = expectedRemainingAfterDeal - 1; // 47 - 1 = 46
        assertEquals(expectedRemainingAfterDraw, deck.remainingCards(),
                "Después de repartir y robar una carta, deben quedar 46 en el mazo.");
    }
}
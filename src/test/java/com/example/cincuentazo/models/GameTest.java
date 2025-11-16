package com.example.cincuentazo.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Game, verificando la lógica central del juego.
 */
public class GameTest {

    private Game game;

    /**
     * Helper para crear cartas con el constructor requerido.
     * Calcula el valor numérico base a partir del nombre (rango).
     */
    private Card createCard(String name, String suit) {
        int valueNumeric;
        switch (name) {
            case "A" -> valueNumeric = 1; // As vale 1 en valor numérico base
            case "0" -> valueNumeric = 10; // Diez
            case "J" -> valueNumeric = 11; // Jota
            case "Q" -> valueNumeric = 12; // Reina
            case "K" -> valueNumeric = 13; // Rey
            default -> valueNumeric = Integer.parseInt(name);
        }
        // La ruta es un placeholder para el test.
        return new Card(name, suit, valueNumeric, "/path/to/" + name + suit + ".png");
    }

    /**
     *
     * @param deck  El mazo del que se robarán las cartas.
     * @param count El número de cartas a robar.
     * @return Una lista de las cartas robadas.
     */
    private List<Card> drawMultipleCards(Deck deck, int count) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Card card = deck.drawCard();
            if (card != null) {
                cards.add(card);
            } else {
                // Manejo si el mazo se queda sin cartas antes de llegar al conteo
                break;
            }
        }
        return cards;
    }

    @BeforeEach
    void setUp() {
        // Inicializa Game y el jugador humano antes de cada prueba (Estado limpio)
        game = new Game();
        Player playerHuman = new Player("Jugador Humano", true);
        game.setPlayer(playerHuman);
    }


    /**
     * Verifica que el mazo inicial tenga 52 cartas y que el barajado cambie el orden.
     */
    @Test
    void testDeckIsAlways52(){
        //Crear un mazo de referencia. Asumimos que new Deck() produce un orden inicial predecible.
        Deck orderedDeck = new Deck();

        //Extraer 52 cartas de un mazo recién creado para obtener el orden inicial
        List<Card> orderedList = drawMultipleCards(orderedDeck, 52);

        //El mazo debe tener 52 cartas
        assertEquals(52, orderedList.size(), "El mazo debe contener exactamente 52 cartas.");

        //Nuevo mazo para barajar
        Deck testDeck = new Deck();

        //Barajar y extraer las cartas
        testDeck.shuffle();
        List<Card> shuffledList = drawMultipleCards(testDeck, 52);

        //La lista barajada debe ser diferente del orden inicial
        boolean isShuffled = false;
        for (int i = 0; i < 52; i++) {
            // Compara si la carta en la posición 'i' del mazo barajado es diferente a la
            // carta en la posición 'i' del mazo 'ordenado'
            if (!orderedList.get(i).equals(shuffledList.get(i))) {
                isShuffled = true;
                break;
            }
        }

        assertTrue(isShuffled, "El barajado debe haber cambiado significativamente el orden de las cartas.");
    }

    /**
     * Verifica la correcta implementación de la unicidad de las cartas (equals) y la
     * representación en cadena (toString).
     */
    @Test
    void testCardEquality() {

        Card cardA = createCard("5", "H"); // Cinco de Corazones
        Card cardB = createCard("5", "H"); // Otro Cinco de Corazones (debería ser igual)
        Card cardC = createCard("6", "H"); // Seis de Corazones (debería ser diferente)

        assertEquals(cardA, cardB, "Dos cartas con el mismo nombre y palo deben ser iguales (equals).");
        assertNotEquals(cardA, cardC, "Dos cartas diferentes deben ser desiguales (equals).");


        assertTrue(cardA.toString().contains("5"), "La representación de la carta debe contener su nombre (5).");
        assertTrue(cardA.toString().contains("H"), "La representación de la carta debe contener su palo (H).");
    }

    @Test
    void testEvaluateCard() {
        Card cardKing = createCard("K", "H");    // Figura -> -10
        Card cardNine = createCard("9", "D");    // Nueve -> 0
        Card cardTen = createCard("0", "C");     // Diez -> 10
        Card cardAce = createCard("A", "S");     // As -> 1 o 10

        // 1. Figura -> Resta 10
        assertEquals(-10, game.evaluateCardEffect(cardKing, 30), "K debe devolver -10.");

        // 2. Nueve -> Suma 0
        assertEquals(0, game.evaluateCardEffect(cardNine, 25), "9 debe devolver 0.");

        // 3. Diez -> Suma 10
        assertEquals(10, game.evaluateCardEffect(cardTen, 10), "0 (Diez) debe devolver 10.");

        // 4. As (Caso 10): La suma no pasa de 50
        assertEquals(10, game.evaluateCardEffect(cardAce, 40),
                "El As debe sumar 10 si la suma resultante es exactamente 50.");

        // 5. As (Caso 1): La suma pasa de 50
        assertEquals(1, game.evaluateCardEffect(cardAce, 41),
                "El As debe sumar 1 si sumar 10 hace que la suma exceda 50.");
    }

    @Test
    void testEvaluateCardE() {
        Card cardTwo = createCard("2", "C");
        Card cardFive = createCard("5", "H");

        assertEquals(2, game.evaluateCardEffect(cardTwo, 10), "2 debe devolver 2.");
    }
}

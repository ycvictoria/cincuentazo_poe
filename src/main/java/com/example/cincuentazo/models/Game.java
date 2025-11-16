package com.example.cincuentazo.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    private Player player;
    private Deck deck;
    private int numMachinePlayer;
    private int cardsPerPlayer = 4;
    //private final List<Card> discardPile = new ArrayList<>();
    private int actualSum;
    private final List<Player> machinePlayers = new ArrayList<>();
    private int currentTurnIndex = 0;
    private Card lastPlayedCard;
    private Deck discardPile = new Deck();

    public Game() {
        deck = new Deck();
    }

    public int getCardsPerPlayer() { return cardsPerPlayer; }
    public Player getPlayer() { return player; }
    public Deck getDeck() { return deck; }
    public int getNumMachinePlayer() { return numMachinePlayer; }
    public Deck getDiscardPile(){return discardPile; }
    public void setLastPlayedCard(Card card) {this.lastPlayedCard = card;}
    public Card getLastPlayedCard() {return lastPlayedCard;}


    public void setCardsPerPlayer(int numCards) { this.cardsPerPlayer = numCards; }
    public void setPlayer(Player player) { this.player = player; }
    public void setDeck(Deck deck) { this.deck = deck; }

    public void setNumMachinePlayer(int numMachinePlayer) {
        this.numMachinePlayer = numMachinePlayer;
        createMachinePlayers();
    }

    /**
     * Método que maneja una carta que ha sido lanzada a la mesa.
     * Mueve la carta anterior (lastPlayedCard) a la pila de descarte.
     * @param playedCard La carta recién lanzada.
     */
    public void cardPlayed(Card playedCard) {
        // 1. Si ya había una carta en la mesa, se mueve a la pila de descarte.
        if (lastPlayedCard != null) {
            discardPile.addCard(lastPlayedCard);
        }

        // 2. La carta actual se convierte en la última jugada.
        this.lastPlayedCard = playedCard;

        // 3. Evaluar y actualizar la suma (la lógica ya la tienes implementada)
        this.actualSum += evaluateCardEffect(playedCard, this.actualSum);
    }

    /**
     * Rellena el mazo con las cartas de la pila de descarte.
     * Se llama cuando el mazo queda vacío. La última carta jugada (lastPlayedCard)
     * se queda en la mesa (NO se mueve al mazo).
     */
    private void refillDeckFromDiscard() {
        System.out.println("Mazo vacío. Rellenando con cartas de descarte...");

        // 1. Obtener una referencia a las cartas descartadas
        List<Card> discardedCards = discardPile.getCards();

        // 2. Mover todas las cartas al mazo principal
        // Usamos addAll() para transferir las cartas de la lista de descarte
        // a la lista interna del mazo principal (deck).
        deck.getCards().addAll(discardedCards);

        // 3. Vaciar la pila de descarte
        // Como 'discardedCards' es una referencia directa a la lista interna de discardPile,
        // al usar .clear(), vaciamos correctamente el descarte sin crear un nuevo Deck.
        discardedCards.clear();

        // 4. Barajar el nuevo mazo principal para mezclar las cartas reincorporadas.
        deck.shuffle();

        System.out.println("Mazo rellenado y barajado. Cartas restantes: " + deck.remainingCards());
    }

    public Card drawCardForPlayer() {
        // 1. Intenta robar la carta del mazo actual.
        // Asumimos que Deck.drawCard() devuelve null si está vacío.
        Card card = deck.drawCard();

        // 2. Si el mazo estaba vacío (card == null), se activa la lógica de relleno.
        if (card == null) {
            // Ejecuta la lógica para mover el discardPile al mazo.
            refillDeckFromDiscard();

            // Intenta robar de nuevo del mazo recién rellenado.
            card = deck.drawCard();
        }

        return card;
    }

    private void createMachinePlayers() {
        machinePlayers.clear();

        List<String> uniqueNames = generateUniqueBotNames(numMachinePlayer);

        for (String name : uniqueNames) {
            machinePlayers.add(new Player(name, false));
        }
    }


    private List<String> generateUniqueBotNames(int count) {
        List<String> names = new ArrayList<>(List.of(
                "Astra", "Nexo", "Vex", "Luma", "Zyon", "Kora", "Hexa", "Volt",
                "Pixel", "Nova", "Cyra", "Rexel", "Zero", "Flux", "Orin", "Vanta", "Nyx",
                "Ciri", "Axo", "Meko")
        );

        // Mezcla los nombres para que salgan en orden aleatorio
        Collections.shuffle(names);

        // Devuelve solo los que necesitas, sin repetir
        return names.subList(0, Math.min(count, names.size()));
    }
    /** Humano + bots (si el humano es null, solo bots) */
    public List<Player> getAllPlayers() {
        List<Player> all = new ArrayList<>();
        if (player != null) all.add(player);
        all.addAll(machinePlayers);
        return all;
    }

    public List<Player> getMachinePlayers() {
        return machinePlayers;
    }

    /** Reparte cartas a todos los jugadores actuales (humano + bots). */
    public void dealInitialHands(int cardsPerPlayer) {
        List<Player> all = getAllPlayers(); // Obtiene todos los jugadores

        for (Player p : all) {
            // En lugar de obtener una carta a la vez (drawCard),
            // Llama a drawCard() la cantidad de veces necesaria para darle su mano completa.
            for (int i = 0; i < cardsPerPlayer; i++) {
                Card card = deck.drawCard(); // Llama a drawCard(), que REMUEVE la carta del mazo.
                if (card != null) {
                    p.addCard(card);
                }
            }
            // Nota: Si tu clase Deck ya tiene un dealCards(int amount), podrías usar:
            // p.getHand().addAll(deck.dealCards(cardsPerPlayer));
        }
    }

    public void dealInitialHands() {
        List<Player> all = getAllPlayers();
        for (Player p : all) {
            // Reparte cardsPerPlayer (ej: 4) cartas al jugador
            for (int i = 0; i < cardsPerPlayer; i++) {
                Card card = deck.drawCard();
                if (card != null) {
                    p.addCard(card);
                }
            }
        }
    }
    public void setActualSum(int actualSum) { this.actualSum = actualSum; }
    public int getActualSum() { return actualSum; }

    /**
     * Calcula el efecto real de una carta sobre la suma de la mesa (Cincuentazo).
     * Reglas:
     * - 2–8 y 10 suman su número.
     * - 9 no suma ni resta.
     * - J, Q, K restan 10.
     * - A suma 1 o 10 según convenga (sin pasar de 50).
     * - "0" representa el 10 en los nombres de archivo/cartas.
     */
    public int evaluateCardEffect(Card card, int currentSum) {
        String name = card.getName();
        int gameValue;

        switch (name) {
            case "J":
            case "Q":
            case "K":
                gameValue = -10;
                break;

            case "9":
                gameValue = 0;
                break;

            case "A":
                gameValue = (currentSum + 10 <= 50) ? 10 : 1;
                break;

            case "0": // 10
                gameValue = 10;
                break;

            default:
                try {
                    gameValue = Integer.parseInt(name);
                } catch (NumberFormatException e) {
                    gameValue = 0;
                }
                break;
        }

        return gameValue;
    }

    /**
     * Devuelve las cartas de un jugador eliminado o de la mesa al mazo y baraja.
     * @param cards La lista de cartas a devolver.
     */
    public void returnCardsToDeck(List<Card> cards) {
        if (cards == null || cards.isEmpty()) return;
        for (Card c : cards) {
            deck.addToBottom(c);  // implementar en Deck
        }
        cards.clear();
        deck.shuffle();
    }

    public Card playCard(Card card) {
        // 1. Encontrar el jugador actual (esta lógica puede variar,
        // pero para fines de prueba, necesitas saber quién juega).
        Player player = getPlayer(); // Asumiendo que 'getPlayer()' devuelve el jugador humano

        if (player == null || !player.getHand().contains(card)) {
            System.err.println("Error: El jugador actual no tiene esta carta.");
            return null;
        }

        // 2. Remueve la carta de la mano del jugador.
        player.getHand().remove(card);

        // 3. Mueve la carta que estaba en la mesa al descarte (si existe).
        if (lastPlayedCard != null) {
            discardPile.addCard(lastPlayedCard);
        }

        // 4. Establece la carta actual como la última jugada.
        this.lastPlayedCard = card;

        return card;
    }


}

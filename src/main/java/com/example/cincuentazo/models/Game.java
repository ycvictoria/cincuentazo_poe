package com.example.cincuentazo.models;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private Player player;
    private Deck deck;
    private int numMachinePlayer;

    private int actualSum;
    private final List<Player> machinePlayers = new ArrayList<>();
    private int currentTurnIndex = 0;
    private Card lastPlayedCard;

    public Game() {
        deck = new Deck();
    }

    public Player getPlayer() { return player; }
    public Deck getDeck() { return deck; }
    public int getNumMachinePlayer() { return numMachinePlayer; }

    public void setPlayer(Player player) { this.player = player; }
    public void setDeck(Deck deck) { this.deck = deck; }

    public void setNumMachinePlayer(int numMachinePlayer) {
        this.numMachinePlayer = numMachinePlayer;
        createMachinePlayers();
    }

    private void createMachinePlayers() {
        machinePlayers.clear();
        for (int i = 1; i <= numMachinePlayer; i++) {
            machinePlayers.add(new Player("Bot " + i, false));
        }
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
    public void dealCards(int cardsPerPlayer) {
        List<Player> all = getAllPlayers();
        for (int i = 0; i < cardsPerPlayer; i++) {
            for (Player p : all) {
                Card card = deck.drawCard();
                if (card != null) {
                    p.addCard(card);
                }
            }
        }
    }

    public void setNumCardPlayer(int numCardPlayer) { /* pendiente según reglas */ }
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

}

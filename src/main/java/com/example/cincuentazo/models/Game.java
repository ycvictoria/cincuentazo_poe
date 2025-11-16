package com.example.cincuentazo.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase principal que gestiona la lógica y el estado del juego.
 *
 */
public class Game {

    private Player player;
    private Deck deck;
    private int numMachinePlayer;
    private int actualSum;
    private final List<Player> machinePlayers = new ArrayList<>();
    private int currentTurnIndex = 0;
    private Card lastPlayedCard;

    /**
     * Constructor inicia un nuevo mazo de cartas barajado.
     */
    public Game() {
        deck = new Deck();
    }

    /**
     * Obtiene el objeto Player que representa al jugador humano.
     * @return El objeto jugador humano.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Obtiene el objeto Deck del juego.
     * @return El Deck de cartas.
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Obtine la cantidad de jugadores máquinas configurados.
     * @return El número de bot.
     */
    public int getNumMachinePlayer() {
        return numMachinePlayer;
    }

    /**
     * Establece el objeto Player que representa el jugador humano.
     * @param player El objeto Player.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Establece el Deck de cartas.
     * @param deck El deck a establecer.
     */
    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    /**
     * Establece la cantidad de jugadores máquina.
     * @param numMachinePlayer la cantidad de jugadores máquina
     */
    public void setNumMachinePlayer(int numMachinePlayer) {
        this.numMachinePlayer = numMachinePlayer;
        createMachinePlayers();
    }

    /**
     * Crea la lista de jugadores máquina y asigna el nombre de cada uno.
     */
    private void createMachinePlayers() {
        machinePlayers.clear();
        List<String> uniqueNames = generateUniqueBotNames(numMachinePlayer);

        for (String name : uniqueNames) {
            // Se crea el jugador máquina con 'isHuman' = false
            machinePlayers.add(new Player(name, false));
        }
    }

    /**
     * Lista de nombres unicos y aleatorios para cada bot.
     * @param count El número de nombres
     * @return Una lista de String con nombre del bot.
     */
    private List<String> generateUniqueBotNames(int count) {
        List<String> names = new ArrayList<>(List.of(
                "Astra", "Nexo", "Vex", "Luma", "Zyon", "Kora", "Hexa", "Volt",
                "Pixel", "Nova", "Cyra", "Rexel", "Zero", "Flux", "Orin", "Vanta", "Nyx",
                "Ciri", "Axo", "Meko")
        );
        Collections.shuffle(names);
        // Devuelve solo los que necesitas, sin repetir
        return names.subList(0, Math.min(count, names.size()));
    }

    /**
     * Obtiene una lista que contiene a todo los jugadores.
     * @return Una lista de Player que inician la partida.
     */
    public List<Player> getAllPlayers() {
        List<Player> all = new ArrayList<>();
        if (player != null) all.add(player);
        all.addAll(machinePlayers);
        return all;
    }

    /**
     * Obtiene una lista de jugadores máquinas.
     * @return Un lista de objetos (bots).
     */
    public List<Player> getMachinePlayers() {
        return machinePlayers;
    }

    /**
     * Reparte una cantidad específica de cartas al iniciar la partida.
     * @param cardsPerPlayer Cantidad de cartas que recibe cada jugador.
     */
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

    /**
     * Establece la suma de las cartas en la mesa.
     * @param actualSum El nuevo valor dela suma.
     */
    public void setActualSum(int actualSum) {
        this.actualSum = actualSum;
    }

    /**
     * Obtiene la suma actual de la mesa
     * @return la suma de las cartas.
     */
    public int getActualSum() {
        return actualSum;
    }

    /**
     * Calcula el valor que se le dará a cada carta.
     * @param card la carta jugada
     * @param currentSum La suma actual de las cartas en la mesa antes de jugar la carta.
     * @return el valor que se le asignó a la carta.
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
                    System.err.println("Error: Carta con nombre no numérico o especial no manejado: " + name);
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
            deck.addToBottom(c);
        }
        cards.clear();
        deck.shuffle();
    }

}

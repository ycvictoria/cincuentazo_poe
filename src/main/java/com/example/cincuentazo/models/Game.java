package com.example.cincuentazo.models;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private Player player;
    private Deck deck;
    private int numMachinePlayer;

    private int actualSum;
    private List<Player> machinePlayers = new ArrayList<>();
    private List<Player> players;
    private int currentTurnIndex = 0;
    private Card lastPlayedCard;

    public  Game(){

        deck= new Deck();
    }
    public Player getPlayer() {return player;}

    public Deck getDeck() {return deck;}

    public int getNumMachinePlayer() {return numMachinePlayer;}

    public void setPlayer(Player player) {this.player=player;}
    public void setDeck(Deck deck) {this.deck=deck;}

    public void setNumMachinePlayer(int numMachinePlayer) {
        this.numMachinePlayer=numMachinePlayer;
        createMachinePlayers();
    }

    private void createMachinePlayers() {
        machinePlayers.clear();
        for (int i = 1; i <= numMachinePlayer; i++) {
            machinePlayers.add(new Player("Bot " + i, false));
        }
    }
    public List<Player> getAllPlayers() {
        List<Player> all = new ArrayList<>();
        all.add(player);
        all.addAll(machinePlayers);
        return all;
    }
    public List<Player> getMachinePlayers() {
        return machinePlayers;
    }
    public void dealCards(int cardsPerPlayer) {
        for (int i = 0; i < cardsPerPlayer; i++) {
            for (Player player : players) {
                Card card = deck.drawCard();
                if (card != null) {
                    player.addCard(card);
                }
            }
        }
    }
    public void setNumCardPlayer(int numCardPlayer) {}
    public void setActualSum(int actualSum) {this.actualSum=actualSum;}
    public int getActualSum() {return actualSum;}

<<<<<<< HEAD
=======

    /**
     * Calcula el efecto real de una carta sobre la suma de la mesa,
     * aplicando las reglas del juego Cincuentazo.
     *
     * Reglas:
     * - 2–8 y 10 suman su número.
     * - 9 no suma ni resta.
     * - J, Q, K restan 10.
     * - A suma 1 o 10 según convenga (sin pasar de 50).
     *
     * @param card        carta a evaluar.
     * @param currentSum  suma actual en la mesa.
     * @return valor que se debe sumar (puede ser negativo o cero).
     */
    public int evaluateCardEffect(Card card, int currentSum) {
        String name = card.getName();
        int gameValue = 0;

        switch (name) {
            case "J", "Q", "K":
                gameValue = -10;
                break;

            case "9":
                gameValue = 0;
                break;

            case "A":
                if (currentSum + 10 <= 50) {
                    gameValue = 10;
                } else {
                    gameValue = 1;
                }
                break;

            case "0":
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



>>>>>>> jdsn
}

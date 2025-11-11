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

}

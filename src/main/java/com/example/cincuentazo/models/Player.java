package com.example.cincuentazo.models;


import java.util.ArrayList;
import java.util.List;

/**
 * Representa a un jugador, almacena el nombre, determina si es humano, y su mano actual.
 */
public class Player {
    private String nickName;
    private boolean isHuman;
    private List<Card> hand;

    /**
     * Constructor para inicializar un nuevo jugador.
     * @param name El nombre del jugador.
     * @param isHuman Si es humano o noo
     */
    public Player(String name, boolean isHuman) {
        this.nickName = name;
        this.isHuman = isHuman;
        this.hand = new ArrayList<>();
    }

    /**
     * Obtiene el apodo del jugador
     * @return el apodo del jugador.
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Comprueba si es humano o no
     * @return true si es humano o false si no.
     */
    public boolean isHuman() {
        return isHuman;
    }

    /**
     *Obtiene la mano de cartas actuales del jugador.
     * @return una lista que representa las cartas.
     */
    public List<Card> getHand() {
        return hand;
    }

    /**
     * Añade una carta a la mano del jugador
     * @param card La carta a añadir.
     */
    public void addCard(Card card) {
        hand.add(card);
    }

    /**
     * Elimina una carta de la mano del jugador.
     * @param card La carta que se va a eliminar.
     */
    public void removeCard(Card card) {
        hand.remove(card);
    }

    /**
     * Establece el nombre del jugador o lo cambia.
     * @param nickname Nuevo apodo.
     */
    public void setNickname(String nickname) {
        this.nickName = nickname;
    }
}

package com.example.cincuentazo.models;


import java.util.ArrayList;
import java.util.List;

public class Player {
    private String nickName;
    private boolean isHuman;
    private List<Card> hand;

    public Player(String name, boolean isHuman) {
        this.nickName = name;
        this.isHuman = isHuman;
        this.hand = new ArrayList<>();
    }

    public String getNickName() {
        return nickName;
    }

    public boolean isHuman() {
        return isHuman;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void removeCard(Card card) {
        hand.remove(card);
    }

    public void setNickname(String nickname) {
        this.nickName = nickname;
    }
}

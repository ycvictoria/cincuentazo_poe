package com.example.cincuentazo.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class GameTest {

    private Game game;
    private Player playerHuman;

    @BeforeEach
    void setUp() {
        game = new Game();
        playerHuman =  new Player("Jugador Humano", true);
        game.setPlayer(playerHuman);
    }

    @Test
    void testInitialDeckSize(){
        //prueba para comprobar que un mazo tiene 52 cartas
        assertEquals(52, game.getDeck().getCards().size(),
                "El mazo se compone de 52 cartas al iniciar.");
    }

    @Test
    void testPlayerMachine(){
        //comprobamos si funciona la elección de los jugadores máquinas.
        game.setNumMachinePlayer(2);

        List<Player> machines = game.getMachinePlayers();

        assertEquals(2, machines.size(),"Debe crear el numero correcto de jugadores");
        assertEquals("Bot 2", machines.get(1).getNickName(), "El nombre del segundo juagdor maquina debe ser bot 2");
        assertFalse(machines.get(0).isHuman(),"Todo los judadores maquinas debe ser isHuman=false");
    }

    @Test
    void testGetAllPlayers(){
        //verificamos primero que se inicie la partida con 3 jugadores máquinas y 1 humano.
        game.setNumMachinePlayer(3);
        List<Player> allPlayersBots = game.getAllPlayers();
        assertEquals(4, allPlayersBots.size(),"Debe haber 4 jugadores 3 bot y 1 humano");

        //verificamos que el  primer jugador debe ser el humano
        assertTrue(allPlayersBots.get(0).isHuman(), "El primer jugador de la lista debe ser humano");

        //Verificamos si al quedar eliminado el jugador humano sigue jugando los bots
        game.setPlayer(null);
        List<Player> onlyPlayerBots = game.getAllPlayers();
        assertEquals(3,onlyPlayerBots.size(), "Deben quedar los 3 jugadores Bots");
    }

    @Test
    void testDealCards(){
        game.setNumMachinePlayer(2); // 3 jugadores con el humano
        int cardsToDeal = 4;

        game.dealCards(cardsToDeal); // repartir las cartas a los jugadores

        //verificamos el tamaño de la mano de cada jugador
        assertEquals(cardsToDeal, game.getPlayer().getHand().size(), "El jugador humano debe tener 4 cartas");
        assertEquals(cardsToDeal,game.getMachinePlayers().get(0).getHand().size(), "El bot 1 debe tener 4 cartas");

        //Verificamos si al iniciar la partida el mazo se reduce
        //Mazo inicial (52) - Cartas repartidas (3 jugadores * 4 cartas) = 40
        //assertEquals(52 - (3 * cardsToDeal), game.getDeck().getCards().size(), "El mazo debe tener 12 cartas menos");
    }
}
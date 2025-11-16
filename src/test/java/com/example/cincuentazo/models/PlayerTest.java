package com.example.cincuentazo.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    void testInitialization() {
        // Iniciamos parametros para la prueba
        String testName = "Ricardo";
        boolean isHuman = true;
        Player player = new Player(testName, isHuman);

        // Crear dos cartas de prueba (usando valores arbitrarios)
        Card card1 = new Card("A", "H", 1, "/path/a_h.png");
        Card card2 = new Card("K", "C", 13, "/path/k_c.png");

        // Prueba de Inicialización
        assertEquals(testName, player.getNickName(), "El nombre del jugador debe ser correcto.");
        assertTrue(player.isHuman(), "El jugador debe ser identificado como humano.");
        assertTrue(player.getHand().isEmpty(), "La mano debe estar vacía al inicio.");

        //Añadir la primera carta
        player.addCard(card1);

        // Después de añadir
        assertEquals(1, player.getHand().size(), "La mano debe tener una carta.");
        assertTrue(player.getHand().contains(card1), "La mano debe contener la carta añadida.");

        //Añadir la segunda carta
        player.addCard(card2);

        //Comprar cuantas cartas se añadieron
        assertEquals(2, player.getHand().size(), "La mano debe tener dos cartas.");

        //Remover la primera carta
        player.removeCard(card1);

        //Después de remover
        assertEquals(1, player.getHand().size(), "La mano debe tener una carta después de remover.");
        assertFalse(player.getHand().contains(card1), "La carta removida no debe estar en la mano.");
        assertTrue(player.getHand().contains(card2), "La carta restante (card2) debe permanecer.");
    }

@Test
    void testNickName() {
    //Iniciamos los parametros para iniciar la prueba
    String initialName = "Bot_1";
    String newName = "Bot_2";
    boolean isHuman = false;
    Player machinePlayer = new Player(initialName, isHuman);

    //Tipo de Jugador
    assertFalse(machinePlayer.isHuman(), "El jugador es maquina.");
    //Nombre inicial
    assertEquals(initialName, machinePlayer.getNickName(), "El nombre inicial debe ser Bot_1.");

    //Cambiar el nickname
    machinePlayer.setNickname(newName);
    //Actualización del nombre
    assertEquals(newName, machinePlayer.getNickName(), "El nickname debe actualizarse a Bot_2.");
    //Comprobar que la mano sigue vacía (el estado no se ve afectado)
    assertTrue(machinePlayer.getHand().isEmpty(), "La mano debe seguir vacía después de cambiar el nickname.");
}
}
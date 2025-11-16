package com.example.cincuentazo.controllers;

import com.example.cincuentazo.models.Player;
import com.example.cincuentazo.views.GameView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Controlador de la vista de bienvenida.
 * Captura el nombre del jugador humano.
 *
 */
public class WelcomeController extends Stage {
    @FXML
    private TextField nicknameTxtfield;

    @FXML
    private Button startGameBtn;

    @FXML
    private ComboBox<Integer> comboJugadores;

    /**
     * Constructor del controlador.
     */
    public WelcomeController() {

    }

    /**
     * Maneja el evento del botón Empezar el juego.
     * @param event evento generado por él botón
     * @throws IOException si hay error al cargar la ventana salta la exception.
     */
    @FXML
    void startGame(ActionEvent event) throws IOException{
        String nickname = nicknameTxtfield.getText();
        Integer numMaquinas = comboJugadores.getValue();

        if (nickname == null || nickname.trim().isEmpty()) {
            showAlert("¿Cómo te llamas?",
                    "Ingresa por favor tu nombre para empezar.",
                    Alert.AlertType.WARNING);
            return;
        }
        if (numMaquinas == null ) {
            showAlert("¿Jugarás solo?",
                    "Por favor, selecciona el número de jugadores máquina.",
                    Alert.AlertType.WARNING);
            return;
        }
        // Inicio del juego
        int num = numMaquinas.intValue();
        Player player = new Player(nickname, true);
        player.setNickname(nickname);
        System.out.println(player.getNickName());
        System.out.println("Iniciando juego con " + numMaquinas + " jugadores CPU...");
        // Cierre de la ventana actual
        Node sourceNode = (Node)event.getSource();
        Scene scene = sourceNode.getScene();
        Stage stage = (Stage)scene.getWindow();
        stage.close();
        GameView gameView= new GameView();
        gameView.getGameController().setStartGame(player, num);
        gameView.show();
    }

    /**
     * Método de iniciación, se llama automáticamente.
     */
    @FXML
    public void initialize() {
        nicknameTxtfield.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText() != null) {
                change.setText(change.getText().toUpperCase());
            }
            return change;
        }));
        comboJugadores.getItems().addAll(1, 2, 3);
        comboJugadores.setPromptText("Selecciona");
        comboJugadores.setValue(1);
        startGameBtn.setCursor(Cursor.HAND);
        startGameBtn.setDefaultButton(true);
    }
    // Muestra la alerta
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

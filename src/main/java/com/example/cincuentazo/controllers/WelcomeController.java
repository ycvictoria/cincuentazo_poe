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

public class WelcomeController extends Stage {
    @FXML
    private TextField nicknameTxtfield;

    @FXML
    private Button startGameBtn;

    @FXML
    private ComboBox<Integer> comboJugadores;

    public WelcomeController() {}

    @FXML
    void startGame(ActionEvent event) throws IOException{

        String nickname = nicknameTxtfield.getText();
        Integer numMaquinas = comboJugadores.getValue(); // Capturar como entero. (Puede ser null).

        // Validación de campos

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

        int num = numMaquinas.intValue(); // Usar el valor validado, ahora es seguro desempacar.

        Player player = new Player(nickname, true);
        player.setNickname(nickname);
        System.out.println(player.getNickName());
        System.out.println("Iniciando juego con " + numMaquinas + " jugadores CPU...");



        // Cierre de la ventana actual
        Node sourceNode = (Node)event.getSource();
        Scene scene = sourceNode.getScene();
        Stage stage = (Stage)scene.getWindow();
        stage.close();
        //GameView gameView = GameView.getInstance();
        GameView gameView= new GameView();
        gameView.getGameController().setStartGame(player, num);
        gameView.show();
    }

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
        alert.setHeaderText(null); // No mostrar un encabezado complejo
        alert.setContentText(content);
        alert.showAndWait();
    }
}

package com.example.cincuentazo.controllers;

import com.example.cincuentazo.models.Player;
import com.example.cincuentazo.views.GameView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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
        Player player = new Player(nickname, true);
        player.setNickname(nickname);
        System.out.println(player.getNickName());
        Integer numMaquinas = comboJugadores.getValue();
        if(numMaquinas==null){}else{
            System.out.println("Iniciando juego con " + numMaquinas + " jugadores CPU...");
        }
        int num= comboJugadores.getValue();
        GameView gameView = GameView.getInstance();
        gameView.getGameController().setStartGame(player, num);
        gameView.show();


        Node sourceNode = (Node)event.getSource();
        Scene scene = sourceNode.getScene();
        Stage stage = (Stage)scene.getWindow();
        stage.close();
    }

    @FXML
    public void initialize() {
        nicknameTxtfield.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText() != null) {
                change.setText(change.getText().toUpperCase());
            }
            return change; // 👈 debes devolver el change
        }));

        comboJugadores.getItems().addAll(1, 2, 3);
        comboJugadores.setPromptText("N° jugadores CPU");
        startGameBtn.setCursor(Cursor.HAND);
        startGameBtn.setDefaultButton(true);

    }
}

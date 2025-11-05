package com.example.cincuentazo.views;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeView extends Stage {
    public WelcomeView() throws IOException {
        //  this.setTitle("Craps Game");
        System.out.println(getClass().getResource("/com/example/cincuentazo/welcome-view.fxml"));

        FXMLLoader fxmlLoader = new FXMLLoader(

                getClass().getResource("/com/example/cincuentazo/welcome-view.fxml")
        );

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        this.setScene(scene);
      /*  this.getIcons().add(
                new Image(getClass().getResourceAsStream("/com/example/crapsgame80/images/favicon.png"))
        );*/

    }
}

package com.example.cincuentazo;

import com.example.cincuentazo.views.WelcomeView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        WelcomeView welcomeView = new WelcomeView();
        welcomeView.show();
    }
}

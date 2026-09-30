package com.example.fastwriting.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WelcomeController {

    @FXML
    private Button btnStart;

    @FXML
    private void onStart() {
        System.out.println("Comenzar -> deberia abrir Game.fxml");
    }
}
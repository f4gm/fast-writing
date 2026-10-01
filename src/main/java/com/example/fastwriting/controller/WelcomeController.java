package com.example.fastwriting.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class WelcomeController {

    @FXML
    private Button StartButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label instructionLabel;

    @FXML
    private void onStart() {
        System.out.println("Comenzar -> deberia abrir Game.fxml");
    }
}
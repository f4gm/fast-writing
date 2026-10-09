package com.example.fastwriting.controller;

import com.example.fastwriting.model.GameState;
import com.example.fastwriting.service.timer.Timer;
import com.example.fastwriting.service.timer.TimerListener;
import com.example.fastwriting.service.navigation.Navigable;
import com.example.fastwriting.service.navigation.Navigator;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

/**
 * Controlador para la escena del juego. Conecta el modelo {@link GameState} con
 * la vista, gestiona el {@link Timer} del nivel.
 */
public class GameController implements Navigable, TimerListener {
    private Navigator navigator;

    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    private GameState gameState;
    private Timer timer;

    @FXML
    private Label levelLabel;

    @FXML
    private Label timerLabel;

    @FXML
    private Label wordLabel;

    @FXML
    private Label feedback;
    private final PauseTransition feedbackTimer = new PauseTransition(Duration.seconds(2));

    @FXML
    private TextField inputTextField;

    @FXML
    private Button validateButton;

    /**
     * Clase interna que implementa {@link EventHandler} para el botón de validación.
     */
    private class ValidateHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            onValidate();
        }
    }

    @FXML
    private void initialize() {
        // Los controladores se registran desde el código, no desde el archivo FXML.
        validateButton.setOnAction(new ValidateHandler());
        inputTextField.addEventHandler(KeyEvent.KEY_PRESSED, new EnterKeyAdapter(this::onValidate));

        // Evento de ratón: hacer clic en cualquier parte del panel devuelve el cursor
        // al input para que el jugador nunca pierda el foco (UX).
        inputTextField.getParent().setOnMouseClicked(mouseEvent -> inputTextField.requestFocus());

        gameState = new GameState();
        gameState.startNewGame();
        startLevel();
    }

    /**
     * Dibuja el nivel actual e inicia su cuenta atrás.
     * Se crea un nuevo temporizador para cada nivel, ya que su duración
     * se fija en el momento de la creación (se reduce cada 5 niveles).
     */
    private void startLevel() {
        if (timer != null) {
            timer.stop();
        }
        paintLevel();
        timer = new Timer(gameState.getTimeLimit());
        timer.setListener(this);
        timer.reset();
        timer.start();
    }

    private void paintLevel() {
        levelLabel.setText("Nivel: " + gameState.getLevel());
        timerLabel.setText(timeText(gameState.getTimeLimit()));
        wordLabel.setText(gameState.getCurrentWord());
    }

    private String timeText(double seconds) {
        return String.format("Tiempo: %.1fs", seconds);
    }

    @Override
    public void onTick(double remainingSeconds) {
        timerLabel.setText(timeText(remainingSeconds));
    }

    /**
     * Invocado por el temporizador al llegar a cero: la respuesta escrita
     * se valida automáticamente; si es incorrecta o está vacía, la partida finaliza.
     */
    @Override
    public void onTimeUp() {
        timerLabel.setText(timeText(0));
        if (!tryAnswer()) {
            endGame("Tiempo agotado");
        }
    }

    /**
     * Comprueba la respuesta escrita. Si es correcta, avanza de nivel,
     * muestra la retroalimentación e inicia la siguiente cuenta atrás.
     *
     * @return true cuando la respuesta es totalmente correcta
     */
    private boolean tryAnswer() {
        String answer = inputTextField.getText();

        if (!gameState.checkAnswer(answer)) {
            return false;
        }

        if (timer != null) {
            timer.stop();
        }
        gameState.nextLevel();

        int MAX_LEVEL = 101;
        if (gameState.getLevel() == MAX_LEVEL) {
            endGame("Felicidades. ¡Haz terminado el juego!");
        }
        inputTextField.clear();
        showFeedback("¡Correcto! Nivel superado. Ahora va el nivel " + gameState.getLevel() + ".");
        startLevel();
        return true;
    }

    /**
     * Valida la respuesta escrita por el jugador.
     * Un error manual no hace perder el nivel, solo el temporizador provoca la derrota.
     */
    @FXML
    private void onValidate() {
        if (tryAnswer()) {
            return;
        }

        if (timer != null) {
            timer.stop();
        }
        showFeedback("¡Incorrecto! Revisá mayúsculas, espacios y puntuación. Seguís en el nivel " + gameState.getLevel() + ".");
        if (timer != null) {
            timer.start();
        }
    }

    private void endGame(String reason) {
        gameState.setGameOver(true);
        if (timer != null) {
            timer.stop();
        }
        navigator.showGameOver(gameState.getCompletedLevels(), reason, timer.getCurrentTime());
    }

    private void showFeedback(String message) {
        feedback.setText(message);
        feedbackTimer.stop();
        feedbackTimer.setOnFinished(event -> feedback.setText(""));
        feedbackTimer.playFromStart();
    }
}

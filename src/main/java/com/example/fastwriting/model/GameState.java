package com.example.fastwriting.model;

import java.util.Random;

/**
 * Almacena el estado de una partida: la palabra o frase actual, el nivel y
 * el indicador de fin de juego. Esta clase carece deliberadamente de interfaz de usuario
 * para permitir su prueba y reutilización sin necesidad de un kit de herramientas gráficas.
 */
public class GameState {
    private static final String[] WORDS = {"sol", "pan", "mar", "luz", "flor", "casa", "gato", "luna", "agua", "cafe", "guitarra", "bajo", "ritmo", "taza", "azucar"};
    private static final String[] PHRASES = {"el sol brilla hoy", "el pan esta fresco", "el mar es azul", "la luz ilumina todo", "la flor es hermosa", "la casa es grande", "el gato duerme mucho", "la luna brilla de noche", "el agua esta fria", "el cafe esta caliente", "la caja esta llena", "hay mucho ruido", "el cafe tiene azucar"};
    private final Random random = new Random();

    private String currentWord;
    private int level = 1;
    private boolean isGameOver = false;

    /**
     * Selecciona una palabra o una frase aleatoria de los conjuntos.
     */
    private void pickRandomWord() {
        String[] pool = random.nextBoolean() ? WORDS : PHRASES;
        currentWord = pool[random.nextInt(pool.length)];
    }

    /**
     * Avanza un nivel y selecciona la siguiente palabra.
     */
    public void nextLevel() {
        level += 1;
        pickRandomWord();
    }

    /**
     * Límite de tiempo para un nivel: comienza en 20 segundos y se reduce en 2 cada
     * 5 niveles, sin bajar nunca de los 2 segundos.
     *
     * @param targetLevel nivel para el que se calcula el límite
     * @return segundos permitidos para ese nivel
     */
    public int secondsForLevel(int targetLevel) {
        int INITIAL_TIME = 20; // Tiempo inicial de los niveles
        int REDUCTION_FACTOR = 4; // Cantidad de segundos a reducir
        int REDUCTION_EVERY = 2; // Cada cuántos niveles reducir

        return Math.max(
                2,
                INITIAL_TIME - REDUCTION_FACTOR * ((targetLevel - 1) / REDUCTION_EVERY)
        );
    }

    /**
     * Compara la respuesta de forma exacta (HU-1): las letras, los espacios, las mayúsculas/minúsculas
     * y la puntuación deben coincidir; por tanto, no se aplican eliminaciones de espacios ni se ignoran mayúsculas/minúsculas.
     *
     * @param answer texto introducido por el jugador
     * @return true solo cuando coincide exactamente con la palabra actual
     */
    public boolean checkAnswer(String answer) {
        return currentWord.equals(answer);
    }

    /**
     * @return la palabra o frase que el jugador debe escribir
     */
    public String getCurrentWord() {
        return currentWord;
    }

    /**
     * @return el nivel actual (comienza en 1)
     */
    public int getLevel() {
        return level;
    }

    /**
     * @return true una vez que la partida haya terminado
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * @return segundos permitidos en el nivel actual
     */
    public int getTimeLimit() {
        return secondsForLevel(level);
    }

    /**
     * @return cuántos niveles se completaron (nivel menos uno)
     */
    public int getCompletedLevels() {
        return level - 1;
    }

    /**
     * Establece la partida como finalizada o en curso.
     *
     * @param isGameOver el nuevo valor del indicador
     */
    public void setGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    /**
     * Inicia una partida nueva: nivel 1, indicador de ejecución activo y una nueva palabra aleatoria.
     */
    public void startNewGame() {
        level = 1;
        isGameOver = false;
        pickRandomWord();
    }
}

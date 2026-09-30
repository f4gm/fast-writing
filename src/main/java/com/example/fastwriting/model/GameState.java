package com.example.fastwriting.model;

import java.util.Random;

public class GameState {
    private static final String[] WORDS = {"sol", "pan", "mar", "luz", "flor", "casa", "gato", "luna", "agua", "cafe"};
    private static final String[] PHRASES = {"el sol brilla hoy", "el pan esta fresco", "el mar es azul", "la luz ilumina todo", "la flor es hermosa", "la casa es grande", "el gato duerme mucho", "la luna brilla de noche", "el agua esta fria", "el cafe esta caliente"};
    private final Random random = new Random();

    private String currentWord;
    private int level = 1;
    private boolean isGameOver = false;

    private void pickRandomWord(){
        String[] pool = random.nextBoolean() ? WORDS : PHRASES;
        currentWord = pool[random.nextInt(pool.length)];
    }

    public void nextLevel(){
        level+=1;
        pickRandomWord();
    }

    public int secondsForLevel(int targetLevel){
        return Math.max(2,20-2*((targetLevel-1)/5));
    }

    public boolean checkAnswer(String answer){
        return currentWord.equals(answer);
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public int getLevel(){
        return level;
    }

    public boolean isGameOver(){
        return isGameOver;
    }

    public int getTimeLimit(){
        return secondsForLevel(level);
    }
    public int getCompletedLevels(){
        return level-1;
    }

    public void setGameOver(boolean isGameOver){
        this.isGameOver=isGameOver;
    }

    public void startNewGame(){
        level = 1;
        isGameOver=false;
        pickRandomWord();
    }
}

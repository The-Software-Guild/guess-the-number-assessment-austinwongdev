/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 18, 2021
 * purpose: 
 */

package com.aaw.guessthenumber.model;

import java.time.LocalTime;

/**
 *
 * @author Austin Wong
 */
public class GameRound {

    private int roundId;
    private int gameId;
    private int guess;
    private LocalTime guessTime;
    private String guessResult;

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getGuess() {
        return guess;
    }

    public void setGuess(int guess) {
        this.guess = guess;
    }

    public LocalTime getGuessTime() {
        return guessTime;
    }

    public void setGuessTime(LocalTime guessTime) {
        this.guessTime = guessTime;
    }

    public String getGuessResult() {
        return guessResult;
    }

    public void setGuessResult(String guessResult) {
        this.guessResult = guessResult;
    }
    
}

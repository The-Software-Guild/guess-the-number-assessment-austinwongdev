/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 18, 2021
 * purpose: 
 */

package com.aaw.guessthenumber.service;

import com.aaw.guessthenumber.data.GuessTheNumberDao;
import com.aaw.guessthenumber.model.Game;
import com.aaw.guessthenumber.model.GameRound;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Austin Wong
 */

@Component
public class GuessTheNumberServiceImpl implements GuessTheNumberService {

    private final GuessTheNumberDao dao;
    
    @Autowired
    public GuessTheNumberServiceImpl(GuessTheNumberDao dao){
        this.dao = dao;
    }

    @Override
    public int createAndAddGame() {
        Game game = new Game();
        game.setStatus("In Progress");
        game.setAnswer(generateAnswer());
        return dao.addGame(game);
    }

    @Override
    public GameRound submitGuess(int guess, Game game) {
        String guessResult = generateGuessResult(guess, game.getAnswer());
        
        GameRound round = new GameRound();
        round.setGameId(game.getGameId());
        round.setGuess(guess);
        round.setGuessResult(guessResult);
        round.setGuessTime(LocalTime.now());
        
        return dao.addRound(round);
    }

    @Override
    public List<Game> getAllGames() {
        return dao.getAllGames();
    }

    @Override
    public Game getGame(int gameId) {
        return dao.getGame(gameId);
    }

    @Override
    public List<GameRound> getRounds(Game game) {
        return dao.getRounds(game);
    }
    
    /**
     * Generates an answer with 4 unique random digits
     * @return - 4-digit integer
     */
    private int generateAnswer() {
        List<Integer> availableNums = new ArrayList<>();
        for (int i=0; i<10; i++){
            availableNums.add(i);
        }
        String answer = "";
        Random rand = new Random();
        while (answer.length() < 4){
            answer += availableNums.remove(rand.nextInt(availableNums.size()));
        }
        return Integer.parseInt(answer);
    }
    
    /**
     * Compares guess to answer, returning a result indicating number of exact
     * matches and number of partial matches in format "e:#:p:#"
     * @param guess - Integer value of 4-digit guess
     * @param answer - Integer value of 4-digit answer
     * @return 7-character String indicating number of exact and partial matches
     */
    private String generateGuessResult(int guess, int answer) {
        
        int exactMatches = 0;
        int partialMatches = 0;
        String guessStr = String.valueOf(guess);
        String answerStr = String.valueOf(answer);
        
        for (int i=0; i<guessStr.length(); i++){
            if (guessStr.charAt(i) == answerStr.charAt(i)){
                exactMatches += 1;
            }
            else if (answerStr.contains(String.valueOf(guessStr.charAt(i)))){
                partialMatches += 1;
            }
        }
        
        return "e:" + exactMatches + ":p:" + partialMatches;
    }
    
}

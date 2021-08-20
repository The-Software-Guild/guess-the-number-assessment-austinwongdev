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
    public GameRound submitGuess(String guess, Game game) {
        
        // Evaluate guess
        String guessResult = generateGuessResult(guess, game.getAnswer());
        
        // Create GameRound object
        GameRound round = new GameRound();
        round.setGameId(game.getGameId());
        round.setGuess(guess);
        round.setGuessResult(guessResult);
        round.setGuessTime(LocalTime.now());
        
        // Add GuessRound to DB and update Game if correct guess
        if (guessResult.equals("e:4:p:0")){
            return dao.addWinningRound(round);
        }
        else{
            return dao.addLosingRound(round);
        }
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
     * @return - 4-digit number as String
     */
    private String generateAnswer() {
        List<Integer> availableNums = new ArrayList<>();
        for (int i=0; i<10; i++){
            availableNums.add(i);
        }
        String answer = "";
        Random rand = new Random();
        while (answer.length() < 4){
            answer += availableNums.remove(rand.nextInt(availableNums.size()));
        }
        return answer;
    }
    
    /**
     * Compares guess to answer, returning a result indicating number of exact
     * matches and number of partial matches in format "e:#:p:#"
     * @param guess - String value of 4-digit guess
     * @param answer - String value of 4-digit answer
     * @return 7-character String indicating number of exact and partial matches
     */
    private String generateGuessResult(String guess, String answer) {
        
        int exactMatches = 0;
        int partialMatches = 0;
        
        for (int i=0; i<answer.length(); i++){
            if (guess.charAt(i) == answer.charAt(i)){
                exactMatches += 1;
            }
            else if (answer.contains(String.valueOf(guess.charAt(i)))){
                partialMatches += 1;
            }
        }
        
        return "e:" + exactMatches + ":p:" + partialMatches;
    }
    
}

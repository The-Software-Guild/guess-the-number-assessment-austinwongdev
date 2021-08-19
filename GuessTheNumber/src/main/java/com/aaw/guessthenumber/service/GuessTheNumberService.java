/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 19, 2021
 * purpose: 
 */

package com.aaw.guessthenumber.service;

import com.aaw.guessthenumber.model.Game;
import com.aaw.guessthenumber.model.GameRound;
import java.util.List;

/**
 *
 * @author Austin Wong
 */
public interface GuessTheNumberService {

    int createAndAddGame();
    
    GameRound submitGuess(int guess, Game game);
    
    List<Game> getAllGames();
    
    Game getGame(int gameId);
    
    List<GameRound> getRounds(Game game);
    
}

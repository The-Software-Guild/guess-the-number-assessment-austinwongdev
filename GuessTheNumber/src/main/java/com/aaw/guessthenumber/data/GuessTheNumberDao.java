/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 18, 2021
 * purpose: 
 */

package com.aaw.guessthenumber.data;

import com.aaw.guessthenumber.model.Game;
import com.aaw.guessthenumber.model.GameRound;
import java.util.List;

/**
 *
 * @author Austin Wong
 */
public interface GuessTheNumberDao {

    int addGame(Game game);
    
    GameRound addRound(GameRound round);
    
    List<Game> getAllGames();
    
    Game getGame(int gameId);
    
    List<GameRound> getRounds(Game game);
    
}

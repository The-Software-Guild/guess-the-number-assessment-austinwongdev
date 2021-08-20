/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 19, 2021
 * purpose: 
 */

package com.aaw.guessthenumber.data;

import com.aaw.guessthenumber.model.Game;
import java.util.List;

/**
 *
 * @author Austin Wong
 */
public interface GameDao {

    int addGame(Game game);
    
    List<Game> getAllGames();
    
    Game getGame(int gameId);
    
}

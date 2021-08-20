/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 19, 2021
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
public interface GameRoundDao {
    
    GameRound addLosingRound(GameRound round);
    
    GameRound addWinningRound(GameRound round);
    
    List<GameRound> getRounds(Game game);
    
}

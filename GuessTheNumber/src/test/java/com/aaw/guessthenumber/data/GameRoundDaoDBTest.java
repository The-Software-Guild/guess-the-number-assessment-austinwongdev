/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaw.guessthenumber.data;

import com.aaw.guessthenumber.model.Game;
import com.aaw.guessthenumber.model.GameRound;
import java.time.LocalTime;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;


/**
 *
 * @author Austin Wong
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameRoundDaoDBTest {
   
    @Autowired
    JdbcTemplate jdbc;
    
    @Autowired
    GameDao gameDao;
    
    @Autowired
    GameRoundDao gameRoundDao;
    
    private int gameId;
    
    public GameRoundDaoDBTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    /**
     * Delete all gamerounds and games from DB, then add a game in progress
     * with answer "1234" and set gameId based on assigned id.
     */
    @Before
    public void setUp() {
        final String DELETE_ALL_ROUNDS = "DELETE FROM gameround "
                + "WHERE gameId > 0;";
        jdbc.update(DELETE_ALL_ROUNDS);
        final String DELETE_ALL_GAMES = "DELETE FROM game "
                + "WHERE gameId > 0;";
        jdbc.update(DELETE_ALL_GAMES);
        Game game = new Game();
        game.setAnswer("1234");
        game.setStatus("In Progress");
        gameId = gameDao.addGame(game);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of addLosingRound method, of class GameRoundDaoDB.
     */
    @Test
    public void testAddLosingRound() {
        GameRound round = new GameRound();
        round.setGameId(gameId);
        round.setGuess("8194");
        round.setGuessTime(LocalTime.now());
        round.setGuessResult("e:1:p:1");
        round = gameRoundDao.addLosingRound(round);
        
        Game game = gameDao.getGame(gameId);
        assertEquals("In Progress", game.getStatus());
        
        GameRound round2 = new GameRound();
        round2.setGameId(gameId);
        round2.setGuess("5678");
        round2.setGuessTime(LocalTime.now());
        round2.setGuessResult("e:0:p:0");
        round2 = gameRoundDao.addLosingRound(round2);
        
        assertEquals(round2.getRoundId(), round.getRoundId()+1);
    }

    /**
     * Test of addWinningRound method, of class GameRoundDaoDB.
     */
    @Test
    public void testAddWinningRound() {
        GameRound round = new GameRound();
        round.setGameId(gameId);
        round.setGuess("1234");
        round.setGuessTime(LocalTime.now());
        round.setGuessResult("e:4:p:0");
        gameRoundDao.addWinningRound(round);
        
        Game game = gameDao.getGame(gameId);
        assertEquals("Finished", game.getStatus());
    }

    /**
     * Test of getRounds method, of class GameRoundDaoDB.
     */
    @Test
    public void testGetRounds() {
        GameRound round = new GameRound();
        round.setGameId(gameId);
        round.setGuess("8194");
        round.setGuessTime(LocalTime.of(2, 0, 0));
        round.setGuessResult("e:1:p:1");
        round = gameRoundDao.addLosingRound(round);
        
        GameRound round2 = new GameRound();
        round2.setGameId(gameId);
        round2.setGuess("1234");
        round2.setGuessTime(LocalTime.of(9, 15, 15));
        round2.setGuessResult("e:4:p:0");
        round2 = gameRoundDao.addWinningRound(round2);
        
        Game game = gameDao.getGame(gameId);
        List<GameRound> rounds = gameRoundDao.getRounds(game);
        
        assertEquals(2, rounds.size());
        assertTrue(rounds.contains(round));
        assertTrue(rounds.contains(round2));
    }
    
}

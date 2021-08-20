/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaw.guessthenumber.data;

import com.aaw.guessthenumber.model.Game;
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
public class GameDaoDBTest {
    
    @Autowired
    JdbcTemplate jdbc;
    
    @Autowired
    GameDao gameDao;
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    /**
     * Delete all rows from gameround and game.
     */
    @Before
    public void setUp() {
        final String DELETE_ALL_ROUNDS = "DELETE FROM gameround "
                + "WHERE gameId > 0;";
        jdbc.update(DELETE_ALL_ROUNDS);
        final String DELETE_ALL_GAMES = "DELETE FROM game "
                + "WHERE gameId > 0;";
        jdbc.update(DELETE_ALL_GAMES);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of addGame and getGame method, of class GameDaoDB.
     */
    @Test
    public void testAddGetGame() {
        Game game = new Game();
        game.setAnswer("1234");
        game.setStatus("In Progress");
        int gameId = gameDao.addGame(game);
        
        Game fromDao = gameDao.getGame(gameId);
        
        assertEquals(gameId, fromDao.getGameId());
        game.setGameId(gameId);
        assertEquals(game, fromDao);
    }

    /**
     * Test of getAllGames method, of class GameDaoDB.
     */
    @Test
    public void testGetAllGames() {
        Game game1 = new Game();
        game1.setAnswer("1234");
        game1.setStatus("In Progress");
        gameDao.addGame(game1);
        
        Game game2 = new Game();
        game2.setAnswer("5678");
        game2.setStatus("Finished");
        gameDao.addGame(game2);
        
        List<Game> games = gameDao.getAllGames();
        
        assertEquals(2, games.size());
        assertTrue(games.contains(game1));
        assertTrue(games.contains(game2));
        
    }
    
}

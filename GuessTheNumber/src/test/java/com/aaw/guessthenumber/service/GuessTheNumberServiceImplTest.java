/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaw.guessthenumber.service;

import com.aaw.guessthenumber.data.GameDao;
import com.aaw.guessthenumber.model.Game;
import com.aaw.guessthenumber.model.GameRound;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
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
public class GuessTheNumberServiceImplTest {
    
    @Autowired
    JdbcTemplate jdbc;
    
    @Autowired
    GuessTheNumberService service;
    
    @Autowired
    GameDao gameDao;
    
    private Game game;
    
    public GuessTheNumberServiceImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    /**
     * Delete all gamerounds and games from DB, then add a game in progress
     * with answer "1234" and make game available to this class.
     */
    @Before
    public void setUp() {
        final String DELETE_ALL_ROUNDS = "DELETE FROM gameround "
                + "WHERE gameId > 0;";
        jdbc.update(DELETE_ALL_ROUNDS);
        final String DELETE_ALL_GAMES = "DELETE FROM game "
                + "WHERE gameId > 0;";
        jdbc.update(DELETE_ALL_GAMES);
        game = new Game();
        game.setAnswer("1234");
        game.setStatus("In Progress");
        game.setGameId(gameDao.addGame(game));
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of createAndAddGame method and getGame method, of class GuessTheNumberServiceImpl.
     */
    @Test
    public void testCreateAddGetGame() {
        int gameId = service.createAndAddGame();
        Game newGame = service.getGame(gameId);
        
        assertNotNull(newGame);
        assertNotNull(newGame.getAnswer());
        assertEquals("In Progress", newGame.getStatus());
        
        String answer = newGame.getAnswer();
        assertEquals(4, newGame.getAnswer().length());
        assertEquals(gameId, newGame.getGameId());
    }

    /**
     * Test of submitGuess method, of class GuessTheNumberServiceImpl.
     */
    @Test
    public void testSubmitGuessIncorrect() {
        String guess = "1389";
        GameRound round = new GameRound();
        round.setGameId(game.getGameId());
        round.setGuess(guess);
        round.setGuessResult("e:1:p:1");
        
        GameRound returnedRound = service.submitGuess(guess, game);
        assertNotNull(returnedRound);
        round.setRoundId(returnedRound.getRoundId());
        round.setGuessTime(returnedRound.getGuessTime());
        
        assertEquals(round, returnedRound);
    }
    
        /**
     * Test of submitGuess method, of class GuessTheNumberServiceImpl.
     */
    @Test
    public void testSubmitGuessCorrect() {
        String guess = "1234";
        GameRound round = new GameRound();
        round.setGameId(game.getGameId());
        round.setGuess(guess);
        round.setGuessResult("e:4:p:0");
        
        GameRound returnedRound = service.submitGuess(guess, game);
        assertNotNull(returnedRound);
        round.setRoundId(returnedRound.getRoundId());
        round.setGuessTime(returnedRound.getGuessTime());
        
        assertEquals(round, returnedRound);
    }

    /**
     * Test of getAllGames method, of class GuessTheNumberServiceImpl.
     */
    @Test
    public void testGetAllGames() {
        int game2Id = service.createAndAddGame();
        Game game2 = service.getGame(game2Id);
        
        List<Game> games = service.getAllGames();
        
        assertEquals(2, games.size());
        assertTrue(games.contains(game));
        assertTrue(games.contains(game2));
    }

    /**
     * Test of getRounds method, of class GuessTheNumberServiceImpl.
     */
    @Test
    public void testGetRounds() {
        GameRound round1 = service.submitGuess("5678", game);
        GameRound round2 = service.submitGuess("1234", game);
        
        List<GameRound> rounds = service.getRounds(game);
        
        assertEquals(2, rounds.size());
        assertTrue(rounds.contains(round1));
        assertTrue(rounds.contains(round2));
    }
    
}

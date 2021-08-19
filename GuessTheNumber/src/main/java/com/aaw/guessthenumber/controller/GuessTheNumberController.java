/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 18, 2021
 * purpose: 
 */

package com.aaw.guessthenumber.controller;

import com.aaw.guessthenumber.model.Game;
import com.aaw.guessthenumber.model.GameRound;
import com.aaw.guessthenumber.service.GuessTheNumberService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Austin Wong
 */

@RestController
@RequestMapping("api")
public class GuessTheNumberController {
    
    private final GuessTheNumberService service;
    
    @Autowired
    public GuessTheNumberController(GuessTheNumberService service){
        this.service = service;
    }
    
    /**
     * Starts game, generates an answer, sets status to "In Progress"
     * @return - 201 CREATED message and gameId (int)
     */
    @PostMapping("/begin")
    public ResponseEntity<Integer> beginGame(){
        return new ResponseEntity(service.createAndAddGame(), HttpStatus.CREATED); 
    }
    
    /**
     * Makes a guess by passing guess and gameId in as JSON
     * Calculates results of guess and marks game finished if correct
     * @param guess - Integer representing 4-digit guess
     * @param gameId - Integer representing ID of game to submit guess
     * @return - GameRound object with results filled in
     */
    @PostMapping("/guess")
    public ResponseEntity<GameRound> makeGuess(
            @RequestBody int guess,
            @RequestBody int gameId){
        
        Game game = service.getGame(gameId);
        if (game == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        GameRound round = service.submitGuess(guess, game);
        return ResponseEntity.ok(round);
    }
    
    /**
     * Gets a list of all Games, obfuscating answers for in-progress games
     * @return - List of Game objects
     */
    @GetMapping("/game")
    public List<Game> getAllGames(){
        return service.getAllGames();
    }
    
    /**
     * Gets a specific game based on its ID
     * @param gameId - Integer ID of game
     * @return - Game object
     */
    @GetMapping("/game/{gameId}")
    public ResponseEntity<Game> getGameById(@PathVariable int gameId){
        Game game = service.getGame(gameId);
        if (game == null){
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(game);
    }
    
    /**
     * Gets a list of rounds for the specified game, sorted by time
     * @param gameId - Integer ID of game
     * @return - List of GameRound objects
     */
    @GetMapping("/rounds/{gameId}")
    public ResponseEntity<List<GameRound>> getAllRoundsByGameId(
            @PathVariable int gameId){
        
        Game game = service.getGame(gameId);
        if (game == null){
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        List<GameRound> rounds = service.getRounds(game);
        return ResponseEntity.ok(rounds);
    }
    
}

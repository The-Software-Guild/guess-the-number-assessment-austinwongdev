/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 19, 2021
 * purpose: 
 */

package com.aaw.guessthenumber.data;

import com.aaw.guessthenumber.model.Game;
import com.aaw.guessthenumber.model.GameRound;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Austin Wong
 */

@Repository
@Profile("database")
public class GameRoundDaoDB implements GameRoundDao {
    
    private final JdbcTemplate jdbc;
    
    @Autowired
    public GameRoundDaoDB(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }
    
    /**
     * Adds a round to the database while leaving game status as in-progress
     * @param round - GameRound object to add to the database
     * @return - GameRound object added to database
     */
    @Override
    @Transactional
    public GameRound addLosingRound(GameRound round) {
        return addRound(round);
    }
    
    /**
     * Adds a round to the database and updates game status to finished in one
     * transaction
     * @param round - GameRound object to add to the database
     * @return - GameRound object added to the database
     */
    @Override
    @Transactional
    public GameRound addWinningRound(GameRound round){
        final String UPDATE_GAME_TO_FINISHED = "UPDATE game " +
                "SET status = ? " +
                "WHERE gameId = ?;";
        final String FINISHED_STATUS = "Finished";
        jdbc.update(UPDATE_GAME_TO_FINISHED,
                FINISHED_STATUS,
                round.getGameId());
        return addRound(round);
    }
    
    

    /**
     * Returns a list of all rounds in DB for a given game
     * @param game - Game object
     * @return - List of GameRound objects for the given Game
     */
    @Override
    public List<GameRound> getRounds(Game game) {
        final String GET_GAME_ROUNDS = "SELECT roundId, gameId, guess, guessTime, guessResult "
                + "FROM gameround "
                + "WHERE gameId = ? "
                + "ORDER BY guessTime;";
        return jdbc.query(GET_GAME_ROUNDS, new RoundMapper(), game.getGameId());
    }
    
    private final class RoundMapper implements RowMapper<GameRound>{
        
        @Override
        public GameRound mapRow(ResultSet rs, int index) throws SQLException{
            GameRound round = new GameRound();
            round.setGameId(rs.getInt("gameId"));
            round.setRoundId(rs.getInt("roundId"));
            round.setGuess(rs.getString("guess"));
            round.setGuessResult(rs.getString("guessResult"));
            round.setGuessTime(rs.getTime("guessTime").toLocalTime());
            return round;
        }
    }
    
    /**
     * Adds a round to the database after getting the next valid round ID for
     * the game.
     * @param round - GameRound object to add to the database
     * @return - GameRound object added to the database
     */
    private GameRound addRound(GameRound round){
        final String GET_NEXT_GAME_ROUND_ID = "SELECT IFNULL(MAX(roundId)+1, 1) " +
                "FROM gameround " +
                "WHERE gameId = ?;";
        int roundId = jdbc.queryForObject(GET_NEXT_GAME_ROUND_ID, Integer.class, round.getGameId());
        round.setRoundId(roundId);
        
        final String INSERT_ROUND = "INSERT INTO gameround(roundId, gameId, guess, guessTime, guessResult) "
                + "VALUES (?, ?, ?, ?, ?);";
        
        jdbc.update(INSERT_ROUND,
                round.getRoundId(),
                round.getGameId(),
                round.getGuess(),
                round.getGuessTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                round.getGuessResult());
        
        return round;
    }
}

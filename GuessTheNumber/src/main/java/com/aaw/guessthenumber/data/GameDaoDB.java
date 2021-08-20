/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 19, 2021
 * purpose: 
 */

package com.aaw.guessthenumber.data;

import com.aaw.guessthenumber.model.Game;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Austin Wong
 */

@Repository
@Profile("database")
public class GameDaoDB implements GameDao {
    
    private final JdbcTemplate jdbc;
    
    @Autowired
    public GameDaoDB(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }
    
    /**
     * Adds a new game to the database
     * @param game - Game to be added
     * @return - Game ID of newly added game
     */
    @Override
    public int addGame(Game game) {
        
        final String INSERT_GAME = "INSERT INTO game(answer, status) VALUES (?, ?);";
        
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbc.update((Connection conn) -> {
            
            PreparedStatement statement = conn.prepareStatement(
                    INSERT_GAME,
                    Statement.RETURN_GENERATED_KEYS);
            
            statement.setString(1, game.getAnswer());
            statement.setString(2, game.getStatus());
            return statement;
        }, keyHolder);
        
        game.setGameId(keyHolder.getKey().intValue());
        
        return game.getGameId();
    }
    
    /**
     * Returns a list of all games in DB
     * @return - List of Game objects
     */
    @Override
    public List<Game> getAllGames() {
        final String GET_ALL_GAMES = "SELECT gameId, answer, status FROM game;";
        return jdbc.query(GET_ALL_GAMES, new GameMapper());
    }

    /**
     * Searches for and returns a game by game ID
     * @param gameId - Integer representing the ID of game to search for
     * @return - Game object if ID found in DB
     */
    @Override
    public Game getGame(int gameId) {
        
        final String GET_GAME = "SELECT gameId, answer, status "
                + "FROM game "
                + "WHERE gameId = ?;";
        return jdbc.queryForObject(GET_GAME, new GameMapper(), gameId);
    }
    
    private static final class GameMapper implements RowMapper<Game>{
        
        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException{
            
            Game game = new Game();
            game.setGameId(rs.getInt("gameId"));
            game.setAnswer(rs.getString("answer"));
            game.setStatus(rs.getString("status"));
            return game;
            
        }
    }
}

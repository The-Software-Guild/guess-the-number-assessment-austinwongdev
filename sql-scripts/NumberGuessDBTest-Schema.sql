-- Austin Wong
-- 8/18/21
-- Creates database called NumberGuessDBTest designed to hold test game data for a Guess The Number game.
-- Tracks Games and Rounds.

DROP DATABASE IF EXISTS NumberGuessDBTest;
CREATE DATABASE NumberGuessDBTest;
USE NumberGuessDBTest;

CREATE TABLE game(
	gameId INT NOT NULL AUTO_INCREMENT,
    answer CHAR(4) NOT NULL,
    status VARCHAR(15) NOT NULL,
    CONSTRAINT pk_game PRIMARY KEY (gameId)
);

CREATE TABLE gameround(
	roundId INT NOT NULL,
    gameId INT NOT NULL,
    guess CHAR(4) NOT NULL,
    guessTime TIME NOT NULL,
    guessResult CHAR(7) NOT NULL,
    CONSTRAINT pk_round PRIMARY KEY (roundId, gameId),
    CONSTRAINT fk_round_game FOREIGN KEY (gameId)
		REFERENCES game (gameId)
);
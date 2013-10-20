CREATE TABLE IF NOT EXISTS `brew_players` (`fk_group_id` INTEGER , `name` VARCHAR NOT NULL , `lastRun` SMALLINT , `id` INTEGER PRIMARY KEY AUTOINCREMENT, `score` INTEGER );
CREATE TABLE IF NOT EXISTS `brew_groups` (`name` VARCHAR , `id` INTEGER PRIMARY KEY AUTOINCREMENT );
CREATE TABLE IF NOT EXISTS `brew_stats` (`highestScore` INTEGER , `id` INTEGER PRIMARY KEY AUTOINCREMENT , `lowestScore` INTEGER , `totalNumPlayers` INTEGER , `totalScore` INTEGER , `totalTimesRun` INTEGER );
CREATE TABLE IF NOT EXISTS `player_stats` (`fk_player_id` INTEGER , `highestScore` INTEGER , `id` INTEGER PRIMARY KEY AUTOINCREMENT , `lowestScore` INTEGER , `totalTimesRun` INTEGER , `totalTimesWon` INTEGER );

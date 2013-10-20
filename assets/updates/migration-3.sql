-- Rename old table to temp table
ALTER TABLE `brew_players` RENAME TO `brew_players_tmp`;
-- create new table without incorrect column name
CREATE TABLE IF NOT EXISTS `brew_players` (`fk_group_id` INTEGER , `name` VARCHAR NOT NULL , `lastRun` SMALLINT , `id` INTEGER PRIMARY KEY AUTOINCREMENT, `score` INTEGER );
-- copy from old tmp table to new table
INSERT INTO `brew_players` (`id`, `fk_group_id`, `score`, `name`, `lastRun`) SELECT tmp.`id`, tmp.`fk_group_id`, tmp.`score`, tmp.`name`, tmp.`lastRun` FROM `brew_players_tmp` tmp;
-- Drop old table
DROP TABLE `brew_players_tmp`;
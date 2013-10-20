-- Add new column inline with shema change
ALTER TABLE `brew_players` ADD COLUMN `fk_group_id` AFTER `brew_player_id`;
-- Move data across
UPDATE `brew_players` SET `fk_group_id` = `brew_player_id`;
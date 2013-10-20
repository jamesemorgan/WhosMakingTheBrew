package com.morgan.design.db;

import static com.morgan.design.utils.ObjectUtils.isNotEmpty;
import static com.morgan.design.utils.ObjectUtils.isNotNull;
import static com.morgan.design.utils.ObjectUtils.isNull;
import static com.morgan.design.utils.ObjectUtils.isZero;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.morgan.design.db.domain.BrewGroup;
import com.morgan.design.db.domain.BrewPlayer;
import com.morgan.design.db.domain.BrewStats;
import com.morgan.design.db.domain.PlayerStats;
import com.morgan.design.helpers.Logger;

public class BrewRepository {

	private static final String LOG_TAG = "BrewRepository";

	private Dao<BrewPlayer, Integer> playerDao;
	private Dao<BrewGroup, Integer> groupDao;
	private Dao<BrewStats, Integer> statsDao;
	private Dao<PlayerStats, Integer> playerStatsDao;

	public BrewRepository(final DatabaseHelper databaseHelper) {
		this.playerDao = getPlayerDao(databaseHelper);
		this.groupDao = getGroupDao(databaseHelper);
		this.statsDao = getStatsDao(databaseHelper);
		this.playerStatsDao = getPlayerStatsDao(databaseHelper);
	}

	public void deleteAllLastRunEntries() {
		try {
			final List<BrewPlayer> findLastRunPlayers = findLastRunPlayers();
			for (final BrewPlayer brewPlayer : findLastRunPlayers) {
				brewPlayer.setLastRun(false);
				this.playerDao.update(brewPlayer);
			}
		}
		catch (final SQLException e) {
			logError(e);
		}
	}

	public List<BrewGroup> findAllBrewGroups() {
		try {
			return this.groupDao.queryForAll();
		}
		catch (final SQLException e) {
			logError(e);
		}
		return new ArrayList<BrewGroup>();
	}

	public List<BrewPlayer> findLastRunPlayers() {
		try {
			return this.playerDao.queryForEq(BrewPlayer.LAST_RUN, Boolean.TRUE);
		}
		catch (final SQLException e) {
			logError(e);
		}
		return new ArrayList<BrewPlayer>();
	}

	public void insertLastRunEntry(final List<BrewPlayer> brewPlayers) {
		try {
			for (final BrewPlayer brewPlayer : brewPlayers) {
				brewPlayer.setLastRun(true);
				this.playerDao.update(brewPlayer);
			}
		}
		catch (final SQLException e) {
			logError(e);
		}
	}

	public void deleteGroup(final BrewGroup brewGroup) {
		try {
			final Collection<BrewPlayer> players = brewGroup.getBrewPlayers();
			for (final BrewPlayer brewPlayer : players) {
				this.playerDao.delete(brewPlayer);
			}
			this.groupDao.delete(brewGroup);
		}
		catch (final SQLException e) {
			logError(e);
		}
	}

	public void saveBrewPlayer(final BrewPlayer player) {
		try {
			this.playerDao.createOrUpdate(player);
		}
		catch (final SQLException e) {
			logError(e);
		}
	}

	public void deletePlayer(final BrewPlayer player) {
		try {
			// If has group and only one entry left then delete group
			if (null != player.getBrewGroup() && 1 == player.getBrewGroup().getBrewPlayers().size()) {
				this.groupDao.delete(player.getBrewGroup());
			}
			// otherwise just delete the single entry
			this.playerDao.delete(player);
		}
		catch (final SQLException e) {
			logError(e);
		}
	}

	public BrewGroup saveGroup(final String groupName, final Collection<BrewPlayer> brewPlayers) {
		try {
			final BrewGroup brewGroup = new BrewGroup();
			brewGroup.setName(groupName);
			this.groupDao.createOrUpdate(brewGroup);

			for (final BrewPlayer brewPlayer : brewPlayers) {
				brewPlayer.setBrewGroup(brewGroup);
				this.playerDao.createOrUpdate(brewPlayer);
			}
			this.groupDao.refresh(brewGroup);
			return brewGroup;
		}
		catch (final SQLException e) {
			logError(e);
		}
		return null;
	}

	public BrewGroup findGroupById(final int brewGroupId) {
		try {
			return this.groupDao.queryForId(brewGroupId);
		}
		catch (final SQLException e) {
			logError(e);
		}
		return null;
	}

	public void saveBrewStats(final List<BrewPlayer> players) {
		try {
			BrewStats brewStats = new BrewStats();
			final List<BrewStats> stats = this.statsDao.queryForAll();

			if (isNotNull(stats) && isNotEmpty(stats)) {
				brewStats = stats.get(0);
			}
			else {
				this.statsDao.create(brewStats);
			}

			// Total number of players
			brewStats.addTotalNumberOfPlayers(players.size());

			// Total scores
			int totalScore = brewStats.getTotalScore();
			for (final BrewPlayer player : players) {
				totalScore += player.getScore();
			}
			brewStats.setTotalScore(totalScore);

			// Total runs
			brewStats.incrementTotalTimesRun();

			Collections.sort(players);

			// Save highest score
			final int highestScore = players.get(0).getScore();
			if (highestScore > brewStats.getHighestScore()) {
				brewStats.setHighestScore(highestScore);
			}

			// Save lowest score
			final int currentLowestScore = brewStats.getLowestScore();
			final int lowestScore = players.get(players.size() - 1).getScore();
			if (isZero(currentLowestScore) || currentLowestScore > lowestScore) {
				brewStats.setLowestScore(lowestScore);
			}

			this.statsDao.update(brewStats);
		}
		catch (final SQLException e) {
			logError(e);
		}
	}

	public void savePlayerStats(final List<BrewPlayer> resultsList) {
		try {
			Collections.sort(resultsList);

			// /////////
			// Winner //
			// /////////

			final BrewPlayer winner = resultsList.get(0);
			resultsList.remove(0);

			PreparedQuery<PlayerStats> preparedQuery = this.playerStatsDao.queryBuilder().where()
					.eq(PlayerStats.BREW_PLAYER_ID, winner.getId()).prepare();

			PlayerStats winnerStats = this.playerStatsDao.queryForFirst(preparedQuery);

			if (isNull(winnerStats)) {
				winnerStats = new PlayerStats();
			}
			winnerStats.fromWinner(winner);
			this.playerStatsDao.createOrUpdate(winnerStats);

			// ////////////////
			// Everyone else //
			// ////////////////

			for (final BrewPlayer brewPlayer : resultsList) {

				preparedQuery = this.playerStatsDao.queryBuilder().where()
						.eq(PlayerStats.BREW_PLAYER_ID, brewPlayer.getId()).prepare();

				PlayerStats playerStats = this.playerStatsDao.queryForFirst(preparedQuery);

				if (isNull(playerStats)) {
					playerStats = new PlayerStats();
				}
				playerStats.fromPlayer(brewPlayer);
				this.playerStatsDao.createOrUpdate(playerStats);
			}
		}
		catch (final SQLException e) {
			logError(e);
		}
	}

	public void removePlayerStats(final BrewPlayer player) {
		try {
			final DeleteBuilder<PlayerStats, Integer> deleteBuilder = this.playerStatsDao.deleteBuilder();
			deleteBuilder.where().eq(PlayerStats.BREW_PLAYER_ID, player.getId());
			this.playerStatsDao.delete(deleteBuilder.prepare());
		}
		catch (final SQLException e) {
			logError(e);
		}
	}

	public void removePlayerStats(final PlayerStats playerStat) {
		try {
			this.playerStatsDao.delete(playerStat);
		}
		catch (final SQLException e) {
			logError(e);
		}
	}

	public BrewStats getBrewStats() {
		try {
			final List<BrewStats> stats = this.statsDao.queryForAll();
			return (isNotNull(stats) && isNotEmpty(stats)) ? stats.get(0) : new BrewStats();
		}
		catch (final SQLException e) {
			logError(e);
		}
		return new BrewStats();
	}

	public List<PlayerStats> getPlayerStats() {
		try {
			final PreparedQuery<PlayerStats> preparedQuery = this.playerStatsDao.queryBuilder()
					.orderBy(PlayerStats.TOTAL_TIMES_WON, true).prepare();
			return this.playerStatsDao.query(preparedQuery);
		}
		catch (final SQLException e) {
			logError(e);
		}
		return new ArrayList<PlayerStats>();
	}

	public void clearAllBrewStats() {
		try {
			final List<BrewStats> stats = this.statsDao.queryForAll();
			for (final BrewStats brewStats : stats) {
				this.statsDao.delete(brewStats);
			}
		}
		catch (final SQLException e) {
			logError(e);
		}
	}

	public void clearAllPlayerStats() {
		try {
			final List<PlayerStats> stats = this.playerStatsDao.queryForAll();
			for (final PlayerStats playerStats : stats) {
				this.playerStatsDao.delete(playerStats);
			}
		}
		catch (final SQLException e) {
			logError(e);
		}

	}

	public void updateGroup(final BrewGroup brewGroup) {
		try {
			this.groupDao.update(brewGroup);
		}
		catch (final SQLException e) {
			logError(e);
		}
	}

	public List<BrewPlayer> getPlayersByIds(final List<Integer> playerIds) {
		try {
			return this.playerDao.queryBuilder().where().in(BrewPlayer.ID, playerIds).query();
		}
		catch (final SQLException e) {
			logError(e);
		}
		return new ArrayList<BrewPlayer>();
	}

	private Dao<BrewPlayer, Integer> getPlayerDao(final DatabaseHelper databaseHelper) {
		if (isNull(this.playerDao)) {
			try {
				this.playerDao = databaseHelper.getPlayerDao();
			}
			catch (final SQLException e) {
				logError(e);
			}
		}
		return this.playerDao;
	}

	private Dao<BrewGroup, Integer> getGroupDao(final DatabaseHelper databaseHelper) {
		if (isNull(this.groupDao)) {
			try {
				this.groupDao = databaseHelper.getGroupDao();
			}
			catch (final SQLException e) {
				logError(e);
			}
		}
		return this.groupDao;
	}

	private Dao<BrewStats, Integer> getStatsDao(final DatabaseHelper databaseHelper) {
		if (isNull(this.statsDao)) {
			try {
				this.statsDao = databaseHelper.getStatsDao();
			}
			catch (final SQLException e) {
				logError(e);
			}
		}
		return this.statsDao;
	}

	private Dao<PlayerStats, Integer> getPlayerStatsDao(final DatabaseHelper databaseHelper) {
		if (isNull(this.playerStatsDao)) {
			try {
				this.playerStatsDao = databaseHelper.getPlayerStatsDao();
			}
			catch (final SQLException e) {
				logError(e);
			}
		}
		return this.playerStatsDao;
	}

	private void logError(final SQLException e) {
		Logger.e(LOG_TAG, "SQLException ", e);
		e.printStackTrace();
	}
}

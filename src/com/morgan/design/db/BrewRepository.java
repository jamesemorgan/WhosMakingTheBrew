package com.morgan.design.db;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.morgan.design.db.domain.BrewGroup;
import com.morgan.design.db.domain.BrewPlayer;
import com.morgan.design.db.domain.BrewStats;
import com.morgan.design.db.domain.PlayerStats;
import com.morgan.design.db.domain.utils.PlayerScoreComparator;
import com.morgan.design.db.domain.utils.StatsCalculator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.morgan.design.utils.ObjectUtils.isNotEmpty;
import static com.morgan.design.utils.ObjectUtils.isNotNull;
import static com.morgan.design.utils.ObjectUtils.isNull;
import static com.morgan.design.utils.ObjectUtils.isZero;

public class BrewRepository extends BaseBrewRepository {

    public BrewRepository(DatabaseHelper databaseHelper) {
        super(databaseHelper);
    }

    public void deleteAllLastRunEntries() {
        try {
            final List<BrewPlayer> findLastRunPlayers = findLastRunPlayers();
            for (final BrewPlayer brewPlayer : findLastRunPlayers) {
                brewPlayer.setLastRun(false);
                playerDao.update(brewPlayer);
            }
        } catch (final SQLException e) {
            logError(e);
        }
    }

    public List<BrewGroup> findAllBrewGroups() {
        try {
            return groupDao.queryForAll();
        } catch (final SQLException e) {
            logError(e);
        }
        return new ArrayList<BrewGroup>();
    }

    public List<BrewPlayer> findLastRunPlayers() {
        try {
            return playerDao.queryForEq(BrewPlayer.LAST_RUN, Boolean.TRUE);
        } catch (final SQLException e) {
            logError(e);
        }
        return new ArrayList<BrewPlayer>();
    }

    public void insertLastRunEntry(final List<BrewPlayer> brewPlayers) {
        try {
            for (final BrewPlayer brewPlayer : brewPlayers) {
                brewPlayer.setLastRun(true);
                playerDao.update(brewPlayer);
            }
        } catch (final SQLException e) {
            logError(e);
        }
    }

    public void deleteGroup(final BrewGroup brewGroup) {
        try {
            final Collection<BrewPlayer> players = brewGroup.getBrewPlayers();
            for (final BrewPlayer brewPlayer : players) {
                playerDao.delete(brewPlayer);
            }
            groupDao.delete(brewGroup);
        } catch (final SQLException e) {
            logError(e);
        }
    }

    public void saveBrewPlayer(final BrewPlayer player) {
        try {
            playerDao.createOrUpdate(player);
        } catch (final SQLException e) {
            logError(e);
        }
    }

    public void deletePlayer(final BrewPlayer player) {
        try {
            // If has group and only one entry left then delete group
            if (null != player.getBrewGroup() && 1 == player.getBrewGroup().getBrewPlayers().size()) {
                groupDao.delete(player.getBrewGroup());
            }
            // otherwise just delete the single entry
            playerDao.delete(player);
        } catch (final SQLException e) {
            logError(e);
        }
    }

    public BrewGroup saveGroup(final String groupName, final Collection<BrewPlayer> brewPlayers) {
        try {
            final BrewGroup brewGroup = new BrewGroup();
            brewGroup.setName(groupName);
            groupDao.createOrUpdate(brewGroup);

            for (final BrewPlayer brewPlayer : brewPlayers) {
                brewPlayer.setBrewGroup(brewGroup);
                playerDao.createOrUpdate(brewPlayer);
            }
            groupDao.refresh(brewGroup);
            return brewGroup;
        } catch (final SQLException e) {
            logError(e);
        }
        return null;
    }

    public BrewGroup findGroupById(final int brewGroupId) {
        try {
            return groupDao.queryForId(brewGroupId);
        } catch (final SQLException e) {
            logError(e);
        }
        return null;
    }

    public void saveBrewStats(final List<BrewPlayer> players) {
        try {
            BrewStats brewStats = loadBrewStat();

            // Total number of players
            brewStats.addTotalNumberOfPlayers(players.size());

            // Total scores
            brewStats.addTotalScores(StatsCalculator.countTotalScores(players));

            // Total runs
            brewStats.incrementTotalTimesRun();

            Collections.sort(players, PlayerScoreComparator.INSTANCE);

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
            statsDao.update(brewStats);
        } catch (final SQLException e) {
            logError(e);
        }
    }

    public void savePlayerStats(final List<BrewPlayer> resultsList) {
        try {
            Collections.sort(resultsList, PlayerScoreComparator.INSTANCE);

            // /////////
            // Winner //
            // /////////

            final BrewPlayer winner = resultsList.get(0);
            resultsList.remove(0);

            PreparedQuery<PlayerStats> preparedQuery = playerStatsDao.queryBuilder().where().eq(PlayerStats.BREW_PLAYER_ID, winner.getId())
                    .prepare();

            PlayerStats winnerStats = playerStatsDao.queryForFirst(preparedQuery);

            if (isNull(winnerStats)) {
                winnerStats = new PlayerStats();
            }
            winnerStats.fromWinner(winner);
            playerStatsDao.createOrUpdate(winnerStats);

            // ////////////////
            // Everyone else //
            // ////////////////

            for (final BrewPlayer brewPlayer : resultsList) {

                preparedQuery = playerStatsDao.queryBuilder().where().eq(PlayerStats.BREW_PLAYER_ID, brewPlayer.getId()).prepare();

                PlayerStats playerStats = playerStatsDao.queryForFirst(preparedQuery);

                if (isNull(playerStats)) {
                    playerStats = new PlayerStats();
                }
                playerStats.fromPlayer(brewPlayer);
                playerStatsDao.createOrUpdate(playerStats);
            }
        } catch (final SQLException e) {
            logError(e);
        }
    }

    public void removePlayerStats(final BrewPlayer player) {
        try {
            final DeleteBuilder<PlayerStats, Integer> deleteBuilder = playerStatsDao.deleteBuilder();
            deleteBuilder.where().eq(PlayerStats.BREW_PLAYER_ID, player.getId());
            playerStatsDao.delete(deleteBuilder.prepare());
        } catch (final SQLException e) {
            logError(e);
        }
    }

    public void removePlayerStats(final PlayerStats playerStat) {
        try {
            playerStatsDao.delete(playerStat);
        } catch (final SQLException e) {
            logError(e);
        }
    }

    public BrewStats getBrewStats() {
        try {
            final List<BrewStats> stats = statsDao.queryForAll();
            return (isNotNull(stats) && isNotEmpty(stats)) ? stats.get(0) : new BrewStats();
        } catch (final SQLException e) {
            logError(e);
        }
        return new BrewStats();
    }

    public List<PlayerStats> getPlayerStats() {
        try {
            final PreparedQuery<PlayerStats> preparedQuery = playerStatsDao.queryBuilder().orderBy(PlayerStats.TOTAL_TIMES_WON, true)
                    .prepare();
            return playerStatsDao.query(preparedQuery);
        } catch (final SQLException e) {
            logError(e);
        }
        return new ArrayList<PlayerStats>();
    }

    public void clearAllBrewStats() {
        try {
            final List<BrewStats> stats = statsDao.queryForAll();
            for (final BrewStats brewStats : stats) {
                statsDao.delete(brewStats);
            }
        } catch (final SQLException e) {
            logError(e);
        }
    }

    public void clearAllPlayerStats() {
        try {
            final List<PlayerStats> stats = playerStatsDao.queryForAll();
            for (final PlayerStats playerStats : stats) {
                playerStatsDao.delete(playerStats);
            }
        } catch (final SQLException e) {
            logError(e);
        }

    }

    public void updateGroup(final BrewGroup brewGroup) {
        try {
            groupDao.update(brewGroup);
        } catch (final SQLException e) {
            logError(e);
        }
    }

    public List<BrewPlayer> getPlayersByIds(final List<Integer> playerIds) {
        try {
            return playerDao.queryBuilder().where().in(BrewPlayer.ID, playerIds).query();
        } catch (final SQLException e) {
            logError(e);
        }
        return new ArrayList<BrewPlayer>();
    }

    private BrewStats loadBrewStat() throws SQLException {
        BrewStats brewStats = new BrewStats();
        final List<BrewStats> stats = statsDao.queryForAll();
        if (isNotNull(stats) && isNotEmpty(stats)) {
            brewStats = stats.get(0);
        } else {
            statsDao.create(brewStats);
        }
        return brewStats;
    }

}

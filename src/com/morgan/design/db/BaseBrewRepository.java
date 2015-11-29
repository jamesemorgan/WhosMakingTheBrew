package com.morgan.design.db;

import com.j256.ormlite.dao.Dao;
import com.morgan.design.db.domain.BrewGroup;
import com.morgan.design.db.domain.BrewPlayer;
import com.morgan.design.db.domain.BrewStats;
import com.morgan.design.db.domain.PlayerStats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static com.morgan.design.utils.ObjectUtils.isNull;

public abstract class BaseBrewRepository {

    public final Logger LOG = LoggerFactory.getLogger(BrewRepository.class);

    public Dao<BrewPlayer, Integer> playerDao;
    public Dao<BrewGroup, Integer> groupDao;
    public Dao<BrewStats, Integer> statsDao;
    public Dao<PlayerStats, Integer> playerStatsDao;

    public BaseBrewRepository(final DatabaseHelper databaseHelper) {
        playerDao = getPlayerDao(databaseHelper);
        groupDao = getGroupDao(databaseHelper);
        statsDao = getStatsDao(databaseHelper);
        playerStatsDao = getPlayerStatsDao(databaseHelper);
    }

    private Dao<BrewPlayer, Integer> getPlayerDao(final DatabaseHelper databaseHelper) {
        if (isNull(playerDao)) {
            try {
                playerDao = databaseHelper.getPlayerDao();
            } catch (final SQLException e) {
                logError(e);
            }
        }
        return playerDao;
    }

    private Dao<BrewGroup, Integer> getGroupDao(final DatabaseHelper databaseHelper) {
        if (isNull(groupDao)) {
            try {
                groupDao = databaseHelper.getGroupDao();
            } catch (final SQLException e) {
                logError(e);
            }
        }
        return groupDao;
    }

    private Dao<BrewStats, Integer> getStatsDao(final DatabaseHelper databaseHelper) {
        if (isNull(statsDao)) {
            try {
                statsDao = databaseHelper.getStatsDao();
            } catch (final SQLException e) {
                logError(e);
            }
        }
        return statsDao;
    }

    private Dao<PlayerStats, Integer> getPlayerStatsDao(final DatabaseHelper databaseHelper) {
        if (isNull(playerStatsDao)) {
            try {
                playerStatsDao = databaseHelper.getPlayerStatsDao();
            } catch (final SQLException e) {
                logError(e);
            }
        }
        return playerStatsDao;
    }

    protected void logError(final SQLException e) {
        LOG.error("SQLException ", e);
        e.printStackTrace();
    }
}

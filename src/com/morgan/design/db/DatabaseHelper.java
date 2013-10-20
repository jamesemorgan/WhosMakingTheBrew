package com.morgan.design.db;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.morgan.design.db.domain.BrewGroup;
import com.morgan.design.db.domain.BrewPlayer;
import com.morgan.design.db.domain.BrewStats;
import com.morgan.design.db.domain.PlayerStats;
import com.morgan.design.helpers.Logger;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String LOG_TAG = DatabaseHelper.class.getSimpleName();
	private static final String DATABASE_NAME = "tea_round.db";

	private static final int DATABASE_VERSION = 2;

	private final Context context;

	public DatabaseHelper(final Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	private Dao<BrewPlayer, Integer> playerDao = null;
	private Dao<BrewGroup, Integer> groupDao = null;
	private Dao<BrewStats, Integer> statsDao = null;
	private Dao<PlayerStats, Integer> playerStatsDao = null;

	@Override
	public void onCreate(final SQLiteDatabase db, final ConnectionSource connectionSource) {
		Logger.i(LOG_TAG, "onCreate");
		try {
			dropTablesIfExists(connectionSource);
			TableUtils.createTableIfNotExists(connectionSource, BrewPlayer.class);
			TableUtils.createTableIfNotExists(connectionSource, BrewGroup.class);
			TableUtils.createTableIfNotExists(connectionSource, BrewStats.class);
			TableUtils.createTableIfNotExists(connectionSource, PlayerStats.class);
		}
		catch (final SQLException e) {
			Logger.e(LOG_TAG, "Can't create database", e);
		}
	}

	/**
	 * This is called when your application is upgraded and it has a higher version number. This allows you to adjust the various data to
	 * match the new version number.
	 */
	@Override
	public void onUpgrade(final SQLiteDatabase db, final ConnectionSource connectionSource, int oldVersion, final int newVersion) {
		Logger.i(LOG_TAG, "onUpgrade, oldVersion=[%s], newVersion=[%s]", oldVersion, newVersion);
		try {

			if (newVersion == 1) {
				onCreate(db, connectionSource);
				return;
			}

			while (++oldVersion <= newVersion) {
				switch (oldVersion) {
					case 2: {
						UpgradeHelper.addUpgrade(2);
						TableUtils.createTableIfNotExists(connectionSource, PlayerStats.class);
						break;
					}
					case 3: {
						UpgradeHelper.addUpgrade(3);
						break;
					}
				}
			}

			final List<String> availableUpdates = UpgradeHelper.availableUpdates(this.context.getResources());
			Logger.d(LOG_TAG, "Found a total of %s update statements", availableUpdates.size());

			for (final String statement : availableUpdates) {
				db.beginTransaction();
				try {
					Logger.d(LOG_TAG, "Executing statement: %s", statement);
					db.execSQL(statement);
					db.setTransactionSuccessful();
				}
				finally {
					db.endTransaction();
				}
			}
		}
		catch (final SQLException e) {
			Logger.e(LOG_TAG, "Can't migrate databases, bootstrap database, data will be lost", e);
			onCreate(db, connectionSource);
		}
		catch (final android.database.SQLException e) {
			Logger.e(LOG_TAG, "Can't migrate databases, bootstrap database, data will be lost", e);
			onCreate(db, connectionSource);
		}
		catch (final Exception e) {
			Logger.e(LOG_TAG, "Can't migrate databases, bootstrap database, data will be lost", e);
			onCreate(db, connectionSource);
		}
	}

	private void dropTablesIfExists(final ConnectionSource connectionSource) throws SQLException {
		TableUtils.dropTable(connectionSource, BrewPlayer.class, true);
		TableUtils.dropTable(connectionSource, BrewGroup.class, true);
		TableUtils.dropTable(connectionSource, BrewStats.class, true);
		TableUtils.dropTable(connectionSource, PlayerStats.class, true);
	}

	@Override
	public void close() {
		super.close();
		this.groupDao = null;
		this.playerDao = null;
		this.statsDao = null;
		this.playerStatsDao = null;
	}

	public Dao<BrewPlayer, Integer> getPlayerDao() throws SQLException {
		return loadDao(this.playerDao, BrewPlayer.class);
	}

	public Dao<BrewGroup, Integer> getGroupDao() throws SQLException {
		return loadDao(this.groupDao, BrewGroup.class);
	}

	public Dao<BrewStats, Integer> getStatsDao() throws SQLException {
		return loadDao(this.statsDao, BrewStats.class);
	}

	public Dao<PlayerStats, Integer> getPlayerStatsDao() throws SQLException {
		return loadDao(this.playerStatsDao, PlayerStats.class);
	}

	private <T> Dao<T, Integer> loadDao(Dao<T, Integer> t, final Class<T> clazz) throws SQLException {
		if (t == null) {
			t = getDao(clazz);
		}
		return t;
	}

}

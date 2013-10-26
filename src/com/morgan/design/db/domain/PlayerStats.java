package com.morgan.design.db.domain;

import static com.morgan.design.utils.ObjectUtils.isZero;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "player_stats")
public class PlayerStats {

	public static final String BREW_PLAYER_ID = "fk_player_id";
	public static final String TOTAL_TIMES_WON = "totalTimesWon";

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true, columnName = BREW_PLAYER_ID)
	private BrewPlayer brewPlayer;

	@DatabaseField(canBeNull = true, defaultValue = "0")
	private int totalTimesRun;

	@DatabaseField(canBeNull = true, defaultValue = "0", columnName = TOTAL_TIMES_WON)
	private int totalTimesWon;

	@DatabaseField(canBeNull = true, defaultValue = "0")
	private int highestScore;

	@DatabaseField(canBeNull = true, defaultValue = "0")
	private int lowestScore;

	public PlayerStats() {
		//
	}

	public final int getId() {
		return id;
	}

	public final BrewPlayer getBrewPlayer() {
		return brewPlayer;
	}

	public final int getTotalTimesRun() {
		return totalTimesRun;
	}

	public final int getTotalTimesWon() {
		return totalTimesWon;
	}

	public final int getHighestScore() {
		return highestScore;
	}

	public final int getLowestScore() {
		return lowestScore;
	}

	public void setBrewPlayer(final BrewPlayer brewPlayer) {
		this.brewPlayer = brewPlayer;
	}

	public void incrementTotalTimesRun() {
		totalTimesRun++;
	}

	public void incrementTotalTimesWon() {
		totalTimesWon++;
	}

	public void determineHighestScore(final int score) {
		if (highestScore < score) {
			highestScore = score;
		}
	}

	public void determineLowestScore(final int score) {
		if (isZero(lowestScore) || lowestScore > score) {
			lowestScore = score;
		}
	}

	public void fromPlayer(final BrewPlayer brewPlayer) {
		this.brewPlayer = brewPlayer;
		incrementTotalTimesRun();
		determineHighestScore(brewPlayer.getScore());
		determineLowestScore(brewPlayer.getScore());
	}

	public void fromWinner(final BrewPlayer winner) {
		incrementTotalTimesWon();
		fromPlayer(winner);
	}
}

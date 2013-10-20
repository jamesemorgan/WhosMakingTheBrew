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

	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = BREW_PLAYER_ID)
	private BrewPlayer brewPlayer;

	@DatabaseField(canBeNull = true)
	private int totalTimesRun;

	@DatabaseField(canBeNull = true, columnName = TOTAL_TIMES_WON)
	private int totalTimesWon;

	@DatabaseField(canBeNull = true)
	private int highestScore;

	@DatabaseField(canBeNull = true)
	private int lowestScore;

	public PlayerStats() {
		//
	}

	public final int getId() {
		return this.id;
	}

	public final BrewPlayer getBrewPlayer() {
		return this.brewPlayer;
	}

	public final int getTotalTimesRun() {
		return this.totalTimesRun;
	}

	public final int getTotalTimesWon() {
		return this.totalTimesWon;
	}

	public final int getHighestScore() {
		return this.highestScore;
	}

	public final int getLowestScore() {
		return this.lowestScore;
	}

	public void setBrewPlayer(final BrewPlayer brewPlayer) {
		this.brewPlayer = brewPlayer;
	}

	public void incrementTotalTimesRun() {
		this.totalTimesRun++;
	}

	public void incrementTotalTimesWon() {
		this.totalTimesWon++;
	}

	public void determineHighestScore(final int score) {
		if (this.highestScore < score) {
			this.highestScore = score;
		}
	}

	public void determineLowestScore(final int score) {
		if (isZero(this.lowestScore) || this.lowestScore > score) {
			this.lowestScore = score;
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

package com.morgan.design.db.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "brew_stats")
public class BrewStats {

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(canBeNull = true, defaultValue = "0")
	private int totalTimesRun;

	@DatabaseField(canBeNull = true, defaultValue = "0")
	private int highestScore;

	@DatabaseField(canBeNull = true, defaultValue = "0")
	private int lowestScore;

	@DatabaseField(canBeNull = true, defaultValue = "0")
	private int totalScore;

	@DatabaseField(canBeNull = true, defaultValue = "0")
	private int totalNumPlayers;

	public BrewStats() {
		//
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public final int getTotalTimesRun() {
		return totalTimesRun;
	}

	public final void setTotalTimesRun(final int totalTimesRun) {
		this.totalTimesRun = totalTimesRun;
	}

	public final int getHighestScore() {
		return highestScore;
	}

	public final void setHighestScore(final int highestScore) {
		this.highestScore = highestScore;
	}

	public final int getLowestScore() {
		return lowestScore;
	}

	public final void setLowestScore(final int lowestScore) {
		this.lowestScore = lowestScore;
	}

	public final int getTotalScore() {
		return totalScore;
	}

	public final void setTotalScore(final int totalScore) {
		this.totalScore = totalScore;
	}

	public final int getTotalNumPlayers() {
		return totalNumPlayers;
	}

	public final void setTotalNumPlayers(final int totalNumPlayers) {
		this.totalNumPlayers = totalNumPlayers;
	}

	public int getAverageScore() {
		// total score / total number players
		final int totalScore = getTotalScore();
		final int totalNumberOfPlayers = getTotalNumPlayers();
		if (totalNumberOfPlayers == 0 || totalScore == 0) {
			return 0;
		}
		return totalScore / totalNumberOfPlayers;
	}

	public int getAverageNumberOfPlayers() {
		// total number players / total times run
		final int totalNumberOfPlayers = getTotalNumPlayers();
		final int totalTimesRun = getTotalTimesRun();
		if (totalNumberOfPlayers == 0 || totalTimesRun == 0) {
			return 0;
		}
		return totalNumberOfPlayers / totalTimesRun;
	}

	public void incrementTotalTimesRun() {
		totalTimesRun++;
	}

	public void addTotalNumberOfPlayers(final int size) {
		totalNumPlayers = totalNumPlayers + size;
	}
}
